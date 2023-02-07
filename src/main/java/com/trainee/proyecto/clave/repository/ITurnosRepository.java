package com.trainee.proyecto.clave.repository;

import com.trainee.proyecto.clave.models.Turnos;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITurnosRepository extends CrudRepository<Turnos, Integer> {
}
