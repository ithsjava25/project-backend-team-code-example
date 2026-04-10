@import gg.jte.Content
@param String title
@param Content content

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link rel="stylesheet" href="/css/style4.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,400;0,800;1,900&display=swap" rel="stylesheet">
</head>
<body>
<div class="bg-shape"></div>
<nav>
    <div style="font-size: 1.8rem; font-weight: 900; font-style: italic;">
        HEXA<span style="color: var(--neon-yellow);">GONAL</span>
    </div>
    <div class="nav-links">
        <a href="/">Series</a>
        <a href="/projects">Movies</a>
        <a href="/login">Join Club</a>
    </div>
</nav>

${content}
</body>
</html>
