package chc.tfm.udt.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

/**
 * Template de la clase Donación que usaremos para realizar la transación de sus datos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Donacion extends MensajeError {

        private Long id;

        private String descripcion;

        private String observacion;

        private Date createAt;

        private Jugador jugador;

        private List<ItemDonacion> items;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
