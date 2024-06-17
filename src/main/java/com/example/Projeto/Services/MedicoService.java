package com.example.Projeto.Services;

import com.example.Projeto.Data.Medico;
import com.example.Projeto.Repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;

    @Autowired
    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    public List<Medico> listarTodosMedicos() {
        return medicoRepository.findAll();
    }

    public Optional<Medico> buscarMedicoPorId(Long id) {
        return medicoRepository.findById(id);
    }

    public Medico salvarMedico(Medico medico) {
        return medicoRepository.save(medico);
    }

    public void deletarMedicoPorId(Long id) {
        medicoRepository.deleteById(id);
    }

    public boolean existeMedicoPorCrm(String crm) {
        return medicoRepository.existsByCrm(crm);
    }
}
