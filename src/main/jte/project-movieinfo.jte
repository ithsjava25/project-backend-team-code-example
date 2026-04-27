@import demo.codeexample.project.ProjectDto
@param ProjectDto project
@param String company

@template.layout(
title = project != null ? project.getTitle() : "Not Found",
company = company,
bodyClass = "movie-info-page",
content = @`
    <link rel="stylesheet" href="/css/home/project.css">

    @if(project != null)
        <style>
            :root {
                --bg-image: url('/${company}/api/files/${project.getId()}/project/background');
            }
        </style>
    @endif

    <div class="container">
        @if(project == null)
            <div class="main-details-card">
                <h1>PROJECT NOT FOUND</h1>
            </div>
        @else
            <div class="info-main-grid">
                <div class="info-content">
                    <h1>${project.getTitle()}</h1>

                    <div class="project-metadata">
                        <span class="category-tag">${project.getCategory()}</span>
                        <span class="genre-tag">${project.getGenre()}</span>
                        <span class="date-tag">${project.getReleaseDate().getYear()}</span>
                    </div>

                    <div class="main-details-card">
                        <h2 class="synopsis-title">Synopsis</h2>
                        <p class="synopsis-text">
                            ${project.getDescription()}
                        </p>
                    </div>

                    <div class="main-details-card trailer-container">
                        <h2 class="synopsis-title">Trailer</h2>
                        <div class="video-wrapper">
                            <video controls class="video-player">
                                <source src="/${company}/api/files/${project.getId()}/project/trailer" type="video/mp4">
                                Din webbläsare stödjer inte videouppspelning.
                            </video>
                        </div>
                    </div>
                </div>

                <div class="movie-sidebar">
                    <div class="movie-poster-box">
                        <img src="/${company}/api/files/${project.getId()}/project/cover" alt="Poster">
                    </div>
                </div>
            </div>
        @endif
    </div>
`
)