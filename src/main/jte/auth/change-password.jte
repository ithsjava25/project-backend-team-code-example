@param String error
@param String success

@param String company


@template.layout(
title = "Home",
company = company,
content = @`

    <header class="hero">
        <div class="login-card">

            <div class="text-center mb-4">
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

        <form method="POST" action="${company == null || company.isEmpty() ? "" : "/" + company}/login/change-password">
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
            <button type="submit" class="btn btn-primary w-100 mb-3">
                Update Password
            </button>
        </form>
        <p class="text-center text-muted mt-3" style="font-size: 0.85rem;">
            <a href="/${company}/login" style="color: #e94560;">
                Back to login
            </a>
        </p>
    </div>
</header>

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
`
)