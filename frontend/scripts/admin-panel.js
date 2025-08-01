// Admin Panel JavaScript
document.addEventListener('DOMContentLoaded', function() {


    // Initialize admin panel
    initializeAdminPanel();
    
    setupEventListeners();
});

function initializeAdminPanel() {
   
    
    // Load user info
    loadAdminInfo();
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
    return new Date(date).toLocaleDateString();
}

function formatCurrency(amount) {
    return `$${amount.toFixed(2)}`;
}

///////////////////////////////////////////////////////////////////////////

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

async function diningRecordApiCall(params) {
    const jwt = await getJwtOrGoToLoginPage();
    const response = await fetch(`${SECRETS.API_URL}/api/v1/dining-record/info?date=${params.date} `, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwt}`
        },
        body: JSON.stringify(params)
        
    });
    if (!response.ok) {
        const errorData = await response.json();
        const errorText = errorData.message || response.statusText;
        throw new Error(errorText);
    }
    const data = await response.json();
    return data;
}