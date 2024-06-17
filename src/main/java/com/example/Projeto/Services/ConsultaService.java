package com.example.Projeto.Services;

import com.example.Projeto.Data.Consulta;
import com.example.Projeto.Data.Medico;
import com.example.Projeto.Repository.ConsultaRepository;
import com.example.Projeto.Repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository; // Se você tiver um repositório para Medico

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
        // Verifica se já existe uma consulta marcada para a mesma data e horário
        List<Consulta> consultas = consultaRepository.findByDataAndHorario(consulta.getData(), consulta.getHorario());
        return consultas.isEmpty(); // Retorna true se não há consultas marcadas para o mesmo horário
    }

    public List<Consulta> findConsultasByPaciente(Long idPaciente) {
        return consultaRepository.findByPaciente_Id(idPaciente);
    }

    public List<Medico> getAllMedicos() {
        return medicoRepository.findAll(); // Supondo que MedicoRepository tenha método findAll para buscar todos os médicos
    }
}
