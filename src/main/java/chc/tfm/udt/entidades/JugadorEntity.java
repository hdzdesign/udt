package chc.tfm.udt.entidades;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "jugadores")
public class JugadorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(name = "dni")
    private String dni;

    @NotEmpty
    @Column(name = "nombre")
    private String nombre;

    @NotEmpty
    @Column(name = "apellido1")
    private String apellido1;

    @NotEmpty
    @Column(name = "apellido2")
    private String apellido2;

    @NotEmpty
    @Column(name = "telefono")
    private String telefono;

    @NotEmpty
    @Email
    @Pattern(regexp = ".+@.+\\..+", message = "Formato invalido.")
    @Column(name = "mail")
    private String mail;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "nacimiento")
    @Temporal(TemporalType.DATE)
    private Date nacimiento;

    @NotEmpty
    @Column(name = "edad")
    private String edad;

    @NotEmpty
    @Column(name = "nacionalidad")
    private String nacionalidad;

    //Anotación para transformar la fecha para base de datos.
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "inscripcion")
    @Temporal(TemporalType.DATE)
    private Date inscripcion;

    @NotEmpty
    @Column(name = "dorsal")
    private String dorsal;

    @Column(name = "foto")
    private String foto;

    /**
     * Un jugador muchas donaciones
     * FetchTypy.LAZY atributo perezoso Cada vez que se haga 1 petición de 1 jugador no se hara la petición a las donaciones si no se expresa.
     * CascadeType.All: Con esto conseguimos que si borramos 1 jugador se borren también sus donaciones.
     * mappedBy: Mapea las tablas en ambos sentidos creando las llaves foraneas en ambas tablas
     */

   // @OneToMany(targetEntity = DonacionEntity.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//(mappedBy = "jugadorEntity",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(mappedBy = "jugadorEntity",
            fetch = FetchType.LAZY,
            orphanRemoval = true)
   // @JoinColumn(name = "donacion_id", referencedColumnName = "id")
    private List<DonacionEntity> donaciones;

    public void addDonacion(DonacionEntity donacionEntity){
        donaciones.add(donacionEntity);
    }

    public JugadorEntity(){
        this.donaciones = new ArrayList<>();
    }


    /**
     * Metodo que se invoca justo antes de hacer la inserción en base de datos para generar la fecha.
     */
    @PrePersist
    public void prePersist(){
        inscripcion = new Date();
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
