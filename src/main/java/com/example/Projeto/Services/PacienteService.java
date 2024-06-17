package com.example.Projeto.Services;

import com.example.Projeto.Data.Consulta;
import com.example.Projeto.Data.Paciente;
import com.example.Projeto.Repository.ConsultaRepository;
import com.example.Projeto.Repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        // Busca todas as consultas do médico para a data da consulta
        List<Consulta> consultasNoMesmoHorario = consultaRepository.findByMedicoAndData(
                consulta.getMedico(), consulta.getData());

        // Verifica se já existe uma consulta do mesmo médico na mesma data
        for (Consulta c : consultasNoMesmoHorario) {
            if (c.getData().equals(consulta.getData())) {
                return true; // Conflito encontrado
            }
        }
        return false; // Nenhum conflito encontrado
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
}
