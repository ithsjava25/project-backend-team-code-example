document.addEventListener('DOMContentLoaded', () => {
    const trigger = document.getElementById('category-trigger');
    const menu = document.getElementById('category-menu');

    trigger.addEventListener('click', (e) => {
        e.stopPropagation();
        menu.classList.toggle('active');
    });

    document.addEventListener('click', (e) => {
        if (!menu.contains(e.target) && e.target !== trigger) {
            menu.classList.remove('active');
        }
    });

    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') menu.classList.remove('active');
    });
});