// Admin Panel JavaScript
import { getJwtOrGoToLoginPage } from './lib/token.js';
import { showSuccessMessage, showErrorMessage, hideAllMessages } from './lib/messages.js';
import { SECRETS } from '../secrets.js';

document.addEventListener('DOMContentLoaded', function() {


    // Initialize admin panel
    initializeAdminPanel();
    
    setupEventListeners();
    // No modals needed; everything inline now
});

function initializeAdminPanel() {
   
    
    // Load user info
    loadAdminInfo();

    // Preload existing meal info into the form
    preloadMealInfo();

    // Trigger today's sales on load so we immediately hit the endpoint
    loadTodaySales();

    // Set default search date to today for quick testing
    setDefaultRecordDateToToday();
}

function setupEventListeners() {
    // Sidebar navigation
    const sidebarLinks = document.querySelectorAll('.sidebar-nav a');
    sidebarLinks.forEach(link => {
        link.addEventListener('click', handleNavigation);
    });
    
    // Logout button
    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', handleLogout);
    }
    
    // Modal close buttons
    const modalCloseButtons = document.querySelectorAll('.modal .close');
    modalCloseButtons.forEach(btn => {
        btn.addEventListener('click', closeModal);
    });

    // Set Meal Info form
    const setMealInfoForm = document.getElementById('set-meal-info-form');
    if (setMealInfoForm) {
        setMealInfoForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            hideAllMessages();

            const lunchMealType = document.getElementById('lunch-meal-type')?.value?.trim();
            const lunchMealPriceStr = document.getElementById('lunch-meal-price')?.value?.trim();
            const dinnerMealType = document.getElementById('dinner-meal-type')?.value?.trim();
            const dinnerMealPriceStr = document.getElementById('dinner-meal-price')?.value?.trim();

            if (!lunchMealType || !dinnerMealType) {
                showErrorMessage('Please fill in Lunch Meal Type and Dinner Meal Type.');
                return;
            }

            const payload = {
                mealInfoId: 'mealInfo',
                lunchMealType,
                dinnerMealType
            };

            // Include prices if provided
            const lunchMealPrice = lunchMealPriceStr !== '' ? Number(lunchMealPriceStr) : undefined;
            const dinnerMealPrice = dinnerMealPriceStr !== '' ? Number(dinnerMealPriceStr) : undefined;
            if (!Number.isNaN(lunchMealPrice) && lunchMealPrice !== undefined) payload.lunchMealPrice = lunchMealPrice;
            if (!Number.isNaN(dinnerMealPrice) && dinnerMealPrice !== undefined) payload.dinnerMealPrice = dinnerMealPrice;

            try {
                const apiResponse = await setMealInfoApiCall(payload);
                if (apiResponse && apiResponse.success) {
                    showSuccessMessage(apiResponse.message || 'Meal info saved successfully.');
                    setMealInfoForm.reset();
                    // Inline form: no modal to close
                } else {
                    showErrorMessage(apiResponse?.message || 'Failed to save meal info.');
                }
            } catch (error) {
                console.error('Error setting meal info:', error);
                showErrorMessage(error.message || 'An error occurred while setting meal info.');
            }
        });
    }

    // Search records (inline) form
    const searchRecordsForm = document.getElementById('searchRecordsForm');
    if (searchRecordsForm) {
        searchRecordsForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            hideAllMessages();
            const dateInput = document.getElementById('record-date');
            const raw = dateInput?.value; // yyyy-mm-dd from input
            const date = raw ? formatDate(raw) : '';
            if (!raw) {
                showErrorMessage('Please select a date.');
                return;
            }
            try {
                const res = await diningRecordApiCall({ date });
                console.log('Dining record response:', res);
                const totalTokensEl = document.getElementById('total-token-sales-result');
                const totalAmountEl = document.getElementById('total-sales-amount-result');
                if (res?.notFound) {
                    totalTokensEl.textContent = '0';
                    totalAmountEl.textContent = '৳ 0';
                    showErrorMessage('No dining record found for the selected date.');
                } else if (res && res.success) {
                    const dr = res.diningRecord || {};
                    const tokensSold = dr.tokensSold ?? dr.totalTokenSales ?? dr.totalTokens ?? 0;
                    const totalSales = dr.totalSales ?? dr.totalSalesAmount ?? dr.totalAmount ?? 0;
                    totalTokensEl.textContent = `${tokensSold}`;
                    totalAmountEl.textContent = `৳ ${totalSales}`;
                } else {
                    totalTokensEl.textContent = 'N/A';
                    totalAmountEl.textContent = 'N/A';
                    showErrorMessage(res?.message || 'Failed to fetch records');
                }
            } catch (err) {
                console.error(err);
                showErrorMessage(err.message || 'Error fetching records');
            }
        });
    }
    // Token validation (inline)
    const validateTokenForm = document.getElementById('validateTokenForm');
    if (validateTokenForm) {
        validateTokenForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            hideAllMessages();
            const tokenId = document.getElementById('token-id')?.value?.trim();
            const result = document.getElementById('token-validation-result');
            const statusEl = document.getElementById('token-validation-status');
            if (!tokenId) {
                showErrorMessage('Please enter a token ID');
                return;
            }
            // Loading state
            if (result) result.textContent = 'Validating…';
            if (statusEl) { statusEl.textContent = ''; statusEl.style.color = ''; }
            try {
                const res = await validateDiningTokenApiCall(tokenId);
                if (res && res.success) {
                    showSuccessMessage(res.message || 'Token validated successfully');
                    const info = res.diningTokenInfo || {};
                    if (result) result.textContent = renderDiningTokenInfo(info);
                    if (statusEl) { statusEl.textContent = 'valid'; statusEl.style.color = '#16a34a'; }
                } else {
                    const message = res?.message || 'Token validation failed';
                    showErrorMessage(message);
                    if (result) result.textContent = message;
                    if (statusEl) { statusEl.textContent = 'invalid'; statusEl.style.color = '#dc2626'; }
                }
            } catch (err) {
                console.error('Validation error:', err);
                showErrorMessage(err.message || 'Error validating token');
                if (result) result.textContent = err.message || 'Error validating token';
                if (statusEl) { statusEl.textContent = 'invalid'; statusEl.style.color = '#dc2626'; }
            }
        });
    }

    // Today's sales refresh button
    const refreshTodayBtn = document.getElementById('refresh-today-sales-button');
    if (refreshTodayBtn) {
        refreshTodayBtn.addEventListener('click', loadTodaySales);
    }
}

