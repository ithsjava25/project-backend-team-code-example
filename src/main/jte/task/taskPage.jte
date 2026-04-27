

@import demo.codeexample.task.TaskResponseDto
@param TaskResponseDto task
@param Long currentUserId
@param String company

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
        <a href="/${company}/dashboard/current" style="color: #6366f1; text-decoration: none;">&larr; Back to Dashboard</a>
    </header>

    <div class="table-card" style="padding: 2rem;">
        <div style="display: flex; justify-content: space-between;">
            <h2>${task.getTaskType()}</h2>
            <span class="badge status-${task.getStatus().name().toLowerCase()}">
            ${task.getStatus()}
        </span>
        </div>

        <div class="action-zone" style="margin-top: 2rem;">
            @if(task.getStatus().name().equals("PENDING"))
                <div style="background: #f1f5f9; padding: 1.5rem; border-radius: 8px; border: 1px dashed #cbd5e1;">
                    <p style="color: #64748b; margin: 0;">
                        <span style="font-size: 1.2rem; margin-right: 8px;">🔒</span>
                        This stage is currently <strong>Pending</strong>.
                        It will become available once the previous task is completed.
                    </p>
                </div>

            @elseif(task.getStatus().name().equals("ASSIGNED"))
                @if(task.getUserId().equals(currentUserId))
                    <form action="/tasks/${task.getId()}/accept" method="post">
                        <button type="submit" class="login-button" style="background: #6366f1; width: auto; padding: 0.8rem 2rem;">
                            Accept Task
                        </button>
                    </form>
                @else
                    <p style="color: #64748b; font-style: italic;">Assigned to another worker.</p>
                @endif

            @elseif(task.getStatus().name().equals("IN_PROGRESS"))
                @if(task.getUserId().equals(currentUserId))
                    <form action="/tasks/${task.getId()}/complete" method="post">
                        <button type="submit" class="login-button" style="background: #10b981; width: auto; padding: 0.8rem 2rem;">
                            Mark as Completed
                        </button>
                    </form>
                @else
                    <p style="color: #64748b; font-style: italic;">Worker is currently recording/editing...</p>
                @endif

            @else
                <div style="display: flex; align-items: center; color: #10b981;">
                    <span style="font-size: 1.5rem; margin-right: 10px;">✅</span>
                    <p style="font-weight: bold; margin: 0;">This task has been finished successfully.</p>
                </div>
            @endif
        </div>
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

        <form action="/${company}/${task.getId()}/comments" method="post" style="margin-top: 2rem; border-top: 1px solid #f1f5f9; padding-top: 2rem;">
            <input type="hidden" name="taskId" value="${task.getId()}">
            <textarea name="content" rows="3" placeholder="Add a comment..." required
                      style="width: 100%; padding: 1rem; border: 1px solid #e2e8f0; border-radius: 8px; font-family: inherit;"></textarea>
            <button type="submit" class="login-button" style="margin-top: 1rem; border: none; cursor: pointer;">Post</button>
        </form>
    </div>
</main>
</body>
</html>