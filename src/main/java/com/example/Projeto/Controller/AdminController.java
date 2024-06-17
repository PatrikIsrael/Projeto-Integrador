package com.example.Projeto.Controller;

import com.example.Projeto.Data.Medico;
import com.example.Projeto.Services.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin") // Define um prefixo para todos os endpoints deste controlador
public class AdminController {

    private final MedicoService medicoService;

    @Autowired
    public AdminController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping("/login")
    public String login() {
        return "admin/login_admin";
    }

    @PostMapping("/login")
    public String doLogin(Model model, @RequestParam String username, @RequestParam String password) {
        if ("admin".equals(username) && "admin".equals(password)) {
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("error", "Usuário ou senha inválidos");
            return "admin/login_admin";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("medicos", medicoService.listarTodosMedicos());
        return "admin/admin_dashboard";
    }

    @GetMapping("/nova-equipe")
    public String novaEquipe(Model model) {
        model.addAttribute("medico", new Medico());
        return "admin/nova_equipe";
    }

    @PostMapping("/nova-equipe")
    public String adicionarMedico(Model model, @ModelAttribute("medico") Medico medico) {
        if (medicoService.existeMedicoPorCrm(medico.getCrm())) {
            model.addAttribute("error", "Médico com este CRM já cadastrado");
            return "admin/nova_equipe";
        } else {
            medicoService.salvarMedico(medico);
            model.addAttribute("success", "Médico adicionado com sucesso");
            return "redirect:/admin/dashboard";
        }
    }
}
