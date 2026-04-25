

@import demo.codeexample.task.TaskResponseDto
@param TaskResponseDto task

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Task Details | Hexagonal Productions</title>
    <link rel="stylesheet" href="/css/style2.css">
</head>
<body>

<nav class="navbar">
    <div class="navbar-content">
        <a href="/dashboard" class="logo-link">
            <div class="logo-container">
                <img src="/images/logo.png" alt="Logo" class="logo-img">
                <div class="brand-wrapper">
                    <span class="brand-main">Hexagonal</span>
                    <span class="brand-sub">Productions</span>
                </div>
            </div>
        </a>
    </div>
</nav>

<main class="container">
    <header class="dashboard-header">
        <h1 class="page-title">Task Details</h1>
        <a href="/dashboard" style="color: #6366f1; text-decoration: none;">&larr; Back to Dashboard</a>
    </header>

    <div class="table-card" style="padding: 2rem; margin-bottom: 2rem;">
        <div style="display: flex; justify-content: space-between; align-items: center;">
            <h2 style="margin: 0; color: #1e293b;">${task.getTitle()}</h2>
            <span class="badge" style="background: #6366f1; color: white; padding: 6px 14px; border-radius: 20px; font-weight: 600;">
                ${task.getStatus().name()}
            </span>
        </div>
        <p style="color: #64748b; margin-top: 1rem;">${task.getDescription()}</p>
    </div>

    <div class="table-card" style="padding: 2rem;">
        <h3 style="margin-bottom: 1.5rem; color: #1e293b;">Discussion</h3>

        <div class="comments-list">
            @if(task.getComments() == null || task.getComments().isEmpty())
                <p style="color: #94a3b8; font-style: italic; padding: 1rem 0;">No comments yet.</p>
            @else
                @for(var comment : task.getComments())
                    <div style="border-left: 3px solid #6366f1; background: #f8fafc; padding: 1.2rem; margin-bottom: 1rem; border-radius: 0 8px 8px 0;">
                        <p style="margin: 0 0 0.5rem 0; color: #334155; line-height: 1.5;">${comment.getContent()}</p>
                        <div style="display: flex; justify-content: space-between; align-items: center; color: #94a3b8; font-size: 0.85rem;">
            <span>
                <img src="/images/user-icon.png" style="width: 12px; opacity: 0.5; margin-right: 4px;">
                ${comment.getUserName()}
            </span>

                            <span>${comment.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, HH:mm"))}</span>

                        </div>
                    </div>
                @endfor
            @endif
        </div>

        <form action="/${task.getId()}/comments" method="post" style="margin-top: 2rem; border-top: 1px solid #f1f5f9; padding-top: 2rem;">
            <input type="hidden" name="taskId" value="${task.getId()}">
            <textarea name="content" rows="3" placeholder="Add a comment..." required
                      style="width: 100%; padding: 1rem; border: 1px solid #e2e8f0; border-radius: 8px; font-family: inherit;"></textarea>
            <button type="submit" class="login-button" style="margin-top: 1rem; border: none; cursor: pointer;">Post</button>
        </form>
    </div>
</main>
</body>
</html>