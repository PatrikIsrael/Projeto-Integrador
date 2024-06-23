package com.example.Projeto.Controller;

import com.example.Projeto.Data.Consulta;
import com.example.Projeto.Data.Medico;
import com.example.Projeto.Data.Paciente;
import com.example.Projeto.Data.statusConsulta;
import com.example.Projeto.Services.ConsultaService;
import com.example.Projeto.Services.MedicoService;
import com.example.Projeto.Services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @DeleteMapping("/cancelarConsulta/{consultaId}")
    public ResponseEntity<?> cancelarConsulta(@PathVariable Long consultaId) {
        Consulta consulta = consultaService.findById(consultaId);

        if (consulta == null) {
            return ResponseEntity.notFound().build();
        }

        LocalDate dataConsulta = consulta.getData();
        LocalDate hoje = LocalDate.now();

        if (hoje.plusDays(2).compareTo(dataConsulta) >= 0) {
            return ResponseEntity.badRequest().body("Não é possível cancelar a consulta com menos de 2 dias de antecedência.");
        }

        consulta.setStatus(statusConsulta.CANCELADA);
        consultaService.save(consulta);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/marcarConsulta")
    public String processarFormularioMarcarConsulta(@ModelAttribute Consulta consulta, Model model, @SessionAttribute("paciente") Paciente paciente) {
        consulta.setPaciente(paciente);
        List<Medico> medicos = medicoService.listarTodosMedicos();

        // Verificar se a data não é nula
        if (consulta.getData() == null) {
            model.addAttribute("error", "A data da consulta é obrigatória.");
            model.addAttribute("medicos", medicos);
            return "paciente/marcar_consulta";
        }

        // Verificar se o paciente pode marcar mais consultas
        if (!pacienteService.pacientePodeMarcarConsulta(paciente)) {
            model.addAttribute("error", "Você já possui 10 consultas marcadas. Não é possível agendar mais consultas.");
            model.addAttribute("medicos", medicos);
            return "paciente/marcar_consulta";
        }

        // Verificar conflitos de horário
        if (!consultaService.verificarDisponibilidadeConsulta(consulta)) {
            model.addAttribute("error", "Já existe uma consulta marcada para este médico neste horário.");
            model.addAttribute("medicos", medicos);
            return "paciente/marcar_consulta";
        }

        // Verificar se o horário está dentro do intervalo permitido (8h às 17h)
        LocalTime horario = consulta.getHorario();
        if (!consultaService.validarHorarioConsulta(horario)) {
            model.addAttribute("error", "Você só pode marcar consultas entre 8h e 17h em intervalos de meia hora.");
            model.addAttribute("medicos", medicos);
            return "paciente/marcar_consulta";
        }

        // Verificar se a data está pelo menos 2 dias no futuro e se é um dia de semana
        if (!consultaService.validarDataConsulta(consulta.getData())) {
            model.addAttribute("error", "As consultas só podem ser marcadas com pelo menos 2 dias de antecedência e apenas em dias de semana (segunda a sexta).");
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
            model.addAttribute("medicos", medicos);
            return "paciente/marcar_consulta";
        }
    }
}
