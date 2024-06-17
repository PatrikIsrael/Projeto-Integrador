package com.example.Projeto.Repository;

import com.example.Projeto.Data.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    boolean existsByCrm(String crm);
}
