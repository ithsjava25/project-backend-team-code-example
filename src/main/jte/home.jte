@param String welcomeMessage = "Neon Nights"
@param String company

@template.layout(
title = "Home",
company = company,
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
            <div class="movie-card">
                <img src="https://images.unsplash.com/photo-1616530940355-351fabd9524b?w=400&h=600&fit=crop" alt="Film 5">
            </div>
            <div class="movie-card">
            </div>
        </div>
    </div>
    <div class="movie-container">
        <button type="button" class="nav-btn left-btn" onclick="scrollGrid(-1)">&#10094;</button>

        <div class="movie-row" id="movieRow">
            <div class="movie-card"></div>
            <div class="movie-card"></div>
            <div class="movie-card"></div>
            <div class="movie-card"></div>
            <div class="movie-card"></div>
            <div class="movie-card"></div>
        </div>

        <button type="button" class="nav-btn right-btn" onclick="scrollGrid(1)">&#10095;</button>
    </div>
    <script>
        function scrollGrid(direction) {
            const row = document.getElementById('movieRow');
            const scrollAmount = 400; // Hur många pixlar den ska rulla per klick

            row.scrollBy({
                left: direction * scrollAmount,
                behavior: 'smooth'
            });
        }
    </script>
`
)
