package chc.tfm.udt.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Template de la clase Donación que usaremos para realizar la transación de sus datos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) //te esconde los campos que son null  guay ;)
public class ItemDonacion extends MensajeError {

    private Long id;

    private Long cantidad;

    private Producto producto;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
