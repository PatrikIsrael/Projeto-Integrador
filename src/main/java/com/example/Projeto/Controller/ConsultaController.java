package com.example.Projeto.Controller;

import com.example.Projeto.Data.Consulta;
import com.example.Projeto.Data.Medico;
import com.example.Projeto.Data.Paciente;
import com.example.Projeto.Data.statusConsulta;
import com.example.Projeto.Services.ConsultaService;
import com.example.Projeto.Services.DisponibilidadeService;
import com.example.Projeto.Services.MedicoService;
import com.example.Projeto.Services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private DisponibilidadeService disponibilidadeService;

    @Autowired
    private ConsultaService consultaRepository;

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
    public ResponseEntity<String> cancelarConsulta(@PathVariable Long consultaId) {
        try {
            Consulta consulta = consultaService.findById(consultaId);
            if (consulta == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consulta não encontrada.");
            }

            consultaService.cancelarConsulta(consultaId);

            // Liberar o horário
            disponibilidadeService.liberarHorario(consulta.getData(), consulta.getHorario(), consulta.getMedico());

            return ResponseEntity.ok("Consulta cancelada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cancelar a consulta.");
        }
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
        if (consultaService.existeConsultaNaDataEHora((consulta))){
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
            consultaSalva.setStatus(statusConsulta.AGENDADA);
            consultaService.save(consultaSalva); // Salva a consulta com o status AGENDADA atualizado
            model.addAttribute("successMessage", "Consulta marcada com sucesso!");
            return "redirect:/paciente/dashboard";
        } else {
            model.addAttribute("error", "Erro ao tentar marcar a consulta. Por favor, tente novamente.");
            model.addAttribute("medicos", medicos);
            return "paciente/marcar_consulta";
        }
    }

    @GetMapping("/verificarDisponibilidade")
    @ResponseBody
    public boolean verificarDisponibilidade(@RequestParam LocalDate data, @RequestParam LocalTime horario, @RequestParam Long medicoId) {
        return consultaService.isConsultaAvailable(data, horario, medicoId);
    }

}
