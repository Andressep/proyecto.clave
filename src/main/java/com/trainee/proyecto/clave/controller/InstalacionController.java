package com.trainee.proyecto.clave.controller;

import com.trainee.proyecto.clave.models.Instalacion;
import com.trainee.proyecto.clave.service.InstalacionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/instalaciones")
public class InstalacionController {

    @Autowired
    private InstalacionService service;

    //TODO: READ
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Instalacion> findAll() {
        return service.findAllComprobante();
    }

    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        Instalacion instalacion = null;
        Map<String, Object> response = new HashMap<>();
        try {
            instalacion = service.findById(id);
        } catch( DataAccessException e ) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if( instalacion == null) {
            response.put("error", "El cliente con el ID: ".concat(id.toString().concat(" no existe en la base de datos.")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(instalacion, HttpStatus.OK);
    }

    //TODO: CREATE
    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody Instalacion instalacion, BindingResult result) {
        Instalacion newInstalacion = null;
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo " + err.getField() + " " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("Errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            newInstalacion = service.save(instalacion);
        } catch ( DataAccessException e ) {
            response.put("mensaje", "Error al intentar crear el comprobante.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "Se ha creado el comprobante con exito.");
        response.put("comprobante", newInstalacion);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //TODO: UPDATE
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Instalacion instalacion,BindingResult result, @PathVariable Integer id) {
        Instalacion instalacionActual = service.findById(id);
        Instalacion instalacionUpdated = null;

        Map<String, Object> response = new HashMap<>();

        //TODO: Hacer el manejo de errores.
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(
                            err -> "El campo " + err.getField() + " " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if ( instalacionActual == null) {
            response.put("mensaje", "Error: no se pudo editar, la instalacion ID: ".concat(id.toString().concat(" no existe en la base de datos.")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        try {
            instalacionActual.setNombre(instalacion.getNombre());
            instalacionActual.setDescripcion(instalacion.getDescripcion());

            instalacionUpdated = service.save(instalacionActual);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar la base de datos.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "La instalacion ha sido actualizado con exito");
        response.put("instalacion", instalacionUpdated);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
