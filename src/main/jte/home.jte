@param String welcomeMessage = "Neon Nights"

@template.layout(
title = "Hem",
content = @`
    <header class="hero">
        <h1>${welcomeMessage}</h1>
        <p>A prize‑winning original production from Hexagonal Film. Experience the dark future in crystal clear 8K.</p>
    </header>

    <div class="content-section">
        <h2 class="row-title">Our Projects</h2>
        <div class="movie-row">
            <div class="movie-card">
                <img src="https://images.unsplash.com/photo-1616530940355-351fabd9524b?w=400&h=600&fit=crop" alt="Film 1">
            </div>
            <div class="movie-card">
                <img src="https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=400&h=600&fit=crop" alt="Film 2">
            </div>
            <div class="movie-card">
                <img src="https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=400&h=600&fit=crop" alt="Film 3">
            </div>
            <div class="movie-card">
                <img src="https://images.unsplash.com/photo-1478720568477-152d9b164e26?w=400&h=600&fit=crop" alt="Film 4">
            </div>
        </div>
    </div>
`
)
