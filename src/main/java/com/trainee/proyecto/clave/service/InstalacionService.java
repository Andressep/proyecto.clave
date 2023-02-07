package com.trainee.proyecto.clave.service;

import com.trainee.proyecto.clave.models.Instalacion;
import com.trainee.proyecto.clave.repository.IInstalacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class InstalacionService {

    @Autowired
    private IInstalacionRepository repository;

    public List<Instalacion> findAllComprobante() {
        return (List<Instalacion>) repository.findAll();
    }

    public Instalacion findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Instalacion save(Instalacion comprobante) {
        return repository.save(comprobante);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
