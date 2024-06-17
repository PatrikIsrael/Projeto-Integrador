package com.example.Projeto.Repository;

import com.example.Projeto.Data.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Paciente findByEmail(String email);
    Paciente findByCpf(String cpf);
}
