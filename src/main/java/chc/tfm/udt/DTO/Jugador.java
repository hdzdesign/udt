package chc.tfm.udt.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Jugador {

    private Long id;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String edad;
    private String dni;
    private String mail;
    private String telefono;
    private Date nacimiento;
    private String nacionalidad;
    private Date inscripcion;
    private String foto;
    private String dorsal;
    private List<Donacion> donaciones;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
    public Jugador(){}
}
