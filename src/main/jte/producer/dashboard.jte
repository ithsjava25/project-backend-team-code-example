@import demo.codeexample.project.ProjectDto
@import demo.codeexample.user.UserDto
@import java.util.List

@param List<ProjectDto> projects
@param String company
@param UserDto currentUser


@template.layoutDashboard(
title = "Dashboard",
company = company,
currentUser = currentUser,
content = @`
    <h1 class="page-title">Producer Dashboard</h1>

    <div class="table-card">
        <table class="project-table">
            <thead>
                <tr>
                    <th>Project Title</th>
                    <th>Category</th>
                    <th>Genre</th>
                    <th>Progress</th>
                    <th>Release Date</th>
                </tr>
            </thead>
            <tbody>
                @if(projects == null || projects.isEmpty())
                    <tr>
                        <td colspan="5">
                            No projects found. <a href="/projects/new" style="color: #6366f1;">Create your first film!</a>
                        </td>
                    </tr>
                @else
                    @for(var project : projects)
                        <tr class="project-row" onclick="window.location.href='/${company}/dashboard/${project.getTitle().trim().toLowerCase().replace(" ", "-")}?projectId=${project.getId()}'">

                            <td class="project-name">${project.getTitle()}</td>
                            <td>
                                <span class="badge">
                                    ${String.valueOf(project.getCategory())}
                                </span>
                            </td>
                            <td class="genre-text">${String.valueOf(project.getGenre())}</td>
                            <td>
                                <div class="progress-container">
                                    <div class="progress-fill"></div>
                                </div>
                            </td>
                            <td>
                                <a href="/${company}/dashboard/${project.getTitle().trim().toLowerCase().replace(" ", "-")}?projectId=${project.getId()}"
                                   class="btn-details">${project.getReleaseDate() != null ? project.getReleaseDate().toString() : "-"}</a>
                            </td>
                        </tr>
                    @endfor
                @endif
            </tbody>
        </table>
    </div>
`
)
