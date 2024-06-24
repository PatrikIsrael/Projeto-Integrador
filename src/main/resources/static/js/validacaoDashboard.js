document.addEventListener('DOMContentLoaded', function () {
    window.cancelarConsulta = function(consultaId, dataConsulta) {
        const dataConsultaObj = new Date(dataConsulta);
        const dataAtual = new Date();
        const doisDiasAntes = new Date(dataAtual.setDate(dataAtual.getDate() + 2));

        if (dataConsultaObj <= doisDiasAntes) {
            alert("Você só pode cancelar consultas com pelo menos dois dias de antecedência.");
            return;
        }

        if (confirm("Você quer mesmo cancelar sua consulta?")) {
            fetch('/paciente/cancelarConsulta/' + consultaId, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(function (response) {
                if (response.ok) {
                    alert("Consulta cancelada com sucesso.");
                    location.reload(); // Recarrega a página para atualizar a lista de consultas
                } else {
                    response.text().then(function (text) {
                        alert(text); // Mostra a mensagem de erro do backend
                    });
                }
            }).catch(function (error) {
                console.error('Erro:', error);
                alert("Erro ao cancelar a consulta.");
            });
        }
    }
});
