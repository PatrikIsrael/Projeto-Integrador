package com.example.Projeto.Services;

import com.example.Projeto.Data.Consulta;
import com.example.Projeto.Data.Medico;
import com.example.Projeto.Data.Paciente;
import com.example.Projeto.Data.statusConsulta;
import com.example.Projeto.Exception.ProjetoException;
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

    public Consulta save(Consulta consulta) {
        return consultaRepository.save(consulta);
    }

    public List<Consulta> findByData(LocalDate data) {
        return consultaRepository.findByData(data);
    }

    public List<Consulta> findConsultasByPaciente(Long idPaciente) {
        return consultaRepository.findByPaciente_Id(idPaciente);
    }

    public List<Consulta> findConsultasByMedicoAndData(Long id, LocalDate data) {
        Medico medico = medicoRepository.findById(id).orElse(null);
        if (medico == null) {
            throw new ProjetoException("Médico não encontrado com o ID: " + id);
        }
        return consultaRepository.findByMedicoAndData(medico, data);
    }

    public Consulta marcarConsulta(Consulta consulta) {
        Paciente paciente = consulta.getPaciente();

        // Verificar se o paciente pode marcar mais consultas
        if (!pacienteService.pacientePodeMarcarConsulta(paciente)) {
            throw new ProjetoException("Você já possui 10 consultas marcadas. Não é possível agendar mais consultas.");
        }

        // Verificar se o horário da consulta é válido
        if (!validarHorarioConsulta(consulta.getHorario())) {
            throw new ProjetoException("Horário da consulta inválido. As consultas são permitidas apenas entre 8h e 17h em intervalos de meia hora.");
        }

        // Validar a data da consulta
        if (!validarDataConsulta(consulta.getData())) {
            throw new ProjetoException("Data da consulta inválida. A consulta deve ser marcada com pelo menos 2 dias de antecedência e apenas em dias de semana (segunda a sexta-feira).");
        }

        // Verificar se há conflito de horário
        if (!verificarDisponibilidadeConsulta(consulta)) {
            throw new ProjetoException("Conflito de horário. Já existe uma consulta marcada para este médico neste horário.");
        }

        // Definir o status inicial da consulta como AGENDADA
        consulta.setStatus(statusConsulta.AGENDADA);

        // Salvar a consulta
        return consultaRepository.save(consulta);
    }

    private boolean verificarDisponibilidadeConsulta(Consulta consulta) {
        return !consultaRepository.existsByDataAndHorarioAndMedico(consulta.getData(), consulta.getHorario(), consulta.getMedico());
    }

    public void cancelarConsulta(Long consultaId) {
        Consulta consulta = findById(consultaId);
        if (consulta != null && consulta.getStatus() == statusConsulta.AGENDADA) {
            consulta.setStatus(statusConsulta.CANCELADA);
            save(consulta);
        } else {
            throw new ProjetoException("Consulta não pode ser cancelada. Somente consultas agendadas podem ser canceladas.");
        }
    }

    public Consulta findById(Long consultaId) {
        return consultaRepository.findById(consultaId).orElse(null);
    }

    public boolean existeConsultaNaDataEHora(Consulta consulta) {
        // Verifica se já existe consulta marcada para a mesma data e horário
        return consultaRepository.existsByDataAndHorarioAndMedico(consulta.getData(), consulta.getHorario(), consulta.getMedico());
    }

    public boolean isConsultaAvailable(LocalDate data, LocalTime horario, Long medicoId) {
        Consulta consulta = consultaRepository.findConsultaByDataAndHorarioAndMedico(data, horario, medicoId);
        return consulta == null; // Disponível se não existir consulta
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

    public List<Medico> getAllMedicos() {
        return medicoRepository.findAll();
    }
}
