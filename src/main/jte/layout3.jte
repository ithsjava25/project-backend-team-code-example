@import gg.jte.Content
@param String title
@param Content content

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link rel="stylesheet" href="/css/style3.css">
    <link href="https://fonts.googleapis.com/css2?family=Syncopate:wght@400;700&family=Share+Tech+Mono&display=swap" rel="stylesheet">
</head>
<body>
<nav class="cyber-nav">
    <div style="color: white; font-weight: bold;">[ HEXAGONAL_CORE ]</div>
    <div style="display: flex; gap: 10px;">
        <a href="/" class="nav-link">RECORDS</a>
        <a href="/projects" class="nav-link">TRANSMISSIONS</a>
        <a href="/login" class="nav-link" style="color: var(--hxg-neon);">UPLINK</a>
    </div>
</nav>

${content}

<footer style="margin: 50px 5%; font-family: 'Share Tech Mono'; color: var(--hxg-bright); font-size: 0.7rem;">
    >> SYSTEM_STATUS: ONLINE <br>
    >> ENCRYPTION: HEX_B77
</footer>
</body>
</html>
