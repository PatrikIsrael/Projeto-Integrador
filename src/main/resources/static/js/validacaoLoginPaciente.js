document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("loginForm").addEventListener("submit", function(event) {
        let email = document.getElementById("email").value;
        let cpf = document.getElementById("cpf").value;
        let errorMessages = document.getElementById("errorMessages");
        errorMessages.innerHTML = "";

        let errors = [];

        // Validar email
        if (email.trim() === "") {
            errors.push("Email é obrigatório.");
        } else if (!validateEmail(email)) {
            errors.push("Email inválido.");
        }

        // Validar CPF
        if (cpf.trim() === "") {
            errors.push("CPF é obrigatório.");
        } else if (!validateCPFLength(cpf)) {
            errors.push("CPF deve conter 11 dígitos.");
        }

        // Mostrar mensagens de erro
        if (errors.length > 0) {
            let errorContainer = document.createElement('div');
            errorContainer.className = 'alert alert-danger';
            errors.forEach(error => {
                let p = document.createElement('p');
                p.textContent = error;
                errorContainer.appendChild(p);
            });
            errorMessages.appendChild(errorContainer);
            event.preventDefault(); // Impede o envio do formulário
        }
    });
});

function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

function validateCPFLength(cpf) {
    cpf = cpf.replace(/[^\d]/g, ''); 
    return cpf.length === 11;
}
