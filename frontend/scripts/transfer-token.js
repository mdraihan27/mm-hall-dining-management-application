import { getJwtOrGoToLoginPage } from './lib/token.js';
import { showSuccessMessage, showErrorMessage, hideAllMessages } from './lib/messages.js';
import { SECRETS } from '../secrets.js';




document.addEventListener('DOMContentLoaded', async () => {

    getJwtOrGoToLoginPage();

    const transferTokenForm = document.getElementById('transferTokenForm');

    transferTokenForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        hideAllMessages();
        const newOwnerEmail = document.getElementById('recipientStudentId').value.trim();
        const tokenId = document.getElementById('tokenId').value.trim();

        if (!newOwnerEmail || !tokenId) {
            showErrorMessage('Please fill in all fields');
            return;
        }

        const response = await transferTokenApiCall({
            newOwnerEmail,
            tokenId
        });
        console.log('Transfer token response:', response);

        if (!response || !response.success) {
            showErrorMessage(`Failed to transfer token: ${response.message || 'Unknown error'}`);
            
        }else{

            showSuccessMessage(`Token transferred successfully to : ${newOwnerEmail}`);
        }
        
        document.getElementById('recipientStudentId').value = '';
        document.getElementById('tokenId').value = '';
        

    });

});


async function transferTokenApiCall(params) {
    try {
        const jwt = await getJwtOrGoToLoginPage();
        const response = await fetch(`${SECRETS.API_URL}/api/v1/dining-token/transfer`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwt}`
            },
            body: JSON.stringify(params)

        });
        if (!response.ok || !response) {
            const errorData = await response.json();
            const errorText = errorData.message || response.statusText;
            throw new Error(errorText);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        // showErrorMessage(`Error transferring token: ${error.message}`);
        console.error('Error transferring token : ', error);
        return {
            success: false,
            message: error.message || 'An error occurred while transferring the token'
        };
    }
}

