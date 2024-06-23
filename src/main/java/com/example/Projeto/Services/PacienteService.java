package com.example.Projeto.Services;

import com.example.Projeto.Data.Consulta;
import com.example.Projeto.Data.Paciente;
import com.example.Projeto.Repository.ConsultaRepository;
import com.example.Projeto.Repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class PacienteService {

    private static final int MAX_CONSULTAS_POR_PACIENTE = 10;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    public Paciente autenticar(String email, String cpf) {
        return pacienteRepository.findByEmailAndCpf(email, cpf);
    }


    public Paciente cadastrar(Paciente paciente) {
        // Verifica se já existe um paciente com o mesmo CPF ou e-mail
        if (pacienteRepository.findByCpf(paciente.getCpf()) != null || pacienteRepository.findByEmail(paciente.getEmail()) != null) {
            return null; // Já existe um paciente com o mesmo CPF ou e-mail
        }
        return pacienteRepository.save(paciente);
    }

    public List<Consulta> obterConsultas(Paciente paciente) {
        return consultaRepository.findByPacienteOrderByDataAscHorarioAsc(paciente);
    }


    public Consulta marcarConsulta(Consulta consulta) {
        // Verifica se o paciente pode marcar uma nova consulta
        if (!pacientePodeMarcarConsulta(consulta.getPaciente())) {
            throw new IllegalArgumentException("O paciente já possui 10 consultas marcadas.");
        }

        // Verifica se o horário da consulta é válido
        if (!validarHorarioConsulta(consulta.getHorario())) {
            throw new IllegalArgumentException("O horário da consulta não é válido.");
        }

        // Verifica se há conflito de horário
        if (verificarConflitoConsulta(consulta)) {
            throw new IllegalArgumentException("Há um conflito de horário com outra consulta.");
        }

        return consultaRepository.save(consulta);
    }

    public List<Consulta> listarConsultasPorPaciente(Paciente paciente) {
        return consultaRepository.findByPacienteOrderByDataAscHorarioAsc(paciente);
    }

    public int contarConsultasMarcadasPorPaciente(Paciente paciente) {
        return consultaRepository.countByPaciente(paciente);
    }

    public boolean pacientePodeMarcarConsulta(Paciente paciente) {
        return contarConsultasMarcadasPorPaciente(paciente) < MAX_CONSULTAS_POR_PACIENTE;
    }

    public boolean validarHorarioConsulta(LocalTime horario) {
        LocalTime inicio = LocalTime.of(8, 0); // 08:00
        LocalTime fim = LocalTime.of(17, 0);   // 17:00
        return !horario.isBefore(inicio) && !horario.isAfter(fim) && horario.getMinute() % 30 == 0;
    }

    public boolean validarDataConsulta(LocalDate dataConsulta) {
        // Verificar se a data da consulta é de segunda a sexta-feira
        if (dataConsulta.getDayOfWeek().getValue() >= 6) { // 6 representa sábado e 7 domingo
            return false;
        }

        // Verificar se a data da consulta é pelo menos 2 dias no futuro
        LocalDate hoje = LocalDate.now();
        LocalDate dataMinima = hoje.plusDays(2);
        return dataConsulta.isEqual(dataMinima) || dataConsulta.isAfter(dataMinima);
    }

    public boolean verificarConflitoConsulta(Consulta consulta) {
        // Busca todas as consultas do médico para a data da consulta
        List<Consulta> consultasNoMesmoHorario = consultaRepository.findByMedicoAndData(
                consulta.getMedico(), consulta.getData());

        // Verifica se já existe uma consulta do mesmo médico no mesmo horário
        for (Consulta c : consultasNoMesmoHorario) {
            if (c.getHorario().equals(consulta.getHorario())) {
                return true; // Conflito encontrado
            }
        }
        return false; // Nenhum conflito encontrado
    }
}
