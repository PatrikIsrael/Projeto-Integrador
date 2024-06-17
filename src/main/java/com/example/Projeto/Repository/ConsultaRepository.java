package com.example.Projeto.Repository;

import com.example.Projeto.Data.Consulta;
import com.example.Projeto.Data.Medico;
import com.example.Projeto.Data.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByPaciente(Paciente paciente);

    List<Consulta> findByMedicoAndData(Medico medico, LocalDate data);

    List<Consulta> findByData(LocalDate data);

    List<Consulta> findByDataAndHorario(LocalDate data, LocalTime horario); // Atualizado para usar LocalTime

    List<Consulta> findByOrderByDataAscHorarioAsc(); // Ordenação por data e horário

    List<Consulta> findByPaciente_Id(Long idPaciente);
}
