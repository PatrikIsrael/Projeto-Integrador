package com.example.Projeto.Repository;

import com.example.Projeto.Data.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByEmail(String email);
    Optional<Paciente> findByCpf(String cpf);
    Paciente findByEmailAndCpf(String email, String cpf);
}
