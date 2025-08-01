import { getJwtOrGoToLoginPage } from './lib/token.js';
import { showSuccessMessage, showErrorMessage, hideAllMessages } from './lib/messages.js';
// main.js
import { SECRETS } from '../secrets.js';





document.addEventListener('DOMContentLoaded', async () => {

    getJwtOrGoToLoginPage();

    const amount = document.getElementById('amount');
    const bkash = document.getElementById('bkash');
    const submitButton = document.getElementById('submit-button');

    submitButton.addEventListener('click', async (e) => {
        e.preventDefault(); // Prevent form submission and page refresh
        
        try {
            const addBalanceApiCallResponse = await addBalanceApiCall({ amount: amount.value });
            
            // Debug: Log the response to see its structure
            console.log('API Response:', addBalanceApiCallResponse);
            
            if (!addBalanceApiCallResponse || !addBalanceApiCallResponse.success) {
                showErrorMessage('Failed to add balance. Please try again.');
            } else {
                amount.value = '';
                bkash.value = '';
                showSuccessMessage('Balance added successfully.');
            }
        } catch (error) {
            console.error('Add balance error:', error);
            showErrorMessage('Failed to add balance. Please try again.');
        }
    })




});

async function addBalanceApiCall(params) {
    const jwt = await getJwtOrGoToLoginPage();
    const response = await fetch(`${SECRETS.API_URL}/api/v1/user/balance/topup?amount=${params.amount}`, {
        method: 'POST',
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

