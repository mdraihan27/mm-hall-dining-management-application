// Force dark theme and prevent automatic theme switching
document.documentElement.setAttribute('data-theme', 'dark');

// Override any theme switching based on device preference
if (window.matchMedia) {
    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', function() {
        document.documentElement.setAttribute('data-theme', 'dark');
    });
}

// Forgot password functionality
document.addEventListener('DOMContentLoaded', function() {
    const emailForm = document.getElementById('emailForm');
    const passwordForm = document.getElementById('passwordForm');
    const verifyBtn = document.getElementById('verifyBtn');
    const emailSection = document.getElementById('emailSection');
    const passwordSection = document.getElementById('passwordSection');
    const successMessage = document.getElementById('successMessage');
    const errorMessage = document.getElementById('errorMessage');
    
    let verificationSent = false;
    let verificationVerified = false;
    
    // Show message functions
    function showSuccess(message) {
        successMessage.textContent = message;
        successMessage.style.display = 'block';
        errorMessage.style.display = 'none';
        setTimeout(() => {
            successMessage.style.display = 'none';
        }, 5000);
    }
    
    function showError(message) {
        errorMessage.textContent = message;
        errorMessage.style.display = 'block';
        successMessage.style.display = 'none';
        setTimeout(() => {
            errorMessage.style.display = 'none';
        }, 5000);
    }
    
    // Email input change handler
    document.getElementById('email').addEventListener('input', function() {
        verificationSent = false;
        verificationVerified = false;
        verifyBtn.textContent = 'Send Code';
        passwordSection.classList.add('disabled');
    });
    
    // Verify button click handler
    verifyBtn.addEventListener('click', function() {
        const email = document.getElementById('email').value;
        const verificationCode = document.getElementById('verificationCode').value;
        
        if (!email) {
            showError('Please enter your email address');
            return;
        }
        
        // Email validation
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            showError('Please enter a valid email address');
            return;
        }
        
        if (!verificationSent) {
            // Simulate sending verification code
            console.log('Sending verification code to:', email);
            showSuccess('Verification code sent to your email!');
            verifyBtn.textContent = 'Verify Code';
            verificationSent = true;
            return;
        }
        
        if (!verificationCode) {
            showError('Please enter the verification code');
            return;
        }
        
        // Simulate verification (in real app, this would be sent to backend)
        if (verificationCode === '123456' || verificationCode.length === 6) {
            showSuccess('Email verified successfully!');
            verificationVerified = true;
            passwordSection.classList.remove('disabled');
            verifyBtn.textContent = 'Verified ✓';
            verifyBtn.disabled = true;
            verifyBtn.style.background = '#1a4d2e';
            verifyBtn.style.borderColor = '#4caf50';
            verifyBtn.style.color = '#4caf50';
        } else {
            showError('Invalid verification code. Try "123456" for demo.');
        }
    });
    
    // Password form submission
    passwordForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        if (!verificationVerified) {
            showError('Please verify your email first');
            return;
        }
        
        const newPassword = document.getElementById('newPassword').value;
        const confirmNewPassword = document.getElementById('confirmNewPassword').value;
        
        // Validation
        if (!newPassword || !confirmNewPassword) {
            showError('Please fill in all password fields');
            return;
        }
        
        if (newPassword.length < 6) {
            showError('Password should be at least 6 characters long');
            return;
        }
        
        if (newPassword !== confirmNewPassword) {
            showError('Passwords do not match');
            return;
        }
        
        // Simulate password reset
        console.log('Password reset for email:', document.getElementById('email').value);
        showSuccess('Password reset successfully!');
        
        // Redirect to login page after 2 seconds
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 2000);
    });
    
    // Add input focus effects
    const inputs = document.querySelectorAll('input');
    inputs.forEach(input => {
        input.addEventListener('focus', function() {
            this.parentElement.classList.add('focused');
        });
        
        input.addEventListener('blur', function() {
            this.parentElement.classList.remove('focused');
        });
    });
    
    // Real-time password confirmation validation
    const newPassword = document.getElementById('newPassword');
    const confirmNewPassword = document.getElementById('confirmNewPassword');
    
    confirmNewPassword.addEventListener('input', function() {
        if (newPassword.value !== confirmNewPassword.value) {
            confirmNewPassword.style.borderColor = '#ff6b6b';
        } else {
            confirmNewPassword.style.borderColor = '#555';
        }
    });
});
