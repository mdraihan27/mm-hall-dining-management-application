import { getJwtOrGoToLoginPage } from './lib/token.js';
import { showSuccessMessage, showErrorMessage, hideAllMessages } from './lib/messages.js';
import { SECRETS } from '../secrets.js';

document.addEventListener('DOMContentLoaded', async () => {
    getJwtOrGoToLoginPage();
    const username = document.getElementById('account-name');
    username.textContent = localStorage.getItem('name') || 'User';

    const logoutButton = document.getElementById('logoutButton');

    logoutButton.addEventListener('click', function () {
        localStorage.removeItem('jwt');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('email');
        localStorage.removeItem('name');
        window.location.href = '../pages/index.html';
    });

    const response = await userInfoApiCall();

    if (response && response.success) {
        console.log('User Info:', response.userInfo);
        const balance = document.getElementById('account-balance');
        balance.textContent = `৳ ${response.userInfo.balance}`;

        // Show Admin Panel action if user has admin role
        const roles = response.userInfo?.roles || response.userInfo?.authorities || [];
        const isAdmin = Array.isArray(roles)
            ? roles.some(r => {
                const v = typeof r === 'string' ? r : (r?.authority || r?.role || r?.name);
                return typeof v === 'string' && v.toLowerCase().includes('admin');
              })
            : false;

        const adminBtn = document.getElementById('adminPanelBtn');
        if (isAdmin && adminBtn) {
            adminBtn.style.display = 'flex';
        }
    }
});

async function userInfoApiCall() {

    const jwt = await getJwtOrGoToLoginPage();

    const response = await fetch(`${SECRETS.API_URL}/api/v1/user/user-info`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwt}`
        }

    });

    if (!response.ok) {
        const errorData = await response.json();
        const errorText = errorData.message || response.statusText;
        throw new Error(errorText);
    }

    const data = await response.json();
    return data;

}


