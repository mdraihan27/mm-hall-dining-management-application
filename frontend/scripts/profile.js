import { getJwtOrGoToLoginPage } from './lib/token.js';
import { showSuccessMessage, showErrorMessage, hideAllMessages } from './lib/messages.js';
import { SECRETS } from '../secrets.js';


// Initialize page
document.addEventListener('DOMContentLoaded', async () => {
    await getJwtOrGoToLoginPage();
        await loadUserProfile();
});

// Toggle edit forms
function toggleEditForm(formId) {
    hideAllMessages();
    const form = document.getElementById(formId);
    if (form) {
        form.classList.toggle('active');
        
        // Focus on first input when opening
        if (form.classList.contains('active')) {
            const firstInput = form.querySelector('input');
            if (firstInput) {
                setTimeout(() => firstInput.focus(), 100);
            }
        }
    }
}

// Cancel edit and close form
function cancelEdit(formId) {
    const form = document.getElementById(formId);
    if (form) {
        form.classList.remove('active');
        // Clear form inputs
        const inputs = form.querySelectorAll('input');
        inputs.forEach(input => input.value = '');
    }
    hideAllMessages();
}

// Submit name change
async function submitNameChange() {
    const newName = document.getElementById('new-name-input').value.trim();
    
    if (!newName) {
        showErrorMessage('Please enter a valid name');
        return;
    }

    
    try {
        const result = await updateNameApiCall({ newName: newName });
        if (!result.success) {  
            showErrorMessage('Failed to update name. '+result.message);
            return;
        }
        document.getElementById('username').textContent = newName;
        cancelEdit('name-form');
        showSuccessMessage('Name updated successfully');
        
    } catch (error) {
        showErrorMessage('Failed to update name. Please try again.');
        console.error('Name update error:', error);
    }
}

// Submit password change
async function submitPasswordChange() {
    const currentPassword = document.getElementById('prev-pass').value;
    const newPassword = document.getElementById('new-password').value;
    const confirmPassword = document.getElementById('retype-new-password').value;
    
    // Validation
    if (!currentPassword) {
        showErrorMessage('Please enter your current password');
        return;
    }
    
    if (!newPassword) {
        showErrorMessage('Please enter a new password');
        return;
    }
    
    if (newPassword.length < 8) {
        showErrorMessage('New password must be at least 8 characters long');
        return;
    }
    
    if (newPassword !== confirmPassword) {
        showErrorMessage('New passwords do not match');
        return;
    }
    
    try {
        const result = await changePasswordApiCall({oldPassword:currentPassword, newPassword:newPassword});
        if (!result.success) {
            showErrorMessage('Failed to update password. '+result.message);  
            return;
        }
        
        cancelEdit('password-form');
        showSuccessMessage('Password updated successfully');
        
    } catch (error) {
        showErrorMessage('Failed to update password.');
        console.error('Password update error:', error);
    }
}

// Load user profile data
async function loadUserProfile() {
    try {
        const emailLabel = document.getElementById('email');
        emailLabel.textContent = localStorage.getItem('email') || '';
        
    } catch (error) {
        showErrorMessage('Failed to load profile data');
        console.error('Profile load error:', error);
    }
}

async function updateNameApiCall(params) {
    const jwt = await getJwtOrGoToLoginPage();
    const response = await fetch(`${SECRETS.API_URL}/api/v1/user/update-name`, {
        method: 'PUT',
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

async function changePasswordApiCall(params) {
    const jwt = await getJwtOrGoToLoginPage();
    const response = await fetch(`${SECRETS.API_URL}/api/v1/user/password-reset`, {
        method: 'PUT',
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


// Make functions globally available
window.toggleEditForm = toggleEditForm;
window.cancelEdit = cancelEdit;
window.submitNameChange = submitNameChange;
window.submitPasswordChange = submitPasswordChange;
