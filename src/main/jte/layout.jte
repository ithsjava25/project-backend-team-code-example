@import gg.jte.Content
@param String title
@param Content content

<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} | Hexagonal Film</title>
    <link rel="stylesheet" href="/css/style.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;700;800&display=swap" rel="stylesheet">
</head>
<body>

<nav>
    <div class="logo">⬢ Hexa<span>gonal</span></div>
    <ul class="nav-links">
        <li><a href="/">Home</a></li>
        <li><a href="/projects">Projects</a></li>
        <li><a href="/aboutUs">About Us</a></li>
        <li><a href="/login" class="btn-login">Login</a></li>
    </ul>
</nav>

<main>
    ${content}
</main>

<footer style="padding: 40px 5%; border-top: 1px solid #333; margin-top: 50px; text-align: center; color: #666;">
    <p>&copy; 2026 Hexagonal Film Productions. Created By Students of Duck Academy.</p>
</footer>

</body>
</html>
