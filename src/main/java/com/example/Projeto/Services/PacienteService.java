package com.example.Projeto.Services;

import com.example.Projeto.Data.Consulta;
import com.example.Projeto.Data.Paciente;
import com.example.Projeto.Repository.ConsultaRepository;
import com.example.Projeto.Repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    public Paciente autenticar(String email, String cpf) {
        Paciente paciente = pacienteRepository.findByEmail(email);
        if (paciente != null && paciente.getCpf().equals(cpf)) {
            return paciente;
        }
        return null;
    }

    public Paciente cadastrar(Paciente paciente) {
        if (pacienteRepository.findByCpf(paciente.getCpf()) == null &&
                pacienteRepository.findByEmail(paciente.getEmail()) == null) {
            return pacienteRepository.save(paciente);
        }
        return null;
    }

    public List<Consulta> obterConsultas(Paciente paciente) {
        return consultaRepository.findByPaciente(paciente);
    }

    public boolean verificarConflitoConsulta(Consulta consulta) {
        // Verifica se já existe uma consulta do mesmo médico na mesma data e horário
        return consultaRepository.existsByMedicoAndDataAndHorario(consulta.getMedico(), consulta.getData(), consulta.getHorario());
    }

    public Consulta marcarConsulta(Consulta consulta) {
        return consultaRepository.save(consulta);
    }

    public List<Consulta> listarConsultasPorPaciente(Paciente paciente) {
        return consultaRepository.findByPaciente(paciente);
    }

    public int contarConsultasMarcadasPorPaciente(Paciente paciente) {
        List<Consulta> consultasPaciente = consultaRepository.findByPaciente(paciente);
        return consultasPaciente.size();
    }

    public boolean pacientePodeMarcarConsulta(Paciente paciente) {
        // Verificar se o paciente já atingiu o limite de 10 consultas
        return contarConsultasMarcadasPorPaciente(paciente) < 10;
    }

    public boolean validarHorarioConsulta(LocalTime horario) {
        // Verificar se o horário está dentro do intervalo permitido (8h às 17h) e em intervalos de 30 minutos
        return !horario.isBefore(LocalTime.of(8, 0)) &&
                !horario.isAfter(LocalTime.of(17, 0)) &&
                horario.getMinute() % 30 == 0;
    }
}
