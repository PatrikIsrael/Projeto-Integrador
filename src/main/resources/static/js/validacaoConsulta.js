function updateHiddenDate() {
    const data = document.getElementById('data').value;
    document.getElementById('dataHidden').value = data;
}

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('consultaForm');
    const descricao = document.getElementById('descricao');
    const data = document.getElementById('data');
    const horario = document.getElementById('horario');
    const medico = document.getElementById('medico');

    form.addEventListener('submit', (event) => {
        let valid = true;
        let errorMessage = "";

        if (descricao.value.trim() === "") {
            valid = false;
            errorMessage += "O campo descrição é obrigatório.\n";
        }

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

        if (horario.value === "") {
            valid = false;
            errorMessage += "O campo horário é obrigatório.\n";
        }

        if (medico.value === "") {
            valid = false;
            errorMessage += "O campo médico é obrigatório.\n";
        }

        if (!valid) {
            event.preventDefault();
            alert(errorMessage);
        }
    });
});
