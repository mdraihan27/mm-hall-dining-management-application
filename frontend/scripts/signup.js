// Force dark theme and prevent automatic theme switching
document.documentElement.setAttribute('data-theme', 'dark');

// Override any theme switching based on device preference
if (window.matchMedia) {
    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', function () {
        document.documentElement.setAttribute('data-theme', 'dark');
    });
}

// Login form functionality

const signupForm = document.getElementById('loginForm');

loginForm.addEventListener('submit', async function (e) {
    e.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const name = document.getElementById('name').value;


    // Basic validation
    if (!email || !password || !confirmPassword || name) {
        alert('Please fill in all fields');
        return;
    }
    // Email validation
    const emailRegex = /^[A-Za-z0-9+_.-]+@student\.just\.edu\.bd$/;
    if (!emailRegex.test(email)) {
        alert('Please enter a valid email address. You must use your student email (e.g., id.dept@student.just.edu.bd)');
        return;
    }

    if(password === confirmPassword) {
        alert('Passwords do not match');
        return;
    }

    // Simulate login process
    console.log('signup attempt:', { email, password });

    try {
        const result = await loginApiCall({ email, password });
        if (result) {
            document.getElementById('successMessage').style.display = 'block';
            document.getElementById('successMessage').textContent = `Login successful! Welcome, ${result.name || 'User'}`;
            setTimeout(() => {
                window.location.href = '../pages/home.html';
            }, 2000);
        }
    } catch (error) {
        document.getElementById('errorMessage').style.display = 'block';
        document.getElementById('errorMessage').textContent = error.message;
    }
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
