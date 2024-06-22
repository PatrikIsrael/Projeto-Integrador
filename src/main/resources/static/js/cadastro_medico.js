document.getElementById('medicoForm').addEventListener('submit', function(event) {
    let formIsValid = true;

    // Validação do campo CRM
    const crmField = document.getElementById('crm');
    const crmValue = crmField.value.trim();
    if (!crmValue.match(/^\d+$/)) {
        crmField.classList.add('is-invalid');
        formIsValid = false;
    } else {
        crmField.classList.remove('is-invalid');
    }

    // Validação do campo Nome
    const nomeField = document.getElementById('nome');
    const nomeValue = nomeField.value.trim();
    if (nomeValue === "") {
        nomeField.classList.add('is-invalid');
        formIsValid = false;
    } else {
        nomeField.classList.remove('is-invalid');
    }

    // Validação do campo Especialidade
    const especialidadeField = document.getElementById('especialidade');
    const especialidadeValue = especialidadeField.value.trim();
    if (especialidadeValue === "") {
        especialidadeField.classList.add('is-invalid');
        formIsValid = false;
    } else {
        especialidadeField.classList.remove('is-invalid');
    }

    if (!formIsValid) {
        event.preventDefault(); // Impede o envio do formulário se houver campos inválidos
    }
});