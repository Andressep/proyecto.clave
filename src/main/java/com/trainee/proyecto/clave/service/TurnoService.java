package com.trainee.proyecto.clave.service;

import com.trainee.proyecto.clave.models.Instalacion;
import com.trainee.proyecto.clave.models.Turnos;
import com.trainee.proyecto.clave.repository.ITurnosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class TurnoService {

    @Autowired
    private ITurnosRepository repository;

    public List<Turnos> findAllTurnos() {
        return (List<Turnos>) repository.findAll();
    }

    public Turnos findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Turnos save(Turnos turnos) {
        return repository.save(turnos);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