function handleNavigation(e) {
    e.preventDefault();
    const target = e.target.getAttribute('data-target');
    showSection(target);
}

function showSection(sectionId) {
    // Hide all sections
    const sections = document.querySelectorAll('.content-section');
    sections.forEach(section => {
        section.style.display = 'none';
    });
    
    // Show target section
    const targetSection = document.getElementById(sectionId);
    if (targetSection) {
        targetSection.style.display = 'block';
    }
    
    // Update active nav
    updateActiveNav(sectionId);
}

function updateActiveNav(activeId) {
    const navLinks = document.querySelectorAll('.sidebar-nav a');
    navLinks.forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('data-target') === activeId) {
            link.classList.add('active');
        }
    });
}

function loadDashboardData() {
    // Load dashboard statistics
    loadStats();
    loadRecentOrders();
    loadRecentBookings();
}

function loadStats() {
    // Fetch and display admin statistics
    // This would typically make API calls
}

function loadRecentOrders() {
    // Load recent food orders
}

function loadRecentBookings() {
    // Load recent hall bookings
}

function isAdminAuthenticated() {
    // Check if admin is logged in
    return localStorage.getItem('adminToken') !== null;
}

function loadAdminInfo() {
    const adminName = localStorage.getItem('adminName') || 'Admin';
    const adminNameElement = document.getElementById('admin-name');
    if (adminNameElement) {
        adminNameElement.textContent = adminName;
    }
}

function handleLogout() {
    localStorage.removeItem('adminToken');
    localStorage.removeItem('adminName');
    window.location.href = 'login.html';
}

function closeModal() {
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
        modal.style.display = 'none';
    });
}

// Utility functions
function showMessage(message, type = 'success') {
    // Display success/error messages
    console.log(`${type}: ${message}`);
}

function formatDate(date) {
    const d = new Date(date);
    const dd = String(d.getDate()).padStart(2, '0');
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    const yyyy = d.getFullYear();
    return `${dd}-${mm}-${yyyy}`;
}

function formatCurrency(amount) {
    return `$${amount.toFixed(2)}`;
}

///////////////////////////////////////////////////////////////////////////

function renderDiningTokenInfo(info) {
    try {
        if (!info || typeof info !== 'object') return 'No details available';
        const lines = [];
        if (info.tokenId) lines.push(`Token ID: ${info.tokenId}`);
        if (info.mealTime) lines.push(`Meal Time: ${info.mealTime}`);
        if (info.diningRecordDay) lines.push(`Date: ${info.diningRecordDay}`);
        if (info.status) lines.push(`Status: ${info.status}`);
        if (info.userName) lines.push(`User: ${info.userName}`);
        if (info.userEmail) lines.push(`Email: ${info.userEmail}`);
        if (lines.length) return lines.join(' | ');
        return JSON.stringify(info);
    } catch (_) {
        return 'Unable to render token details';
    }
}

