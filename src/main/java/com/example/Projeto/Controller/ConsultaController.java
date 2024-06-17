package com.example.Projeto.Controller;

import com.example.Projeto.Data.Consulta;
import com.example.Projeto.Data.Medico;
import com.example.Projeto.Data.Paciente;
import com.example.Projeto.Services.MedicoService;
import com.example.Projeto.Services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
public class ConsultaController {

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/marcarConsulta")
    public String mostrarFormularioMarcarConsulta(Model model) {
        List<Medico> medicos = medicoService.listarTodosMedicos();
        model.addAttribute("medicos", medicos);
        model.addAttribute("consulta", new Consulta());
        return "paciente/marcar_consulta";
    }

    @PostMapping("/marcarConsulta")
    public String processarFormularioMarcarConsulta(@ModelAttribute Consulta consulta, Model model, @SessionAttribute("paciente") Paciente paciente) {
        consulta.setPaciente(paciente); // Define o paciente na consulta

        // Verifica se o paciente já atingiu o limite de 10 consultas
        List<Consulta> consultasPaciente = pacienteService.listarConsultasPorPaciente(paciente);
        if (consultasPaciente.size() >= 10) {
            model.addAttribute("error", "Você já possui 10 consultas marcadas. Não é possível agendar mais consultas.");
            List<Medico> medicos = medicoService.listarTodosMedicos();
            model.addAttribute("medicos", medicos);
            return "paciente/marcar_consulta";
        }

        // Lógica para verificar conflitos de horário
        if (pacienteService.verificarConflitoConsulta(consulta)) {
            model.addAttribute("error", "Já existe uma consulta marcada para este médico neste horário.");
            List<Medico> medicos = medicoService.listarTodosMedicos();
            model.addAttribute("medicos", medicos);
            return "paciente/marcar_consulta";
        }

        // Caso não haja conflitos, prossegue com o agendamento
        Consulta consultaSalva = pacienteService.marcarConsulta(consulta);
        if (consultaSalva != null) {
            model.addAttribute("successMessage", "Consulta marcada com sucesso!");
            return "redirect:/paciente/dashboard"; // Redireciona para o dashboard após o agendamento
        } else {
            model.addAttribute("error", "Erro ao tentar marcar a consulta. Por favor, tente novamente.");
            List<Medico> medicos = medicoService.listarTodosMedicos();
            model.addAttribute("medicos", medicos);
            return "paciente/marcar_consulta";
        }
    }
}
