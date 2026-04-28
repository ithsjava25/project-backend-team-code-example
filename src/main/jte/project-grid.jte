@import java.util.List
@import demo.codeexample.project.ProjectDto
@param List<ProjectDto> projects
@param String categoryTitle
@param String company

@template.layout(
title = categoryTitle,
company = company,
content = @`
    <h1 class="page-title">Category: ${categoryTitle}</h1>

    <div class="table-card">
        <div class="project-container">
            @if(projects.isEmpty())
                <p class="subtitle">Inga projekt hittades i denna kategori.</p>
            @else
                <div class="info-main-grid"> @for(var project : projects)
                        <a href="/${company}/${project.getTitle()}/${project.getId()}" style="text-decoration: none; color: inherit;">
                            <div class="main-details-card" style="margin-bottom: 20px;">
                                <h3>${project.getTitle()}</h3>
                                <p class="badge">${project.getGenre()}</p>
                            </div>
                        </a>
                    @endfor
                </div>
            @endif
        </div>
    </div>
`
)