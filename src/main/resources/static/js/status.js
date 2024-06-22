document.addEventListener('DOMContentLoaded', () => {
    const consultaElements = document.querySelectorAll('.consulta');

    consultaElements.forEach(element => {
        const status = element.getAttribute('data-status');
        let statusText = '';

        switch (status) {
            case 'AGENDADA':
                statusText = 'Agendada';
                break;
            case 'CANCELADA':
                statusText = 'Cancelada';
                break;
            case 'CONCLUIDA':
                statusText = 'Concluída';
                break;
            default:
                statusText = 'Desconhecido';
        }
        element.querySelector('.status-text').textContent = statusText;
    });
});
