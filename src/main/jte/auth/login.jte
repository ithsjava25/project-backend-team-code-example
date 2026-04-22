@param String error
@param String company

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login — Film Studio</title>
    <style>
        body {
            background-color: #4e4e8e;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
    </style>
</head>
<body>
<div class="card p-4 shadow" style="width: 100%; max-width: 420px;">

    <!-- Logo / Title -->
    <div class="text-center mb-4">
        <h2>🎬 Film Studio</h2>
        <p class="text-muted">Sign in to your account</p>
    </div>

    <!-- Error message -->
    @if(error != null)
        <div class="alert alert-danger" role="alert">
            ${error}
        </div>
    @endif

    <!-- Email + Password Form -->
    <form method="POST" action="/${company}/login">
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
    <a href="/oauth2/authorization/google" class="btn btn-google">
        <img src="https://developers.google.com/identity/images/g-logo.png"
             width="20" height="20"
             style="margin-right: 8px; vertical-align: middle;"
             alt="Google logo">
        Continue with Google
    </a>

    <p class="text-center text-muted mt-3" style="font-size: 0.85rem;">
        Don't have an account? Contact your administrator.
    </p>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js">
</script>
</body>
</html>