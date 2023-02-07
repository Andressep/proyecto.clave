package com.trainee.proyecto.clave.controller;

import com.trainee.proyecto.clave.models.Instalacion;
import com.trainee.proyecto.clave.models.Turnos;
import com.trainee.proyecto.clave.service.InstalacionService;
import com.trainee.proyecto.clave.service.TurnoService;
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
@RequestMapping("/api/v1/turnos")
public class TurnosController {

    @Autowired
    private TurnoService turnoService;
    @Autowired
    private InstalacionService instalacionService;

    // TODO: READ
    @GetMapping(value = "/")
    @ResponseStatus(HttpStatus.OK)
    public List<Turnos> findAllTurnos() {
        return turnoService.findAllTurnos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        Optional<Turnos> turno = null;
        Map<String, Object> response = new HashMap<>();
        try {
            turno = Optional.ofNullable(turnoService.findById(id));
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al buscar el dato solicitado en la base de datos.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (turno == null) {
            response.put("error", "El cliente con el ID: ".concat(id.toString().concat(" no existe en la base de datos.")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        response.put("turno", turno);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // TODO: CREATE
    /*
    * {
    *   "instalacion": "Nombre de la instalacion",
    *   "horario": "Horario en formato String",
    *   "pago": 42,
    *   "cancelado": true,
    *   "fecha": 01-02-2023,
    *   "instalacion_id":
    *       {
    *           "instalacion": "Nombre de la instalacion para crear un filtro",
    *           "descripcion": "Descripcion del comprobante"
    *       }
    * }
    *
    * */
    @PostMapping("/")
    public ResponseEntity<?> create(@Valid @RequestBody Turnos turnos, BindingResult result) {
        Optional<Instalacion> instalacion = Optional.ofNullable(instalacionService.findById(turnos.getInstalacionId().getId()));
        Map<String, Object> response = new HashMap<>();
        Turnos turnoNuevo = null;

        if(!instalacion.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if( result.hasErrors() ) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo " + err.getField() + " " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            turnos.setInstalacionId(instalacion.get());
            turnoNuevo = turnoService.save(turnos);
        } catch( DataAccessException e ) {
            response.put("mensaje", "Error al realizar insert en la base de datos.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.put("mensaje", "Producto creado con exito");
        response.put("producto", turnoNuevo);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    //TODO: UPDATE
    @PutMapping("{id}")
    public ResponseEntity<?> Update(@Valid @RequestBody Turnos turnos, BindingResult result, @PathVariable("id") Integer id) {
        Turnos turnoActual = turnoService.findById(id);
        Turnos turnoNuevo = null;
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map( err -> "El campo: "+ err.getField() + " " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if( turnoActual == null ) {
            response.put("mensaje", "El turno con ID: ".concat(id.toString().concat(" no existe en la base de datos.")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        try {
            turnoActual.setInstalacion(turnos.getInstalacion());
            turnoActual.setHorario(turnos.getHorario());
            turnoActual.setPago(turnos.getPago());
            turnoActual.setCancelado(turnos.isCancelado());
            turnoActual.setFecha(turnos.getFecha());
            turnoActual.setComprobante(turnos.getComprobante());
            turnoActual.setInstalacionId(turnos.getInstalacionId());

            turnoNuevo = turnoService.save(turnoActual);
        } catch ( DataAccessException e ) {
            response.put("mensaje", "Error al realizar insert en la base de datos.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje","Turno modificado con exito.");
        response.put("turno", turnoNuevo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //TODO: DELETE
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            turnoService.deleteById(id);
        } catch ( DataAccessException e ) {
            response.put("mensaje", "Error al intentar eliminar el turno.");
            response.put("errors", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "Turno eliminado con exito.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
