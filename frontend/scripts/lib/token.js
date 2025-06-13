const checkJwtValidity = (jwt) => {
    if (!jwt) {
        return false;
    }

    const parts = jwt.split('.');
    if (parts.length !== 3) {
        return false;
    }

    try {
        const payload = JSON.parse(atob(parts[1]));
        // Check if the token has expired
        if (payload.exp && Date.now() >= payload.exp * 1000) {
            return false;
        }
        return true;
    } catch (e) {
        console.error('Invalid JWT structure:', e);
        return false;
    }
};

const refreshJwt = async () => {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
        throw new Error('Refresh token is required');
    }

    try {
        const response = await fetch(`http://localhost:8089/api/v1/auth/refresh?refreshToken=${refreshToken}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (!response.ok) {
            throw new Error('Failed to refresh token');
        }

        const data = await response.json();
        localStorage.setItem('jwt', data.jwt);
        return data.jwt; 
    } catch (error) {
        console.error('Error refreshing JWT:', error);
        throw error;
    }
};

export const getJwtOrGoToLoginPage = async () => {
    let jwt = localStorage.getItem('jwt');
    if (!jwt) {
        window.location.href = '../pages/login.html';
        return null;
    }
    
    if (!checkJwtValidity(jwt)) {
        const refreshToken = localStorage.getItem('refreshToken');
        if (!refreshToken || !checkJwtValidity(refreshToken)) {
            window.location.href = '../pages/login.html';
            return null;
        }
        
        try {
            const refreshedJwt = await refreshJwt();
            if (!refreshedJwt) {
                window.location.href = '../pages/login.html';
                return null;
            }
            jwt = refreshedJwt;
            localStorage.setItem('jwt', jwt);
        } catch (error) {
            console.error('Error refreshing token:', error);
            window.location.href = '../pages/login.html';
            return null;
        }
    }
    return jwt;
}
