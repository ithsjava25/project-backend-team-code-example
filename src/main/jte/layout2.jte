@import gg.jte.Content
@param String title
@param Content content

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link rel="stylesheet" href="/css/style2.css">
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:ital,wght@0,400;1,700&family=Inter:wght@300;600&display=swap" rel="stylesheet">
</head>
<body>
<nav class="nav-minimal">
    <a href="/">Index</a>
    <div style="font-size: 1.5rem; font-weight: bold; letter-spacing: -2px;">H X G .</div>
    <a href="/login">Studio Access</a>
</nav>

${content}

<footer style="padding: 100px 5%; text-align: left; opacity: 0.5; font-family: 'Inter', sans-serif;">
    <p>Hexagonal Film Productions / Est. 2026</p>
</footer>
</body>
</html>
