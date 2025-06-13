import {sendEmailVerificationCodeCall} from '../scripts/email-verification.js';
import { showSuccessMessage, showErrorMessage, hideAllMessages } from '../scripts/lib/messages.js';

// Button loading state utility functions
function setButtonLoading(button, isLoading) {
    const btnText = button.querySelector('.btn-text');
    const btnLoading = button.querySelector('.btn-loading');
    
    if (isLoading) {
        button.disabled = true;
        btnText.style.display = 'none';
        btnLoading.style.display = 'flex';
    } else {
        button.disabled = false;
        btnText.style.display = 'block';
        btnLoading.style.display = 'none';
    }
}

// Input validation utilities
function validateEmail(email) {
    const emailRegex = /^[A-Za-z0-9+_.-]+@student\.just\.edu\.bd$/;
    return emailRegex.test(email);
}

function validatePassword(password) {
    return password.length >= 8;
}

// Real-time input validation
function addInputValidation() {
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const nameInput = document.getElementById('name');

    if (emailInput) {
        emailInput.addEventListener('input', function() {
            hideAllMessages();
            const email = this.value.trim();
            if (email && !validateEmail(email)) {
                this.classList.add('error');
            } else {
                this.classList.remove('error');
            }
        });
    }

    if (nameInput) {
        nameInput.addEventListener('input', function() {
            hideAllMessages();
            const name = this.value.trim();
            if (name && name.length < 2) {
                this.classList.add('error');
            } else {
                this.classList.remove('error');
            }
        });
    }

    if (passwordInput) {
        passwordInput.addEventListener('input', function() {
            hideAllMessages();
            const password = this.value;
            if (password && !validatePassword(password)) {
                this.classList.add('error');
            } else {
                this.classList.remove('error');
            }
            
            // Check password match if confirm field has value
            if (confirmPasswordInput.value) {
                checkPasswordMatch();
            }
        });
    }

    if (confirmPasswordInput) {
        confirmPasswordInput.addEventListener('input', checkPasswordMatch);
    }

    function checkPasswordMatch() {
        const password = passwordInput.value;
        const confirmPassword = confirmPasswordInput.value;
        
        if (confirmPassword && password !== confirmPassword) {
            confirmPasswordInput.classList.add('error');
        } else {
            confirmPasswordInput.classList.remove('error');
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    // Initialize input validation
    addInputValidation();

    // Add input focus effects
    const inputs = document.querySelectorAll('input');
    inputs.forEach(input => {
        input.addEventListener('focus', function () {
            this.parentElement.classList.add('focused');
        });

        input.addEventListener('blur', function () {
            this.parentElement.classList.remove('focused');
        });
    });

    const signupForm = document.getElementById('signupForm');

    signupForm.addEventListener('submit', async function (e) {
        e.preventDefault();
        hideAllMessages();

        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const name = document.getElementById('name').value.trim();

        // Basic validation
        if (!email || !password || !confirmPassword || !name) {
            showErrorMessage('Please fill in all fields');
            return;
        }

        if (name.length < 2) {
            showErrorMessage('Name must be at least 2 characters long');
            document.getElementById('name').focus();
            return;
        }

        // Email validation
        if (!validateEmail(email)) {
            showErrorMessage('Please enter a valid email address. You must use your student email (e.g., id.dept@student.just.edu.bd)');
            document.getElementById('email').focus();
            return;
        }

        if (!validatePassword(password)) {
            showErrorMessage('Password must be at least 8 characters long');
            document.getElementById('password').focus();
            return;
        }

        if(password !== confirmPassword) {
            showErrorMessage('Passwords do not match');
            document.getElementById('confirmPassword').focus();
            return;
        }

        const submitBtn = signupForm.querySelector('.btn');
        setButtonLoading(submitBtn, true);        try {
            const result = await signupApiCall({ email, name, password });
            if (result) {
                showSuccessMessage(`Signup successful! Welcome, ${result.userInfo?.name || name}`);
                setTimeout(async () => {
                    try {
                        await sendEmailVerificationCodeCall();
                        window.location.href = '../pages/email-verification.html';
                    } catch (error) {
                        console.error('Error sending verification code:', error);
                        window.location.href = '../pages/email-verification.html';
                    }
                }, 2000);
            }
        } catch (error) {
            showErrorMessage(error.message);
        } finally {
            setButtonLoading(submitBtn, false);
        }
    });
});

async function signupApiCall(params) {
    const response = await fetch('http://localhost:8089/api/v1/auth/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(params)
    });

    if (!response.ok) {
        const errorData = await response.json();
        const errorText = errorData.message || response.statusText;
        throw new Error(errorText);
    }

    const data = await response.json();
    localStorage.setItem('jwt', data.jwt);
    localStorage.setItem('refreshToken', data.refreshToken);
    localStorage.setItem('name', data.userInfo.name);
    localStorage.setItem('email', data.userInfo.email);
    return data;
}
