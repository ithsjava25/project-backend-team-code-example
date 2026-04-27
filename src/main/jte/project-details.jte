@import demo.codeexample.project.ProjectDto
@import demo.codeexample.task.TaskResponseDto
@import demo.codeexample.task.TaskSummaryDto;import demo.codeexample.user.UserDto
@import java.util.List

@param ProjectDto currentProject
@param String company
@param UserDto currentUser
@param List<TaskSummaryDto> tasks

@template.layoutDashboard(
title = currentProject.getTitle(),
company = company,
currentUser = currentUser,
content = @`
    <div class="project-container" data-company="${company}" data-project-id="${currentProject.getId()}" data-project-title="${currentProject.getTitle()}">

        <header class="project-header">
            <div class="header-content">
                <span class="genre-badge">${currentProject.getGenre().toString()}</span>
                <h1>${currentProject.getTitle()}</h1>
            </div>
            <div class="header-actions">
                <a href="/dashboard/current" class="btn-back">← Back to Dashboard</a>
            </div>
        </header>

        <div class="project-grid">
            <section class="main-info">
                <div class="card description-card">
                    <h2>Description</h2>
                    <p>${currentProject.getDescription()}</p>
                </div>

                <div class="card media-card">
                    <div class="media-header">
                        <h2>Media Assets</h2>
                        <label for="detailFiles" class="btn-upload-media"> Upload Media </label>
                        <input type="file" id="detailFiles" multiple style="display: none;" accept=".png, .jpg, .jpeg, .pdf, video/*">
                    </div>

                    <div id="detailStatus" style="margin-top: 10px; font-weight: bold;"></div>

                    <div id="trailer-section" class="media-section" style="display: none;">
                        <h3>Trailer</h3>
                        <div id="project-trailer">
                        </div>
                    </div>

                    <div id="gallery-section" class="media-section" style="display: none;">
                        <h3>Gallery</h3>
                        <div class="image-grid" id="image-gallery"></div>
                    </div>

                    <div id="docs-section" class="media-section" style="display: none;">
                        <h3>Documents</h3>
                        <div class="doc-list" id="document-list"></div>
                    </div>
                </div>

            </section>

            <aside class="sidebar">
                <div class="card stats-card">
                    <div class="stat-item">
                        <span class="label">Release Date</span>
                        <span class="value">${currentProject.getReleaseDate().toString()}</span>
                    </div>
                    <div class="stat-item">
                        <span class="label">Production Status</span>
                        <div class="progress-bar">
                            <div class="progress-fill" style="width: 65%;"></div>
                        </div>
                    </div>
                </div>
                <div class="card tasks-card">
                    <h2>Production Pipeline</h2>
                    <div class="task-list">
                        @if(tasks == null || tasks.isEmpty())
                            <p style="color: var(--text-dim);">No tasks found.</p>
                        @else
                            @for(var task : tasks)
                                <a href="/${company}/tasks/${task.getId()}/view" class="task-row-link">
                                    <div class="task-row" style="padding: 10px; margin-bottom: 8px;">
                                        <div class="task-meta">
                                            <div class="status-dot status-${task.getStatus().name().toLowerCase()}"></div>
                                            <span class="task-type" style="font-size: 0.9rem;">${task.getTaskType()}</span>
                                        </div>
                                        <span class="arrow">→</span>
                                    </div>
                                </a>
                            @endfor
                        @endif
                    </div>
                </div>
        <div class="project-actions" style="margin-top: 2rem; padding: 2rem; border-top: 1px solid #e2e8f0;">
            @if(currentProject.isCompleted())
                <div style="color: #10b981; font-weight: bold; display: flex; align-items: center;">
                    <span style="font-size: 1.5rem; margin-right: 10px;">🏆</span>
                    This project is officially COMPLETED.
                </div>
            @else
                <form action="/projects/${currentProject.getId()}/finalize" method="post">
                    <button type="submit"
                            class="login-button"
                            style="background: #6366f1; width: auto; padding: 0.8rem 2rem;"
                            onclick="return confirm('Are you sure you want to finalize this project? This will close all production.')">
                        Complete Project
                    </button>
                </form>
            @endif
        </div>


            </aside>
        </div>

    <style>
        :root {
            --bg-dark: #0f0a1e;
            --card-bg: #1a142d;
            --accent-purple: #6366f1;
            --text-main: #f8fafc;
            --text-dim: #94a3b8;
        }

        body {
            background-color: var(--bg-dark);
            color: var(--text-main);
            font-family: 'Inter', sans-serif;
        }

        .project-container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 0 20px;
        }

        .header-content{
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        .project-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-end;
            margin-bottom: 40px;
            border-bottom: 1px solid rgba(255,255,255,0.1);
            padding-bottom: 20px;
        }

        .genre-badge {
            background: rgba(99, 102, 241, 0.1);
            color: var(--accent-purple);
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 0.8rem;
            border: 1px solid var(--accent-purple);
            text-transform: uppercase;
            width: fit-content;
        }

        h1 { font-size: 3rem; margin: 0; font-weight: 800; }
        h2 { color: var(--accent-purple); font-size: 1.2rem; margin-bottom: 15px; }

        .project-grid {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 30px;
        }

        .card {
            background: var(--card-bg);
            border-radius: 16px;
            padding: 25px;
            border: 1px solid rgba(255,255,255,0.05);
            margin-bottom: 20px;
        }

        .btn-back {
            color: var(--text-dim);
            text-decoration: none;
            transition: color 0.3s;
        }
        .btn-back:hover { color: var(--accent-purple); }

        .stat-item { margin-bottom: 20px; }
        .label { display: block; color: var(--text-dim); font-size: 0.9rem; }
        .value { font-weight: 700; font-size: 1.1rem; }

        .progress-bar {
            height: 8px;
            background: #334155;
            border-radius: 4px;
            margin-top: 8px;
        }
        .progress-fill {
            height: 100%;
            background: linear-gradient(90deg, #6366f1, #a855f7);
            border-radius: 4px;
            box-shadow: 0 0 15px rgba(99, 102, 241, 0.4);
        }


        .btn-upload-media {
            background: transparent;
            color: var(--accent-purple);
            border: 1px solid var(--accent-purple);
            padding: 6px 16px;
            border-radius: 8px;
            font-size: 0.85rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .btn-upload-media:hover {
            background: var(--accent-purple);
            color: white;
            /* Här skapas lyskraften (glow) */
            box-shadow: 0 0 15px rgba(99, 102, 241, 0.6), 0 0 5px rgba(99, 102, 241, 0.4);
            transform: translateY(-1px);
        }

        .media-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .media-section {
            margin-top: 25px;
            padding-top: 15px;
            border-top: 1px solid rgba(255,255,255,0.05);
        }

        .video-player {
            width: 100%;
            max-height: 400px;
            border-radius: 12px;
            background: #000;
            border: 1px solid rgba(255,255,255,0.1);
        }

        .image-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
            gap: 15px;
        }


        .doc-list {
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        /* Container för varje bild/media-item */
        .media-item {
            position: relative;
            display: inline-block;
            transition: transform 0.2s;
        }

        .media-item:hover {
            transform: scale(1.05);
        }

        .media-item img {
            width: 100%;
            height: 120px;
            object-fit: cover;
            border-radius: 8px;
            cursor: pointer;
        }

        .delete-btn {
            position: absolute;
            top: 5px;
            right: 5px;
            background: var(--danger-red);
            color: white;
            border: none;
            border-radius: 50%;
            width: 24px;
            height: 24px;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 14px;
            font-weight: bold;
            opacity: 0;
            transition: opacity 0.2s ease;
            z-index: 10;
        }

        .media-item:hover .delete-btn {
            opacity: 1;
        }

        .delete-btn:hover {
            background: var(--danger-red);
            transform: scale(1.1);
        }

    </style>

    <script src="/js/media.js"></script>
`
)