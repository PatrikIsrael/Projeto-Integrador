// Função para exibir mensagem de sucesso
function showSuccessMessage(message) {
    const alertDiv = document.createElement('div');
    alertDiv.classList.add('alert', 'alert-success', 'mt-3');
    alertDiv.textContent = message;

    const container = document.querySelector('.container');
    container.insertBefore(alertDiv, container.firstChild);

    // Remover a mensagem após 5 segundos
    setTimeout(() => {
        alertDiv.remove();
    }, 5000);
}

// Função para exibir mensagem de erro
function showErrorMessage(message) {
    const alertDiv = document.createElement('div');
    alertDiv.classList.add('alert', 'alert-danger', 'mt-3');
    alertDiv.textContent = message;

    const container = document.querySelector('.container');
    container.insertBefore(alertDiv, container.firstChild);

    // Remover a mensagem após 5 segundos
    setTimeout(() => {
        alertDiv.remove();
    }, 5000);
}
