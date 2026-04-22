@import demo.codeexample.user.UserDto
@import demo.codeexample.project.ProjectDto
@import java.util.List

@param List<ProjectDto> projects
@param UserDto currentUser

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Producer Dashboard | CodeExample Productions</title>
    <link rel="stylesheet" href="/css/style2.css">
</head>
<body>

<nav class="navbar">
    <div class="navbar-content">
        <a href="/login" class="logo-link">
            <div class="logo-container">
                <img src="/images/logo.png" alt="Logo" class="logo-img">
                <div class="brand-wrapper">
                    <span class="brand-main">Hexagonal</span>
                    <span class="brand-sub">Productions</span>
                </div>
            </div>
        </a>

        <div class="nav-links">
            <a href="/dashboard" class="active-tab">Producer</a>
            <a href="/projects/new" class="login-button">New Project</a>

            @if(currentUser != null)
                <div class="user-menu">
                    <div class="user-avatar">
                        ${currentUser.getInitial()}
                    </div>
                    <div class="user-tooltip">
                        <div class="user-tooltip-name">${currentUser.getFullName()}</div>
                        <div class="user-tooltip-role">${String.valueOf(currentUser.getRole())}</div>
                        <div class="user-tooltip-email">${currentUser.getEmail()}</div>
                    </div>
                </div>
            @endif

            <a href="/logout">Logout</a>
        </div>
    </div>
</nav>

<main class="container">
    <header class="dashboard-header">
        <h1 class="page-title">Producer Dashboard</h1>

    </header>

    <div class="table-card">
        <table class="project-table">
            <thead>
                <tr>
                    <th>Project Title</th>
                    <th>Category</th>
                    <th>Genre</th>
                    <th>Progress</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                @if(projects == null || projects.isEmpty())
                    <tr>
                        <td colspan="5" style="text-align: center; padding: 3rem; color: #94a3b8;">
                            No projects found. <a href="/projects/new" style="color: #6366f1;">Create your first film!</a>
                        </td>
                    </tr>
                @else
                    @for(var project : projects)
                        <tr>
                            <td class="project-name" style="font-weight: 700;">${project.getTitle()}</td>
                            <td>
                                <span class="badge" style="color: #6366f1; border: 1px solid rgba(255,255,255,0.1); padding: 4px 10px; border-radius: 10px;">
                                    ${String.valueOf(project.getCategory())}
                                </span>
                            </td>
                            <td class="genre-text">${String.valueOf(project.getGenre())}</td>
                            <td>
                                <div class="progress-container" style="background: #e2e8f0; border-radius: 10px; width: 100px; height: 10px;">
                                    <div class="progress-fill" style="width: 50%; background: #6366f1; height: 100%; border-radius: 10px;"></div>
                                </div>
                            </td>
                            <td>
                                <a href="/projects/${project.getId()}" class="btn-details" style="text-decoration: none; color: #6366f1;">Details</a>
                            </td>
                        </tr>
                    @endfor
                @endif
            </tbody>
        </table>
    </div>
</main>

</body>
</html>