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
        <form action="/${company}/projects" method="post" id="create-project-form" data-company="${company}">

            <label class="form-label">Project Title</label>
            <input type="text" name="title" class="form-input" required>

            <input type="hidden" name="companyName" value="${company}">

            <label class="form-label">Category</label>
            <div class="task-group" style="position: relative;">
                <button type="button" class="select-btn custom-dropdown-trigger" id="category-btn">
                    <span class="btn-text">Select Category</span>
                    <span class="dropdown-arrow">▼</span>
                </button>
                <input type="hidden" name="category" id="category-input" required>

                <div class="dropdown-suggestions category-dropdown" style="display: none;">
                    @for(var c : Category.values())
                        <div class="item category-item" data-value="${c.name()}">
                            ${c.name()}
                        </div>
                    @endfor
                </div>
            </div>

            <label class="form-label">Release Date</label>
            <input type="date"
                   name="releaseDate"
                   class="form-input"
                   min="${java.time.LocalDate.now().toString()}"
                   required>

            <label class="form-label">Genre</label>
            <div class="task-group" style="position: relative;">
                <button type="button" class="select-btn custom-dropdown-trigger" id="genre-btn">
                    <span class="btn-text">Select Genre</span>
                    <span class="dropdown-arrow">▼</span>
                </button>
                <input type="hidden" name="genre" id="genre-input" required>

                <div class="dropdown-suggestions genre-dropdown" style="display: none;">
                    @for(var g : Genre.values())
                        <div class="item genre-item" data-value="${g.name()}">
                            ${g.name()}
                        </div>
                    @endfor
                </div>
            </div>

            <div class="worker-assignment-section" data-users='[@for(var i = 0; i < users.size(); i++){"id": "${users.get(i).getId()}", "name": "${users.get(i).getFirstName()} ${users.get(i).getLastName()}", "role": "${users.get(i).getRole()}"}${i < users.size() - 1 ? "," : ""}@endfor]'>

                @for(var role : List.of("RECRUITER", "DIRECTOR", "EDITOR"))
                    <div class="task-group" style="margin-bottom: 20px; position: relative;">
                        <label class="form-label">
                            Assign ${role.substring(0,1) + role.substring(1).toLowerCase()}
                        </label>
                        <button type="button"
                                class="form-input select-btn"
                                data-role="${role}">
                            <span class="btn-text">Select ${role.toLowerCase()}...</span>
                            <span class="dropdown-arrow">▼</span>
                        </button>
                        <input type="hidden" name="employeesId">
                        <div class="dropdown-suggestions" style="display: none;"></div>
                    </div>
                @endfor
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

<script src="/js/form.js"></script>
<script src="/js/media.js"></script>
`
)


