package com.example.Projeto.Services;

import com.example.Projeto.Data.Consulta;
import com.example.Projeto.Data.Medico;
import com.example.Projeto.Data.Paciente;
import com.example.Projeto.Data.statusConsulta;
import com.example.Projeto.Repository.ConsultaRepository;
import com.example.Projeto.Repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteService pacienteService;

    public List<Consulta> findAll() {
        return consultaRepository.findAll();
    }

    public Consulta findById(Long id) {
        return consultaRepository.findById(id).orElse(null);
    }

    public Consulta save(Consulta consulta) {
        return consultaRepository.save(consulta);
    }

    public void delete(Long id) {
        consultaRepository.deleteById(id);
    }

    public List<Consulta> findByData(LocalDate data) {
        return consultaRepository.findByData(data);
    }

    public boolean verificarDisponibilidadeConsulta(Consulta consulta) {
        return !consultaRepository.existsByMedicoAndDataAndHorario(consulta.getMedico(), consulta.getData(), consulta.getHorario());
    }

    public List<Consulta> findConsultasByPaciente(Long idPaciente) {
        return consultaRepository.findByPaciente_Id(idPaciente);
    }

    public List<Medico> getAllMedicos() {
        return medicoRepository.findAll();
    }

    public boolean validarHorarioConsulta(LocalTime horario) {
        LocalTime inicio = LocalTime.of(8, 0);
        LocalTime fim = LocalTime.of(17, 0);
        return (horario.isAfter(inicio.minusMinutes(1)) && horario.isBefore(fim.plusMinutes(1)))
                && (horario.getMinute() == 0 || horario.getMinute() == 30);
    }

    public boolean validarDataConsulta(LocalDate data) {
        if (data == null) {
            return false;
        }
        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.plusDays(2);
        return data.isAfter(dataLimite) && !(data.getDayOfWeek().getValue() >= 6);
    }

    public List<Consulta> findConsultasByMedicoAndData(Long id, LocalDate data) {
        Medico medico = medicoRepository.findById(id).orElse(null);
        if (medico == null) {
            return List.of();
        }
        return consultaRepository.findByMedicoAndData(medico, data);
    }

    public Consulta marcarConsulta(Consulta consulta) {
        Paciente paciente = consulta.getPaciente();

        // Verificar se o paciente pode marcar mais consultas
        if (!pacienteService.pacientePodeMarcarConsulta(paciente)) {
            throw new IllegalArgumentException("Você já possui 10 consultas marcadas. Não é possível agendar mais consultas.");
        }

        // Verificar se o horário da consulta é válido
        if (!validarHorarioConsulta(consulta.getHorario())) {
            throw new IllegalArgumentException("Horário da consulta inválido. As consultas são permitidas apenas entre 8h e 17h em intervalos de meia hora.");
        }

        // Validar a data da consulta
        if (!validarDataConsulta(consulta.getData())) {
            throw new IllegalArgumentException("Data da consulta inválida. A consulta deve ser marcada com pelo menos 2 dias de antecedência e apenas em dias de semana (segunda a sexta-feira).");
        }

        // Verificar se há conflito de horário
        if (!verificarDisponibilidadeConsulta(consulta)) {
            throw new IllegalArgumentException("Conflito de horário. Já existe uma consulta marcada para este médico neste horário.");
        }

        // Definir o status inicial da consulta como AGENDADA
        consulta.setStatus(statusConsulta.AGENDADA);

        // Salvar a consulta
        return consultaRepository.save(consulta);
    }
}
