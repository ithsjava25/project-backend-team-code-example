@import demo.codeexample.project.ProjectDto
@import demo.codeexample.shared.Category
@import demo.codeexample.project.domain.Genre
@import demo.codeexample.user.UserDto
@import java.util.List

@param List<UserDto> users
@param String company

@template.layoutDashboard(
title = "Create",
company = company,
content = @`
<main class="container">
    <h1 class="page-title">Create New Project</h1>
    <p class="subtitle">Start a new film production by filling in the details below.</p>

    <div class="form-card">
        <form action="/${company}/projects" method="post">

            <label class="form-label">Project Title</label>
            <input type="text" name="title" class="form-input" required>

            <input type="hidden" name="companyId" value="1">

            <label class="form-label">Category</label>
            <select name="category" class="form-input" required>
                @for(var c : Category.values())
                    <option value="${c.name()}">${c.name()}</option>
                @endfor
            </select>

            <label class="form-label">Release Date</label>
            <input type="date"
                   name="releaseDate"
                   class="form-input"
                   min="${java.time.LocalDate.now().toString()}"
                   required>

            <label class="form-label">Genre</label>
            <select name="genre" class="form-input" required>
                @for(var g : Genre.values())
                    <option value="${g.name()}">${g.name()}</option>
                @endfor
            </select>

            <div class="task-group" style="margin-bottom: 20px; position: relative;">
                <label class="form-label">Assign Recruiting Worker (Role: Recruiter)</label>
                <input type="text"
                       class="form-input worker-search"
                       placeholder="Search name..."
                       data-role="RECRUITER"
                       autocomplete="off">
                <input type="hidden" name="employeesId">
                <div class="dropdown-suggestions" style="display: none;"></div>
            </div>

            <div class="task-group" style="margin-bottom: 20px; position: relative;">
                <label class="form-label">Assign Recording Worker (Role: Director)</label>
                <input type="text"
                       class="form-input worker-search"
                       placeholder="Search name..."
                       data-role="DIRECTOR"
                       autocomplete="off">
                <input type="hidden" name="employeesId">
                <div class="dropdown-suggestions" style="display: none;"></div>
            </div>

            <div class="task-group" style="margin-bottom: 20px; position: relative;">
                <label class="form-label">Assign Editing Worker (Role: Editor)</label>
                <input type="text"
                       class="form-input worker-search"
                       placeholder="Search name..."
                       data-role="EDITOR"
                       autocomplete="off">
                <input type="hidden" name="employeesId">
                <div class="dropdown-suggestions" style="display: none;"></div>
            </div>

            <label class="form-label">Deadline for Recruiting</label>
            <input type="datetime-local" name="recruitingDeadline" class="form-input" required>

            <label class="form-label">Deadline for Recording</label>
            <input type="datetime-local" name="recordingDeadline" class="form-input" required>

            <label class="form-label">Deadline for Editing</label>
            <input type="datetime-local" name="editingDeadline" class="form-input" required>

            <label class="form-label">Description</label>
            <textarea name="description" class="form-input" rows="5"></textarea>

            <label class="form-label">Project Media (Images/PDFs)</label>
            <input type="file" id="projectFiles" class="form-input" multiple accept="image/*,application/pdf">
            <div id="uploadStatus" style="margin-top: 10px; font-size: 0.9rem;"></div>

            <button type="submit" class="btn-primary" id="submitBtn">Create Project & Upload</button>
<%--            <button type="submit" class="btn-primary">Create Project</button>--%>
        </form>
    </div>
</main>

<script>

    const allUsers = [
        @for(var u : users)
        { id: "${u.getId()}", name: "${u.getFirstName()} ${u.getLastName()}", role: "${u.getRole()}" },
        @endfor
    ];

    document.querySelectorAll('.worker-search').forEach(input => {
        const container = input.parentElement;
        const suggestionBox = container.querySelector('.dropdown-suggestions');
        const hiddenInput = container.querySelector('input[type="hidden"]');
        const targetRole = input.getAttribute('data-role');

        input.addEventListener('input', function() {
            const val = this.value.toLowerCase();
            hiddenInput.value = '';
            suggestionBox.innerHTML = '';

            if (!val) {
                suggestionBox.style.display = 'none';
                return;
            }


            const matches = allUsers.filter(u =>
                u.name.toLowerCase().includes(val) && u.role === targetRole
            );

            for (let i = 0; i < matches.length; i++) {
                const matchObj = matches[i];
                const div = document.createElement('div');
                div.className = 'worker-item';
                div.textContent = matchObj.name;

                div.onclick = function() {
                    input.value = matchObj.name;
                    hiddenInput.value = matchObj.id;
                    suggestionBox.style.display = 'none';
                };
                suggestionBox.appendChild(div);
            }
            suggestionBox.style.display = matches.length > 0 ? 'block' : 'none';
        });
    });

        const teamSearch = document.getElementById('teamSearch');
        if (teamSearch) {
                teamSearch.addEventListener('input', function() {
                        const term = this.value.toLowerCase();
                        document.querySelectorAll('.team-member-item').forEach(item => {
                                const name = item.innerText.toLowerCase();
                                item.style.display = name.includes(term) ? 'flex' : 'none';
                            });
                    });
            }

    document.addEventListener('click', e => {
        if (!e.target.classList.contains('worker-search')) {
            document.querySelectorAll('.dropdown-suggestions').forEach(d => d.style.display = 'none');
        }
    });
</script>
<script>
    const currentCompany = "${company}"
    @raw
    document.querySelector('form').addEventListener('submit', async function(e) {
        e.preventDefault();

        const btn = document.getElementById('submitBtn');
        const status = document.getElementById('uploadStatus');
        btn.disabled = true;
        status.style.color = "inherit"; // Återställ färg
        status.innerText = "Creating project...";

        try {
            // 1. Skapa projektet via AJAX
            // Vi använder URLSearchParams för att simulera ett vanligt formulärinskick
            const formData = new URLSearchParams(new FormData(this));

            const response = await fetch(this.action, {
                method: 'POST',
                body: formData, // Spring läser detta via @ModelAttribute
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });

            if (!response.ok) {
                const errorData = await response.text();
                throw new Error("Could not create one project: " + errorData);
            }

            const project = await response.json();
            const projectId = project.id;

            // 2. Hantera filer
            const fileInput = document.getElementById('projectFiles');
            const files = fileInput.files;

            if (files.length > 0) {
                for (let i = 0; i < files.length; i++) {
                    const file = files[i];
                    status.innerText = `Uploading file ${i + 1} of ${files.length}: ${file.name}...`;

                    // A. Hämta Presigned URL
                    const urlRes = await fetch(`/${currentCompany}/api/files/upload-url?fileName=${encodeURIComponent(file.name)}&contentType=${encodeURIComponent(file.type)}`);
                    const { url } = await urlRes.json();

                    // B. Ladda upp till S3
                    await fetch(url, {
                        method: 'PUT',
                        body: file,
                        headers: { 'Content-Type': file.type }
                    });

                    // C. Callback till servern för att spara i S3File-tabellen
                    await fetch(`/${currentCompany}/api/files/callback?projectId=${projectId}&fileName=${encodeURIComponent(file.name)}&contentType=${encodeURIComponent(file.type)}`, {
                        method: 'POST'
                    });
                }
            }

            status.style.color = "green";
            status.innerText = "Project and files uploaded successfully! Redirecting...";

            setTimeout(() => {
                window.location.href = "/${currentCompany}/producer/dashboard";
            }, 1500);

        } catch (err) {
            console.error(err);
            status.style.color = "red";
            status.innerText = "Error: " + err.message;
            btn.disabled = false;
        }
    });
    @endraw
</script>
`
)


