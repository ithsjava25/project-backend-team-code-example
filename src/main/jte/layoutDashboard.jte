@import gg.jte.Content
@param Content content
@param String company
@param String title

<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} | ${company}</title>
    <link rel="stylesheet" href="/css/style2.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;700;800&display=swap" rel="stylesheet">
</head>
<body>
    <nav class="navbar">
        <div class="navbar-content">
            <a href="/${company}" class="logo-link">
                <div class="logo-container">
                    <img src="/images/logo.png" alt="Logo" class="logo-img">
                    <div class="brand-wrapper">
                        <span class="brand-main">${company}</span>
                    </div>
                </div>
            </a>

            <div class="nav-links">
                <a href="/${company}/dashboard" class="active-tab">Current Projects</a>
                <a href="/${company}/projects/new" class="login-button">New Project</a>
                <a href="/${company}">Logout</a>
            </div>
        </div>
    </nav>

    <main>
        ${content}
    </main>
</body>
</html>