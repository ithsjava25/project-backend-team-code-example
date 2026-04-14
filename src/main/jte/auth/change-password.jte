@param String error
@param String success

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Change Password — Film Studio</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
          rel="stylesheet">
    <style>
        body {
            background-color: #1a1a2e;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .card {
            background-color: #16213e;
            border: 1px solid #0f3460;
            border-radius: 12px;
        }
        .btn-primary {
            background-color: #0f3460;
            border-color: #0f3460;
        }
        .btn-primary:hover {
            background-color: #e94560;
            border-color: #e94560;
        }
        .form-control {
            background-color: #0f3460;
            border-color: #1a4a8a;
            color: #ffffff;
        }
        .form-control:focus {
            background-color: #0f3460;
            color: #ffffff;
            border-color: #e94560;
            box-shadow: 0 0 0 0.2rem rgba(233,69,96,0.25);
        }
        h2 { color: #e94560; }
        .text-muted { color: #aaaaaa !important; }
        label { color: #cccccc; }
    </style>
</head>
<body>
<div class="card p-4 shadow" style="width: 100%; max-width: 420px;">

    <div class="text-center mb-4">
        <h2>🔐 Change Password</h2>
        <p class="text-muted">
            You must change your password before continuing.
        </p>
    </div>

    @if(error != null && !error.isEmpty())
        <div class="alert alert-danger" role="alert">
            ${error}
        </div>
    @endif

    @if(success != null && !success.isEmpty())
        <div class="alert alert-success" role="alert">
            ${success}
        </div>
    @endif

    <form method="POST" action="/web/change-password">
        <div class="mb-3">
            <label for="currentPassword" class="form-label">
                Current Password
            </label>
            <input type="password"
                   class="form-control"
                   id="currentPassword"
                   name="currentPassword"
                   placeholder="••••••••"
                   required>
        </div>

        <div class="mb-3">
            <label for="newPassword" class="form-label">
                New Password
            </label>
            <input type="password"
                   class="form-control"
                   id="newPassword"
                   name="newPassword"
                   placeholder="••••••••"
                   required
                   minlength="8">
        </div>

        <div class="mb-3">
            <label for="confirmPassword" class="form-label">
                Confirm New Password
            </label>
            <input type="password"
                   class="form-control"
                   id="confirmPassword"
                   name="confirmPassword"
                   placeholder="••••••••"
                   required
                   minlength="8">
        </div>

        <button type="submit" class="btn btn-primary w-100">
            Update Password
        </button>
    </form>

    <p class="text-center text-muted mt-3" style="font-size: 0.85rem;">
        <a href="/web/login" style="color: #e94560;">
            Back to login
        </a>
    </p>
</div>

<!-- Client-side password match check -->
<script>
    document.querySelector('form').addEventListener('submit', function(e) {
        const newPwd     = document.getElementById('newPassword').value;
        const confirmPwd = document.getElementById('confirmPassword').value;
        if (newPwd !== confirmPwd) {
            e.preventDefault();
            alert('New passwords do not match!');
        }
    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js">
</script>
</body>
</html>