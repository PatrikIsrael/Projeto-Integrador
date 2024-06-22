document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('pacienteForm');
    form.addEventListener('submit', function (event) {
        let isValid = true;
        let errorMessage = '';

        // Nome validation
        const nome = document.getElementById('nome').value.trim();
        if (nome === '') {
            isValid = false;
            errorMessage += 'O campo nome é obrigatório.\n';
        }

        // CPF validation
        const cpf = document.getElementById('cpf').value.trim();
        const cpfPattern = /^\d{11}$/;
        if (!cpfPattern.test(cpf)) {
            isValid = false;
            errorMessage += 'O CPF deve conter 11 dígitos.\n';
        }

        // Email validation
        const email = document.getElementById('email').value.trim();
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(email)) {
            isValid = false;
            errorMessage += 'O email não é válido.\n';
        }

        // Telefone validation
        const telefone = document.getElementById('telefone').value.trim();
        const telefonePattern = /^\d{10,11}$/;
        if (telefone !== '' && !telefonePattern.test(telefone)) {
            isValid = false;
            errorMessage += 'O telefone deve conter 10 ou 11 dígitos.\n';
        }

        // Data de Nascimento validacao
        const dataNascimentoField = document.getElementById('dataNascimento');
        const dataNascimentoValue = dataNascimentoField.value;
        if (dataNascimentoValue === '') {
            isValid = false;
            errorMessage += 'O campo data de nascimento é obrigatório.\n';
        } else {
            // Formata a data de yyyy-MM-dd para dd/MM/yyyy
            const [year, month, day] = dataNascimentoValue.split('-');
            const formattedDate = `${day}/${month}/${year}`;
            dataNascimentoField.value = formattedDate;
        }


        if (!isValid) {
            event.preventDefault();
            const errorElement = document.getElementById('error');
            errorElement.textContent = errorMessage;
        }
    });
});
