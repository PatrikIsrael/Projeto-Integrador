function updateHiddenDate() {
    const data = document.getElementById('data').value;
    document.getElementById('dataHidden').value = data;
}

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('formAgendamento');
    const descricao = document.getElementById('descricao');
    const data = document.getElementById('data');
    const horario = document.getElementById('horario');
    const medico = document.getElementById('medico');
    const medicoId = document.getElementById('medicoId');

    form.addEventListener('submit', (event) => {
        let valid = true;
        let errorMessage = "";

        // Validação do campo descrição
        if (descricao.value.trim() === "") {
            valid = false;
            errorMessage += "O campo descrição é obrigatório.\n";
        }

        // Validação do campo data
        if (data.value === "") {
            valid = false;
            errorMessage += "O campo data é obrigatório.\n";
        } else {
            const selectedDate = new Date(data.value);
            const today = new Date();
            const twoDaysFromNow = new Date();
            twoDaysFromNow.setDate(today.getDate() + 2);

            if (selectedDate < twoDaysFromNow) {
                valid = false;
                errorMessage += "A data deve ser pelo menos 2 dias no futuro.\n";
            }

            const dayOfWeek = selectedDate.getDay();
            if (dayOfWeek === 0 || dayOfWeek === 6) {
                valid = false;
                errorMessage += "A data deve ser um dia útil (segunda a sexta).\n";
            }
        }

        // Validação do campo horário
        if (horario.value === "") {
            valid = false;
            errorMessage += "O campo horário é obrigatório.\n";
        }

        // Validação do campo médico
        if (medico.value === "") {
            valid = false;
            errorMessage += "O campo médico é obrigatório.\n";
        }

        // Se o formulário não for válido, exibe mensagens de erro e impede o envio
        if (!valid) {
            event.preventDefault();
            alert(errorMessage);
            return;
        }

        // Verificação de disponibilidade da consulta
        event.preventDefault(); // Impede o envio do formulário até a validação ser concluída

        if (data.value && horario.value && medicoId.value) {
            fetch(`/paciente/verificarDisponibilidade?data=${data.value}&horario=${horario.value}&medicoId=${medicoId.value}`)
                .then(response => response.json())
                .then(isAvailable => {
                    if (isAvailable) {
                        form.submit(); // Se disponível, envia o formulário
                    } else {
                        alert("Consulta já agendada para esse horário. Por favor, escolha outro horário.");
                    }
                })
                .catch(error => {
                    console.error('Erro:', error);
                    alert("Erro ao verificar disponibilidade da consulta.");
                });
        } else {
            alert("Preencha todos os campos obrigatórios.");
        }
    });
});
