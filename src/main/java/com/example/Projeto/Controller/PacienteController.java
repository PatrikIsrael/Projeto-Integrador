package com.example.Projeto.Controller;

import com.example.Projeto.Data.Consulta;
import com.example.Projeto.Data.Medico;
import com.example.Projeto.Data.Paciente;
import com.example.Projeto.Exception.ProjetoException;
import com.example.Projeto.Services.ConsultaService;
import com.example.Projeto.Services.MedicoService;
import com.example.Projeto.Services.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/paciente")
@SessionAttributes("paciente")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private MedicoService medicoService;

    @GetMapping("/login")
    public String login() {
        return "paciente/paciente_login";
    }

    @GetMapping("/logout")
    public String logout(SessionStatus status, RedirectAttributes redirectAttributes) {
        status.setComplete(); // Finaliza a sessão Spring
        redirectAttributes.addFlashAttribute("successMessage", "Logout efetuado com sucesso!");
        return "redirect:/paciente/login";
    }

    @PostMapping("/login")
    public String doLogin(Model model, @RequestParam String email, @RequestParam String cpf) {
        try {
            Paciente paciente = pacienteService.autenticar(email, cpf);
            if (paciente != null) {
                model.addAttribute("paciente", paciente);
                model.addAttribute("successMessage", "Login efetuado com sucesso!");
                return "redirect:/paciente/dashboard";
            } else {
                model.addAttribute("error", "Email ou CPF inválidos");
                return "paciente/paciente_login";
            }
        } catch (ProjetoException e) {
            model.addAttribute("error", e.getMessage());
            return "paciente/paciente_login";
        }
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "paciente/paciente_cadastro";
    }

    @PostMapping("/cadastro")
    public String doCadastro(Model model, @ModelAttribute @Valid Paciente paciente, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return "paciente/paciente_cadastro";
            }

            Paciente novoPaciente = pacienteService.cadastrar(paciente);
            if (novoPaciente != null) {
                model.addAttribute("successMessage", "Cadastro efetuado com sucesso! Faça o login.");
                return "redirect:/paciente/login";
            } else {
                model.addAttribute("error", "Esse CPF já possui cadastro");
                return "paciente/paciente_cadastro";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "paciente/paciente_cadastro";
        }
    }

    @GetMapping("/dashboard")
    public String pacienteDashboard(Model model, @SessionAttribute("paciente") Paciente paciente) {
        try {
            Consulta consulta = new Consulta();
            model.addAttribute("consulta", consulta);

            List<Consulta> consultasPaciente = consultaService.findConsultasByPaciente(paciente.getId());
            model.addAttribute("consultasPaciente", consultasPaciente);

            List<Medico> medicos = consultaService.getAllMedicos();
            model.addAttribute("medicos", medicos);

            return "paciente/paciente_dashboard";
        } catch (ProjetoException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/paciente/login";
        }
    }
}
