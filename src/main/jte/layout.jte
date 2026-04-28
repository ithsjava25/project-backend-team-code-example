@import gg.jte.Content
@param Content content
@param String company
@param String bodyClass = ""
@param String title

<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} | ${company}</title>
    <link rel="stylesheet" href="/css/style.css">
    <link href="https://fonts.googleapis.com/css2?family=Orbitron:wght@400;700&family=Oxanium:wght@400;700&family=Exo+2:wght@400;700&display=swap" rel="stylesheet">
</head>
<body class="${bodyClass}">

<nav>
    <div class="logo">
        <img src="/images/logo.png" alt="Logo" class="logo-img">
        <a href="/${company}">${company}</a>
    </div>
    <ul class="nav-links">
        <li><a href="/${company}">Home</a></li>
        <button type="button" class="nav-link-btn" id="category-trigger"> Categories <span class="dropdown-arrow">▼</span></button>
        <li><a href="/${company}/aboutUs">About Us</a></li>
        <li><a href="/${company}/login" class="btn-login">Login</a></li>
    </ul>
</nav>

<main>
    ${content}
</main>

<footer style="padding: 40px 5%; border-top: 1px solid #333; margin-top: 50px; text-align: center; color: #666;">
    <p>&copy; 2026 Hexagonal Film Productions. Created By Students of Duck Academy.</p>
</footer>

<script src="/js/drop-down.js"></script>
</body>
</html>
