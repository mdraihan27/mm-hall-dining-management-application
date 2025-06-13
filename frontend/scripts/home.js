// Force dark theme and prevent automatic theme switching
document.documentElement.setAttribute('data-theme', 'dark');

// Override any theme switching based on device preference
if (window.matchMedia) {
    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', function () {
        document.documentElement.setAttribute('data-theme', 'dark');
    });
}


const username = document.getElementById('username');
username.textContent = localStorage.getItem('name') || 'User';

const logoutButton = document.getElementById('logoutButton');

logoutButton.addEventListener('click', function () {
    localStorage.removeItem('jwt');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('email');
    localStorage.removeItem('name');
    window.location.href = '../pages/index.html';
});

