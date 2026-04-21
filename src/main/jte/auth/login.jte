@param String error
@param String company = ""

@template.layout(
title = "Log in",
company = company,
content = @`

    <header class="hero" >
        <div class="login-card">
            <div class="text-center mb-4">
                <p class="text-muted">Sign in to your account</p>
            </div>

            <!-- Error message -->
            @if(error != null)
                <div class="alert alert-danger" role="alert">
                    ${error}
                </div>
            @endif

            <!-- Email + Password Form -->
            <form method="POST" action="/${company}/web/login">
                <div class="mb-3">
                    <label for="email" class="form-label">Work Email</label>
                    <input type="email"
                           class="form-control"
                           id="email"
                           name="email"
                           placeholder="you@filmstudio.com"
                           required>
                </div>

                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password"
                           class="form-control"
                           id="password"
                           name="password"
                           placeholder="••••••••"
                           required>
                </div>
                <button type="submit" class="btn btn-primary w-100 mb-3">
                    Sign In
                </button>
            </form>

            <!-- Divider -->
            <div class="divider">or</div>

            <!-- Google Login -->
            <a href="/oauth2/authorization/google" class="btn btn-google w-100">
                <img src="https://developers.google.com/identity/images/g-logo.png"
                     width="20" height="20"
                     alt="Google logo">
                Continue with Google
            </a>

            <p class="text-center text-muted mt-3" style="font-size: 0.85rem;">
                Don't have an account? Contact your administrator.
            </p>
        </div>
    </header>
`
)