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
        <style>{
                background: linear-gradient(to bottom, rgba(20, 2, 39, 0.3), #140227),
                url('/${company}/api/files/project-poster/${project.getId()}');
                background-size: cover;
            }
        </style>
    @endif

    <div class="container" style="max-width: 1200px; margin: 0 auto; padding: 40px 20px;">
        <header style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
            <a href="/${company}" class="btn-back">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <line x1="19" y1="12" x2="5" y2="12"></line>
                    <polyline points="12 19 5 12 12 5"></polyline>
                </svg>
                BACK
            </a>
        </header>

        @if(project == null)
            <h1 style="color: #fff;">NOT FOUND</h1>
        @else
            <div class="info-main-grid">
                <div class="info-content">
                    <h1 style="font-size: 4rem; color: #fff; margin: 0 0 15px 0; line-height: 1;">
                        ${project.getTitle()}
                    </h1>

                    <div style="display: flex; gap: 20px; margin-bottom: 30px; font-size: 0.9rem;">
                        <span style="color: #a855f7;">${String.valueOf(project.getCategory())}</span>
                        <span style="color: #64748b;">|</span>
                        <span style="color: #ec4899;">${String.valueOf(project.getGenre())}</span>
                        <span style="color: #64748b;">|</span>
                        <span style="color: #cbd5e1;">${project.getReleaseDate()!= null ? project.getReleaseDate().toString() : "TBA"}</span>
                    </div>

                    <div class="main-details-card">
                        <h3 style="font-size: 0.8rem; color: #94a3b8; margin-bottom: 20px; letter-spacing: 2px;">SYNOPSIS</h3>
                        <p style="line-height: 1.8; color: #e2e8f0; font-size: 1.1rem; margin-bottom: 0;">
                            ${project.getDescription()}
                        </p>

                        <div class="trailer-box">
                            <span style="font-size: 0.8rem; letter-spacing: 2px; color: #fff;">▶ PLAY TRAILER</span>
                        </div>
                    </div>
                </div>

                <div class="info-sidebar">
                    <div class="movie-poster-box">
                        <img src="/${company}/api/files/project-poster/${project.getId()}"
                             alt="${project.getTitle()}"
                             style="width: 100%; height: 100%; object-fit: cover;"
                             onerror="this.style.display='none'; this.nextElementSibling.style.display='block';">

                        <span style="display: none; font-size: 0.7rem; color: #475569; letter-spacing: 2px;">
                                NO POSTER AVAILABLE
                            </span>
                    </div>
                </div>
            </div>
        @endif
    </div>
`
)