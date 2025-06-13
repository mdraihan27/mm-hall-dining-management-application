
export function showMessage(type, text, duration = 5000) {
    hideAllMessages();
    
    const messageElement = document.getElementById(`${type}Message`);
    if (messageElement) {
        messageElement.textContent = text;
        messageElement.style.display = 'block';
        
        if (duration > 0) {
            setTimeout(() => {
                hideMessage(type);
            }, duration);
        }
    }
}

export function hideMessage(type) {
    const messageElement = document.getElementById(`${type}Message`);
    if (messageElement) {
        messageElement.style.display = 'none';
    }
}

export function hideAllMessages() {
    const messageTypes = ['success', 'error', 'info', 'warning'];
    messageTypes.forEach(type => hideMessage(type));
}

export function showSuccessMessage(text, duration = 5000) {
    showMessage('success', text, duration);
}

export function showErrorMessage(text, duration = 5000) {
    showMessage('error', text, duration);
}

export function showInfoMessage(text, duration = 5000) {
    showMessage('info', text, duration);
}

export function showWarningMessage(text, duration = 5000) {
    showMessage('warning', text, duration);
}
