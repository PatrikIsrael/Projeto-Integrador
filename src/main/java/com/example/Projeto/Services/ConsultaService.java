package com.example.Projeto.Services;


import com.example.Projeto.Data.Consulta;
import com.example.Projeto.Repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    public List<Consulta> findAll() {
        return consultaRepository.findAll();
    }

    public Consulta save(Consulta consulta) {
        return consultaRepository.save(consulta);
    }

    public void delete(Long id) {
        consultaRepository.deleteById(id);
    }


}
