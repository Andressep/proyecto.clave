package com.trainee.proyecto.clave.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "turnos")
@NoArgsConstructor
public class Turnos implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;
    private String instalacion;
    @NotEmpty( message = "El horario del turno debe de ser asignado." )
    private String horario;
    @NotNull( message = "Debe ingresar el pago acordado por el turno." )
    private int pago;
    @NotNull( message = "Especifique si fue pagado o no." )
    private boolean cancelado;
    @NotNull( message = "Asigne una fecha al turno por favor." )
    @DateTimeFormat( pattern = "dd-mm-yyyy")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    private String comprobante;
    @OneToOne( cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn( name = "instalacion_id", referencedColumnName = "id")
    private Instalacion instalacionId;


    public Turnos(String instalacion, String horario, int pago, boolean cancelado, Date fecha, String comprobante, Instalacion instalacionId) {
        this.instalacion = instalacion;
        this.horario = horario;
        this.pago = pago;
        this.cancelado = cancelado;
        this.fecha = fecha;
        this.comprobante = comprobante;
        this.instalacionId = instalacionId;
    }
}
