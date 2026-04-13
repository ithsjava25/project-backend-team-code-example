@param String csrfToken
@param boolean isAuthenticated

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${csrfToken}">
    <meta name="_csrf_header" content="X-CSRF-TOKEN">
    <title>S3 File Manager</title>
</head>
<body>
<h1>S3 File Manager</h1>
<p>Welcome! <!-- {displayName}! --></p>
<div class="nav">
    <a href="/" class="button">Home</a>
    <form action="/logout" method="post" style="display:inline;">
        <input type="hidden" name="_csrf" value="${csrfToken}">
        <button type="submit" class="button">Logout</button>
    </form>
</div>

<div id="uploadSection">
    <h3>Upload File</h3>
    <input type="file" id="fileInput" style="display: none">

    <label for="fileInput" class="button">Choose file</label>
    <p id="fileNameDisplay" style="margin-top: 10px; font-style: italic; color: #555;">No file chosen</p>

    <button onclick="uploadFile()" class="button">Upload to S3</button>
    <p id="status"></p>
</div>
<script>
    // Vi hämtar elementen
    const fileInput = document.getElementById('fileInput');
    const fileNameDisplay = document.getElementById('fileNameDisplay');

    // Vi lyssnar efter "change"-eventet (när användaren valt en fil)
    fileInput.addEventListener('change', function() {
        if (this.files && this.files.length > 0) {
            // Hämta namnet på den första filen
            const fileName = this.files[0].name;
            // Uppdatera texten på skärmen
            fileNameDisplay.textContent = "Selected file: " + fileName;
        } else {
            fileNameDisplay.textContent = "No file chosen";
        }
    });
</script>

<h3>Files in S3</h3>
<table class="file-list">
    <thead>
    <tr>
        <th>File Name</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody id="fileTableBody">
    <!-- Loaded via JS -->
    </tbody>
</table>

<script>
    @raw
    async function fetchFiles() {
        const res = await apiRequest('/api/files'); // Defaults to GET
        if (!res) return; // Exit if the page is reloading from a 403
        const files = await res.json();
        const body = document.getElementById('fileTableBody');
        body.innerHTML = '';
        files.forEach(file => {
            if (!file || typeof file !== 'string') return;

            const tr = document.createElement('tr');

            // File name cell
            const tdName = document.createElement('td');
            tdName.textContent = file; // Safe: prevents XSS

            // Actions cell
            const tdActions = document.createElement('td');

            // Download button
            const downloadBtn = document.createElement('button');
            downloadBtn.textContent = 'Download';
            downloadBtn.className = 'button';
            downloadBtn.style.marginRight = '8px';
            downloadBtn.addEventListener('click', async () => {
                downloadBtn.disabled = true;
                try {
                    await downloadFile(file);
                } finally {
                    downloadBtn.disabled = false;
                }
            });

            // Delete button
            const deleteBtn = document.createElement('button');
            deleteBtn.textContent = 'Delete';
            deleteBtn.className = 'button';
            deleteBtn.addEventListener('click', async () => {
                const confirmed = confirm(`Delete "${file}"?`);
                if (!confirmed) return;

                deleteBtn.disabled = true;
                try {
                    await deleteFile(file);
                } finally {
                    deleteBtn.disabled = false;
                }
            });

            // Assemble row
            tdActions.appendChild(downloadBtn);
            tdActions.appendChild(deleteBtn);
            tr.appendChild(tdName);
            tr.appendChild(tdActions);

            body.appendChild(tr);
        });
    }

    async function uploadFile() {
        const input = document.getElementById('fileInput');
        if (input.files.length === 0) return;
        const file = input.files[0];
        const status = document.getElementById('status');

        const fileNameDisplay = document.getElementById('fileNameDisplay');
        status.innerText = 'Getting presigned URL...';

        try {
            // 1. Get Presigned URL (Now using apiRequest for auto-403 handling)
            const res = await apiRequest(`/api/files/upload-url?fileName=${encodeURIComponent(file.name)}&contentType=${encodeURIComponent(file.type)}`);
            if (!res) return; // Stop if 403 reload triggered
            const {url} = await res.json();

            // 2. PUT to S3 (Keep using native fetch - S3 doesn't know your CSRF)
            status.innerText = 'Uploading directly to S3...';
            const putRes = await fetch(url, {
                method: 'PUT',
                body: file,
                headers: {'Content-Type': file.type}
            });

            if (putRes.ok) {
                status.innerText = 'Upload successful! Notifying server...';

                // 3. Callback to server (Using apiRequest for CSRF and 403 handling)
                await apiRequest(`/api/files/callback?fileName=${encodeURIComponent(file.name)}`, {
                    method: 'POST'
                });

                status.innerText = 'Upload and notification successful!';
                fetchFiles();

                if (fileNameDisplay) {
                    fileNameDisplay.textContent = "no file chosen"
                }
                input.value = '';

            } else {
                status.innerText = 'S3 Upload failed: ' + putRes.statusText;
            }
        } catch (err) {
            status.innerText = 'Error: ' + err.message;
        }
    }

    async function downloadFile(fileName) {
        // 1. Get the signed URL from your Java API
        const res = await apiRequest(`/api/files/download-url?fileName=${encodeURIComponent(fileName)}`);

        // 2. If res is null, the wrapper is already reloading the page (403)
        if (!res) return;

        // 3. If successful, open the S3 link in a new tab
        const {url} = await res.json();
        window.open(url, '_blank');
        // Use location.href if window.open is blocked from a async method
        //window.location.href = url;
    }
    async function deleteFile(fileName) {
        if (!confirm(`Delete ${fileName}?`)) return;
        const res = await apiRequest(`/api/files?fileName=${encodeURIComponent(fileName)}`,{
            method: 'DELETE'
        });
        if (!res) return;

        if (res.ok) {
            await fetchFiles();
        } else {
            alert('Delete failed!');
        }
    }

    async function apiRequest(url, options = {}) {
        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

        options.credentials = options.credentials || 'same-origin';
        options.headers = options.headers || {};

        // NEW: Tell fetch NOT to follow redirects automatically
        options.redirect = 'manual';

        const method = options.method ? options.method.toUpperCase() : 'GET';
        if (method !== 'GET' && method !== 'HEAD') {
            options.headers[csrfHeader] = csrfToken;
        }

        const response = await fetch(url, options);

        // NEW: Check if the response type is 'opaqueredirect' or status is 403/401
        // 'opaqueredirect' means the server tried to send you to Google/Login
        // 401 = Not logged in (Session expired)
        // 403 = CSRF invalid or No permission
        if (response.status === 401 || response.status === 403) {
            console.warn("Authentication issue. Reloading...");
            window.location.reload();
            return null;
        }

        return response;
    }

    fetchFiles();
    @endraw
</script>
</body>
</html>
