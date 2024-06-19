package com.example.Projeto.Controller;

import com.example.Projeto.Data.Consulta;
import com.example.Projeto.Data.Medico;
import com.example.Projeto.Data.Paciente;
import com.example.Projeto.Services.ConsultaService;
import com.example.Projeto.Services.MedicoService;
import com.example.Projeto.Services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/paciente")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/marcarConsulta")
    public String mostrarFormularioMarcarConsulta(Model model, @SessionAttribute("paciente") Paciente paciente) {
        if (!pacienteService.pacientePodeMarcarConsulta(paciente)) {
            model.addAttribute("error", "Você já possui 10 consultas marcadas. Não é possível agendar mais consultas.");
            return "redirect:/paciente/dashboard";
        }

        List<Medico> medicos = medicoService.listarTodosMedicos();
        model.addAttribute("medicos", medicos);
        model.addAttribute("consulta", new Consulta());
        return "paciente/marcar_consulta";
    }

    @PostMapping("/marcarConsulta")
    public String processarFormularioMarcarConsulta(@ModelAttribute Consulta consulta, Model model, @SessionAttribute("paciente") Paciente paciente) {
        consulta.setPaciente(paciente);

        // Verificar se o paciente pode marcar mais consultas
        if (!pacienteService.pacientePodeMarcarConsulta(paciente)) {
            model.addAttribute("error", "Você já possui 10 consultas marcadas. Não é possível agendar mais consultas.");
            return "redirect:/paciente/dashboard";
        }

        // Verificar conflitos de horário
        if (pacienteService.verificarConflitoConsulta(consulta)) {
            model.addAttribute("error", "Já existe uma consulta marcada para este médico neste horário.");
            List<Medico> medicos = medicoService.listarTodosMedicos();
            model.addAttribute("medicos", medicos);
            return "paciente/marcar_consulta";
        }

        // Verificar se o horário está dentro do intervalo permitido (8h às 17h)
        if (!pacienteService.validarHorarioConsulta(consulta.getHorario())) {
            model.addAttribute("error", "Você só pode marcar consultas entre 8h e 17h em intervalos de meia hora.");
            List<Medico> medicos = medicoService.listarTodosMedicos();
            model.addAttribute("medicos", medicos);
            return "paciente/marcar_consulta";
        }

        // Salvar a consulta
        Consulta consultaSalva = consultaService.save(consulta);
        if (consultaSalva != null) {
            model.addAttribute("successMessage", "Consulta marcada com sucesso!");
            return "redirect:/paciente/dashboard";
        } else {
            model.addAttribute("error", "Erro ao tentar marcar a consulta. Por favor, tente novamente.");
            List<Medico> medicos = medicoService.listarTodosMedicos();
            model.addAttribute("medicos", medicos);
            return "paciente/marcar_consulta";
        }
    }
}
