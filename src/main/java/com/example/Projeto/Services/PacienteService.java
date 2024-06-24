package com.example.Projeto.Services;

import com.example.Projeto.Data.Consulta;
import com.example.Projeto.Data.Paciente;
import com.example.Projeto.Repository.ConsultaRepository;
import com.example.Projeto.Repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    private static final int MAX_CONSULTAS_POR_PACIENTE = 10;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    public Paciente autenticar(String email, String cpf) {
        return pacienteRepository.findByEmailAndCpf(email, cpf);
    }

    public Paciente cadastrar(Paciente paciente) throws Exception {
        // Verifica se já existe um paciente com o mesmo CPF ou e-mail
        if (pacienteRepository.findByCpf(paciente.getCpf()).isPresent()) {
            throw new Exception("Este CPF já está cadastrado.");
        }

        if (pacienteRepository.findByEmail(paciente.getEmail()).isPresent()) {
            throw new Exception("Este e-mail já está cadastrado.");
        }

        return pacienteRepository.save(paciente);
    }

    public List<Consulta> obterConsultas(Paciente paciente) {
        return consultaRepository.findByPacienteOrderByDataAscHorarioAsc(paciente);
    }

    public int contarConsultasMarcadasPorPaciente(Paciente paciente) {
        return consultaRepository.countByPaciente(paciente);
    }

    public boolean pacientePodeMarcarConsulta(Paciente paciente) {
        return contarConsultasMarcadasPorPaciente(paciente) < MAX_CONSULTAS_POR_PACIENTE;
    }
}
