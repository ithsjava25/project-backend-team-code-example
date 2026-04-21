@import demo.codeexample.project.ProjectDto
@import demo.codeexample.shared.Category
@import demo.codeexample.project.domain.Genre
@import java.util.List


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Project | Producer</title>
    <link rel="stylesheet" href="/css/style2.css">
</head>

<body>

<nav class="navbar">
    <div class="navbar-content">
        <a href="/web/producer/dashboard" class="logo-link">
            <div class="logo-container">
                <img src="/images/logo.png" class="logo-img">
                <div class="brand-wrapper">
                    <span class="brand-main">Hexagonal</span>
                    <span class="brand-sub">Productions</span>
                </div>
            </div>
        </a>

        <div class="nav-links">
            <a href="/dashboard">Dashboard</a>
            <a href="/projects/new" class="active-tab">New Project</a>
            <a href="/logout">Logout</a>
        </div>
    </div>
</nav>

<main class="container">
    <h1 class="page-title">Create New Project</h1>
    <p class="subtitle">Start a new film production by filling in the details below.</p>

    <div class="form-card">
        <form action="/projects" method="post">

            <label class="form-label">Project Title</label>
            <input type="text" name="title" class="form-input" required>

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

            <label class="form-label">Description</label>
            <textarea name="description" class="form-input" rows="5"></textarea>

            <button type="submit" class="btn-primary">Create Project</button>
        </form>
    </div>
</main>

</body>
</html>
