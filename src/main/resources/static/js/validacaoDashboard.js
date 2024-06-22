document.addEventListener('DOMContentLoaded', () => {
    window.cancelarConsulta = function(consultaId) {
        if (confirm("Você quer mesmo cancelar sua consulta?")) {
            fetch(`/paciente/cancelarConsulta/${consultaId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if (response.ok) {
                    alert("Consulta cancelada com sucesso.");
                    location.reload(); // Recarrega a página para atualizar a lista de consultas
                } else {
                    response.text().then(text => {
                        alert(text); // Mostra a mensagem de erro do backend
                    });
                }
            }).catch(error => {
                console.error('Erro:', error);
                alert("Erro ao cancelar a consulta.");
            });
        }
    }
});
