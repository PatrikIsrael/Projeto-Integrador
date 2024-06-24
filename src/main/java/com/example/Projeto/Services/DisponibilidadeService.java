package com.example.Projeto.Services;

import com.example.Projeto.Data.Disponibilidade;
import com.example.Projeto.Data.Medico;
import com.example.Projeto.Repository.DisponibilidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class DisponibilidadeService {

    @Autowired
    private DisponibilidadeRepository disponibilidadeRepository;

    public void liberarHorario(LocalDate data, LocalTime horario, Medico medico) {
        Disponibilidade disponibilidade = disponibilidadeRepository.findByDataAndHorarioAndMedico(data, horario, medico);
        if (disponibilidade != null) {
            disponibilidade.setDisponivel(true);
            disponibilidadeRepository.save(disponibilidade);
        }
    }
}
