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
                    <div class="movie-card-wrapper">
                        <div class="movie-card">
                            <img class="card-image" src="/${company}/api/files/project-poster/${project.getId()}"
                                 alt="${project.getTitle()}"
                                 style="width: 100%; height: 100%; object-fit: contain; background-color: #000;"/>
                        </div>
                        <div class="movie-info">
                            <span class="project-title">${project.getTitle()} </span>
                        </div>
                    </div>
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
                    <div class="movie-card-wrapper">
                        <div class="movie-card">
                            <img class="card-image" src="" alt="${film.getTitle()}" />
                        </div>
                        <div class="movie-info">
                            <span class="project-title">${film.getTitle()} </span>
                        </div>
                    </div>
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
                    <div class="movie-card-wrapper">
                        <div class="movie-card">
                            <img class="card-image" src="" alt="${serie.getTitle()}" />
                        </div>
                        <div class="movie-info">
                            <span class="project-title">${serie.getTitle()} </span>
                        </div>
                    </div>
                @endfor
            </div>

            <button type="button" class="nav-btn right-btn" onclick="scrollGrid(this,1)">&#10095;</button>
        </div>
    </div>
    <script>
        function scrollGrid(buttonElement, direction) {
            const container = buttonElement.closest('.movie-container');
            const row = container.querySelector('.movie-row');
            const scrollAmount = 400; // Hur många pixlar den ska rulla per klick

            row.scrollBy({
                left: direction * scrollAmount,
                behavior: 'smooth'
            });
        }
    </script>
`
)
