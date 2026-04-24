@import demo.codeexample.project.ProjectDto
@param ProjectDto currentProject
@param String company

@template.layoutDashboard(
title = currentProject.getTitle(),
company = company,
content = @`
    <div class="project-container">
        <header class="project-header">
            <div class="header-content">
                <h1>${currentProject.getTitle()}</h1>
                <span class="category-badge">${currentProject.getCategory().toString()}</span>
                <p class="genre-tag">${currentProject.getGenre().toString()}</p>
            </div>
            <div class="header-actions">
                <a href="/${company}/dashboard/current" class="btn-back">← Back to Dashboard</a>
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
                        <video id="project-trailer" controls class="video-player">
                            Your browser does not support the video tag.
                        </video>
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

<%--                <div class="card media-placeholder">--%>
<%--                    <h2>Media Assets</h2>--%>
<%--                    <label for="detailFiles" class="btn-upload-media"> Upload Media </label>--%>
<%--                    <input type="file" id="detailFiles" multiple style="display: none;" accept=".png, .jpg, .jpeg, .pdf, video/*">--%>
<%--                    <div id="detailStatus" style="margin-top: 10px; font-weight: bold;"></div>--%>
<%--                    <div class="placeholder-box">--%>
<%--                        <img class="card-image" src="/${company}/api/files/project-poster/${currentProject.getId()}"--%>
<%--                             alt="${currentProject.getTitle()}"--%>
<%--                             style="width: 100%; height: 100%; object-fit: contain; background-color: #000;"/>--%>
<%--                    </div>--%>
<%--                </div>--%>
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

<%--                <div class="card team-card">--%>
<%--                    <h2>Team Members</h2>--%>
<%--                    <ul class="employee-list">--%>
<%--                        @for(var empId : currentProject.getEmployeesId())--%>
<%--                            <li>--%>
<%--                                <div class="avatar"></div>--%>
<%--                                <span>User ID: ${empId}</span>--%>
<%--                            </li>--%>
<%--                        @endfor--%>
<%--                    </ul>--%>
<%--                </div>--%>
            </aside>
        </div>
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

        .project-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-end;
            margin-bottom: 40px;
            border-bottom: 1px solid rgba(255,255,255,0.1);
            padding-bottom: 20px;
        }

        .category-badge {
            background: rgba(99, 102, 241, 0.1);
            color: var(--accent-purple);
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 0.8rem;
            border: 1px solid var(--accent-purple);
            text-transform: uppercase;
        }

        h1 { font-size: 3rem; margin: 10px 0; font-weight: 800; }
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

        .placeholder-box {
            height: 300px;
            background: rgba(0,0,0,0.2);
            border: 2px dashed rgba(255,255,255,0.1);
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: var(--text-dim);
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

        .employee-list { list-style: none; padding: 0; }
        .employee-list li {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 10px 0;
            border-bottom: 1px solid rgba(255,255,255,0.05);
        }
        .avatar {
            width: 32px;
            height: 32px;
            background: linear-gradient(45deg, #333, #666);
            border-radius: 50%;
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

        .image-grid img {
            width: 100%;
            height: 120px;
            object-fit: cover;
            border-radius: 8px;
            transition: transform 0.2s;
            cursor: pointer;
        }

        .image-grid img:hover {
            transform: scale(1.05);
        }

        .doc-list {
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        .doc-link {
            display: flex;
            align-items: center;
            gap: 10px;
            padding: 10px;
            background: rgba(255,255,255,0.03);
            border-radius: 6px;
            color: var(--text-main);
            text-decoration: none;
            font-size: 0.9rem;
        }

        .doc-link:hover {
            background: rgba(99, 102, 241, 0.1);
        }
    </style>

    <script src="/js/file-upload.js"></script>
    <script>
        const fileInput = document.getElementById('detailFiles');
        if (fileInput) {
            fileInput.addEventListener('change', async function() {
                if (this.files.length === 0) return;
                const status = document.getElementById('detailStatus');

                try {
                    await uploadFiles("${currentProject.getId()}", "${company}", 'detailFiles', 'detailStatus');

                    setTimeout(() => {
                        location.reload();
                    }, 800);
                } catch (err) {
                    console.error(err);
                }
            });
        }

        document.addEventListener('DOMContentLoaded', () => {
            loadMedia("${company}", "${currentProject.getId()}");
        });
    </script>
`
)