function setDefaultRecordDateToToday() {
    try {
        const input = document.getElementById('record-date');
        if (!input) return;
        const today = new Date();
        const yyyy = today.getFullYear();
        const mm = String(today.getMonth() + 1).padStart(2, '0');
        const dd = String(today.getDate()).padStart(2, '0');
        // date input expects yyyy-mm-dd
        input.value = `${yyyy}-${mm}-${dd}`;
    } catch (_) {}
}

async function userInfoApiCall() {
    const jwt = await getJwtOrGoToLoginPage();
    const url = `${SECRETS.API_URL}/api/v1/user/user-info`;
    const response = await fetch(url, {
        method: 'GET',
        headers: {
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

async function diningRecordApiCall(params) {
    const jwt = await getJwtOrGoToLoginPage();
    const url = `${SECRETS.API_URL}/api/v1/dining-record/info?date=${params.date}`;
    console.log('GET dining-record/info ->', url);
    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwt}`
        }
        
    });
    console.log('Response status (dining-record/info):', response.status);
    if (response.status === 404) {
        return { notFound: true };
    }
    if (!response.ok) {
        let errorText = response.statusText;
        try {
            const errorData = await response.json();
            errorText = errorData.message || errorText;
        } catch(_) {}
        throw new Error(errorText);
    }
    const data = await response.json();
    return data;
}

async function setMealInfoApiCall(params) {
    const jwt = await getJwtOrGoToLoginPage();
    const response = await fetch(`${SECRETS.API_URL}/api/v1/admin/meal-info/set`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwt}`
        },
        body: JSON.stringify(params)
    });
    if (!response.ok) {
        let errorText = response.statusText;
        try {
            const errorData = await response.json();
            errorText = errorData.message || errorText;
        } catch (_) {}
        throw new Error(errorText);
    }
    const data = await response.json();
    return data;
}

async function getMealInfoPublic() {
    const url = `${SECRETS.API_URL}/api/v1/public/meal-info/get`;
    const response = await fetch(url, {
        method: 'GET',
        headers: {}
    });
    if (!response.ok) {
        let errorText = response.statusText;
        try {
            const errorData = await response.json();
            errorText = errorData.message || errorText;
        } catch (_) {}
        throw new Error(errorText);
    }
    return response.json();
}

async function preloadMealInfo() {
    try {
        const form = document.getElementById('set-meal-info-form');
        if (!form) return; // Form not on this page

        const res = await getMealInfoPublic();
        if (res && res.success && res.mealInfo) {
            const mi = res.mealInfo;
            const setIfExists = (id, val) => { const el = document.getElementById(id); if (el && val !== undefined && val !== null) el.value = val; };
            setIfExists('lunch-meal-type', mi.lunchMealType || '');
            setIfExists('dinner-meal-type', mi.dinnerMealType || '');
            if (typeof mi.lunchMealPrice !== 'undefined') setIfExists('lunch-meal-price', mi.lunchMealPrice);
            if (typeof mi.dinnerMealPrice !== 'undefined') setIfExists('dinner-meal-price', mi.dinnerMealPrice);
        }
    } catch (e) {
        // Non-blocking: just log. Admin can still enter fresh values.
        console.warn('Could not preload meal info:', e);
    }
}

// Removed modal setup (no modal UI used)

async function loadTodaySales() {
    try {
        const today = new Date();
        const date = formatDate(today); // dd-mm-yyyy
        const res = await diningRecordApiCall({ date });
        const totalTokensEl = document.getElementById('today-total-token-sales');
        const totalAmountEl = document.getElementById('today-total-sales-amount');
        if (res?.notFound) {
            totalTokensEl.textContent = '0';
            totalAmountEl.textContent = '৳ 0';
        } else if (res && res.success) {
            const dr = res.diningRecord || {};
            const tokensSold = dr.tokensSold ?? dr.totalTokenSales ?? dr.totalTokens ?? 0;
            const totalSales = dr.totalSales ?? dr.totalSalesAmount ?? dr.totalAmount ?? 0;
            totalTokensEl.textContent = `${tokensSold}`;
            totalAmountEl.textContent = `৳ ${totalSales}`;
        } else {
            totalTokensEl.textContent = 'N/A';
            totalAmountEl.textContent = 'N/A';
        }
    } catch (e) {
        console.warn('Failed to load today sales', e);
    }
}

async function validateDiningTokenApiCall(tokenId) {
    const jwt = await getJwtOrGoToLoginPage();
    const url = `${SECRETS.API_URL}/api/v1/admin/dining-token/validate?tokenId=${encodeURIComponent(tokenId)}`;
    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwt}`
        }
    });
    let data = null;
    try {
        data = await response.json();
    } catch (_) {}
    if (!response.ok) {
        return data || { success: false, message: response.statusText };
    }
    return data;
}