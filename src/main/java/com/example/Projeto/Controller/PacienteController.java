package com.example.Projeto.Controller;

import com.example.Projeto.Data.Consulta;
import com.example.Projeto.Data.Paciente;
import com.example.Projeto.Services.ConsultaService;
import com.example.Projeto.Services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/paciente")
@SessionAttributes("paciente")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private ConsultaService consultaService;

    @GetMapping("/login")
    public String login() {
        return "paciente/paciente_login";
    }

    @PostMapping("/login")
    public String doLogin(Model model, @RequestParam String email, @RequestParam String cpf) {
        Paciente paciente = pacienteService.autenticar(email, cpf);
        if (paciente != null) {
            model.addAttribute("paciente", paciente);
            model.addAttribute("successMessage", "Login efetuado com sucesso!");
            return "redirect:/paciente/dashboard";
        } else {
            model.addAttribute("error", "Email ou CPF inválidos");
            return "paciente/paciente_login";
        }
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "paciente/paciente_cadastro";
    }

    @PostMapping("/cadastro")
    public String doCadastro(Model model, @ModelAttribute Paciente paciente) {
        Paciente novoPaciente = pacienteService.cadastrar(paciente);
        if (novoPaciente != null) {
            model.addAttribute("successMessage", "Cadastro efetuado com sucesso! Faça o login.");
            return "redirect:/paciente/login";
        } else {
            model.addAttribute("error", "Esse CPF já possui cadastro");
            return "paciente/paciente_cadastro";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @SessionAttribute("paciente") Paciente paciente) {
        model.addAttribute("paciente", paciente);
        List<Consulta> consultasPaciente = pacienteService.listarConsultasPorPaciente(paciente);
        model.addAttribute("consultasPaciente", consultasPaciente);
        return "paciente/paciente_dashboard";
    }


}
