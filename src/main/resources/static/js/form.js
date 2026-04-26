document.addEventListener('DOMContentLoaded', () => {
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
});