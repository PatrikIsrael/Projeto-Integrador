package com.example.Projeto.Repository;

import com.example.Projeto.Data.Consulta;
import com.example.Projeto.Data.Medico;
import com.example.Projeto.Data.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByPaciente(Paciente paciente);

    List<Consulta> findByMedicoAndData(Medico medico, LocalDate data);
}
