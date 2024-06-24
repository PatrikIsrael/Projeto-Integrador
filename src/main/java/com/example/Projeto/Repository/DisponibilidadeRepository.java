package com.example.Projeto.Repository;

import com.example.Projeto.Data.Disponibilidade;
import com.example.Projeto.Data.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface DisponibilidadeRepository extends JpaRepository<Disponibilidade, Long> {
    Disponibilidade findByDataAndHorarioAndMedico(LocalDate data, LocalTime horario, Medico medico);
}
