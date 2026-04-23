@import gg.jte.Content
@import demo.codeexample.user.UserDto

@param Content content
@param String company
@param String title
@param UserDto currentUser


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

            @if(currentUser != null)
                <div class="user-menu">
                    <div class="user-avatar">
                        ${currentUser.getFirstName().substring(0,1)}
                    </div>

                    <div class="user-dropdown">
                        <div class="user-info">
                            <div class="user-name">
                                ${currentUser.getFirstName()} ${currentUser.getLastName()}
                            </div>
                            <div class="user-email">
                                ${currentUser.getEmail()}
                            </div>
                            <div class="user-role">
                                ${currentUser.getRole()}
                            </div>
                        </div>

                        <div class="dropdown-divider"></div>

                        <a href="/${company}" class="dropdown-item logout">
                            Logout
                        </a>
                    </div>
                </div>
            @endif
        </div>
    </nav>

    <main>
        ${content}
    </main>
</body>
</html>