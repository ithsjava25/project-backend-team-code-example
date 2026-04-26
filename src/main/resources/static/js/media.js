async function uploadFiles(projectId, company, fileInputId, statusId){
    const fileInput = document.getElementById(fileInputId);
    const status = document.getElementById(statusId);
    const files = fileInput.files;

    if(files.length === 0)
        return

    for (let i = 0; i < files.length; i++){
        const file = files[i];
        if(status) status.innerText = `Uploading ${i + 1}/${files.length}: ${file.name}...`;

        try{
            const urlRes = await fetch(`/${company}/api/files/upload-url?fileName=${encodeURIComponent(file.name)}&contentType=${encodeURIComponent(file.type)}`);
            const { url } = await urlRes.json();

            await fetch(url, {
                method: 'PUT',
                body: file,
                headers: { 'Content-Type': file.type}
            });

            await fetch(`/${company}/api/files/callback?projectId=${projectId}&fileName=${encodeURIComponent(file.name)}&contentType=${encodeURIComponent(file.type)}`, {
                method: 'POST'
            });

            status.style.color = "green";
            status.innerText = "Project and files uploaded successfully! Redirecting...";
        } catch (err) {
            console.error("Upload failed for: ", file.name, err);
            status.style.color = "red";
            if (status) status.innerText = "Error: " + err.message;
            throw err;
        }
    }
}

async function loadMedia(company, projectId) {
    try {
        const res = await fetch(`/${company}/api/files/project-media/${projectId}`);
        if (!res.ok) return;

        const mediaItems = await res.json();
        console.log(mediaItems);


        const trailerSec = document.getElementById('trailer-section');
        const gallerySec = document.getElementById('gallery-section');
        const docsSec = document.getElementById('docs-section');

        const galleryGrid = document.getElementById('image-gallery');
        const docList = document.getElementById('document-list');
        const videoElement = document.getElementById('project-trailer');

        mediaItems.forEach(item => {
            if (item.type.includes('video')) {
                videoElement.src = item.url;
                trailerSec.style.display = 'block';
            }
            else if (item.type.includes('image')) {
                gallerySec.style.display = 'block';
                const img = document.createElement('img');
                img.src = item.url;
                img.onclick = () => window.open(item.url, '_blank');
                galleryGrid.appendChild(img);
            }
            else if (item.type.includes('pdf') || item.type.includes('text')) {
                docsSec.style.display = 'block';
                const link = document.createElement('a');
                link.href = item.url;
                link.className = 'doc-link';
                link.target = '_blank';
                link.innerHTML = `<span>${item.type === 'pdf' ? '📄' : '📝'}</span> ${item.name}`;
                docList.appendChild(link);
            }
        });
    } catch (err) {
        console.error("Kunde inte ladda media", err);
    }
}

document.addEventListener('DOMContentLoaded', () => {

    // LOGIC FOR PROJECT-DETAILS
    const projectView = document.querySelector('.project-container');
    if (projectView) {
        const company = projectView.dataset.company;
        const projectId = projectView.dataset.projectId;

        if (company && projectId) loadMedia(company, projectId);

        const fileInput = document.getElementById('detailFiles');
        if (fileInput) {
            fileInput.addEventListener('change', async function() {
                if (this.files.length === 0) return;
                try {
                    await uploadFiles(projectId, company, 'detailFiles', 'detailStatus');
                    setTimeout(() => location.reload(), 800);
                } catch (err) { console.error(err); }
            });
        }
    }

    // LOGIC FOR CREATE-PROJECT
    const createForm = document.getElementById('create-project-form');
    if (createForm) {
        const company = createForm.dataset.company;

        createForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            const btn = document.getElementById('submitBtn');
            btn.disabled = true;

            try {
                const formData = new URLSearchParams(new FormData(this));
                const response = await fetch(this.action, {
                    method: 'POST',
                    body: formData,
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
                });

                if (!response.ok) throw new Error("Kunde inte skapa projekt");

                const project = await response.json();

                await uploadFiles(project.id, company, 'projectFiles', 'uploadStatus');

                setTimeout(() => {
                    window.location.href = `/${company}/dashboard/current`;
                }, 1500);
            } catch (err) {
                console.error(err);
                btn.disabled = false;
            }
        });
    }
});
