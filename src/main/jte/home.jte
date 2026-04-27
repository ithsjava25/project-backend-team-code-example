@import demo.codeexample.project.ProjectDto
@import java.util.List

@param List<ProjectDto> projects
@param List<ProjectDto> films
@param List<ProjectDto> series

@param String company


@template.layout(
title = "Home",
company = company,
content = @`
    <header class="hero">
        <h1>Neon Nights</h1>
        <p>A prize‑winning original production from Hexagonal Film. Experience the dark future in crystal clear 8K.</p>
    </header>

    <div class="content-section">
        <h2 class="row-title">Our Projects</h2>
        <div class="movie-container">
            <button type="button" class="nav-btn left-btn" onclick="scrollGrid(this,-1)">&#10094;</button>

            <div class="movie-row">
                @for(ProjectDto project : projects)
                 <a href="/${company}/${project.getTitle().replace(" ", "-")}/${project.getId()}" style="text-decoration: none; color: inherit;">
                    <div class="movie-card-wrapper">
                        <div class="movie-card">
                            <img class="card-image" src="/api/files/${project.getId()}/project/cover"
                                 alt="${project.getTitle()}"
                                 style="width: 100%; height: 100%; object-fit: contain; background-color: #000;"/>
                        </div>
                        <div class="movie-info">
                            <span class="project-title">${project.getTitle()} </span>
                        </div>
                    </div>
                 </a>
                @endfor
            </div>

            <button type="button" class="nav-btn right-btn" onclick="scrollGrid(this,1)">&#10095;</button>
        </div>
    </div>
    <div class="content-section">
        <h2 class="row-title">Films/Movies</h2>
        <div class="movie-container">
            <button type="button" class="nav-btn left-btn" onclick="scrollGrid(this,-1)">&#10094;</button>

            <div class="movie-row" >
                @for(ProjectDto film : films)
                <a href="/${film.getTitle().replace(" ", "-")}/${film.getId()}">
                    <div class="movie-card-wrapper">
                        <div class="movie-card">
                            <img class="card-image"
                                     src="/api/files/${film.getId()}/project/cover"
                                     alt="${film.getTitle()}"
                                     style="width: 100%; height: 100%; object-fit: contain; background-color: #000;"/>
                        </div>
                        <div class="movie-info">
                            <span class="project-title">${film.getTitle()} </span>
                        </div>
                    </div>
                </a>
                @endfor
            </div>

            <button type="button" class="nav-btn right-btn" onclick="scrollGrid(this,1)">&#10095;</button>
        </div>
    </div>
    <div class="content-section">
        <h2 class="row-title">Series</h2>
        <div class="movie-container">
            <button type="button" class="nav-btn left-btn" onclick="scrollGrid(this,-1)">&#10094;</button>

            <div class="movie-row" >
                @for(ProjectDto serie : series)
                <a href="/${serie.getTitle().replace(" ", "-")}/${serie.getId()}" class="card-link">
                    <div class="movie-card-wrapper">
                        <div class="movie-card">
                            <img class="card-image"
                                     src="/api/files/${serie.getId()}/project/cover"
                                     alt="${serie.getTitle()}"
                                     style="width: 100%; height: 100%; object-fit: contain; background-color: #000;"/>
                        </div>
                        <div class="movie-info">
                            <span class="project-title">${serie.getTitle()} </span>
                        </div>
                    </div>
                </a>
                @endfor
            </div>

            <button type="button" class="nav-btn right-btn" onclick="scrollGrid(this,1)">&#10095;</button>
        </div>
    </div>
    <script>
        function scrollGrid(buttonElement, direction) {
            const container = buttonElement.closest('.movie-container');
            const row = container.querySelector('.movie-row');
            const scrollAmount = 400;

            row.scrollBy({
                left: direction * scrollAmount,
                behavior: 'smooth'
            });
        }
    </script>
`
)
