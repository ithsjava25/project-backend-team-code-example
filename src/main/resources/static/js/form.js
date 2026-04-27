document.addEventListener('DOMContentLoaded', () => {
    // --- 1. Dropdown Logic ---
    const closeAll = () => document.querySelectorAll('.dropdown-suggestions').forEach(d => d.style.display = 'none');
    document.addEventListener('click', closeAll);

    document.querySelectorAll('.select-btn').forEach(btn => {
        const container = btn.closest('.task-group');
        const list = container.querySelector('.dropdown-suggestions');
        const input = container.querySelector('input[type="hidden"]');
        const btnText = btn.querySelector('.btn-text');

        btn.onclick = (e) => {
            e.stopPropagation();
            const isVisible = list.style.display === 'block';
            closeAll();

            if (btn.dataset.role && !isVisible) {
                const users = JSON.parse(document.querySelector('.worker-assignment-section').dataset.users);
                list.innerHTML = users
                    .filter(u => u.role === btn.dataset.role)
                    .map(u => `<div class="item" data-id="${u.id}">${u.name}</div>`)
                    .join('');
            }
            list.style.display = isVisible ? 'none' : 'block';
        };

        list.onclick = (e) => {
            const item = e.target.closest('.item, .genre-item, .category-item');
            if (!item) return;

            e.stopPropagation();
            btnText.textContent = item.textContent.trim();
            input.value = item.dataset.id || item.dataset.value || item.textContent.trim();
            list.style.display = 'none';
        };
    });

    // --- 2. Form-handler ---
    const createForm = document.getElementById('create-project-form');
    if (createForm) {
        createForm.addEventListener('submit', async function(e) {
            e.preventDefault();

            document.querySelectorAll('.error-msg').forEach(el => el.remove());
            document.querySelectorAll('.input-error').forEach(el => el.classList.remove('input-error'));

            const formData = new FormData(this);
            const company = this.dataset.company;

            try {
                const response = await fetch(`/${company}/projects`, {
                    method: 'POST',
                    body: new URLSearchParams(formData)
                });

                if (response.ok) {
                    const data = await response.json();
                    await uploadFiles(data.id, company, formData.get('title'), 'projectFiles', 'uploadStatus');
                    window.location.href = `/${company}/dashboard/current`;
                } else if (response.status === 400) {
                    const errors = await response.json();
                    displayErrors(errors);
                }
            } catch (err) {
                console.error("Submit error:", err);
            }
        });
    }
});

// --- 3. Show error ---
function displayErrors(errors) {
    for (const [fieldName, message] of Object.entries(errors)) {
        const input = document.querySelector(`[name="${fieldName}"]`);
        if (input) {
            input.classList.add('input-error');

            const errorSpan = document.createElement('span');
            errorSpan.className = 'error-msg';
            errorSpan.innerText = message;
            input.parentNode.appendChild(errorSpan);
        }
    }
}