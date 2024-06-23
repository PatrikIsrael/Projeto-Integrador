package com.example.Projeto.Repository;

import com.example.Projeto.Data.Consulta;
import com.example.Projeto.Data.Medico;
import com.example.Projeto.Data.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    // Retorna todas as consultas de um paciente específico ordenadas por data e horário
    List<Consulta> findByPacienteOrderByDataAscHorarioAsc(Paciente paciente);

    // Retorna todas as consultas de um médico específico em uma data específica
    List<Consulta> findByMedicoAndData(Medico medico, LocalDate data);

    // Retorna a consulta em uma data e horário específicos (deve retornar no máximo uma única consulta)
    Consulta findByDataAndHorario(LocalDate data, LocalTime horario);

    // Retorna todas as consultas em uma data específica
    List<Consulta> findByData(LocalDate data);

    // Retorna todas as consultas ordenadas por data e horário
    List<Consulta> findByOrderByDataAscHorarioAsc();

    // Verifica se já existe uma consulta para um médico, data e horário específicos
    boolean existsByMedicoAndDataAndHorario(Medico medico, LocalDate data, LocalTime horario);

    // Conta quantas consultas um paciente específico possui
    int countByPaciente(Paciente paciente);

    List<Consulta> findByPaciente_Id(Long id);


}
