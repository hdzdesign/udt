package chc.tfm.udt.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Template de la clase Donación que usaremos para realizar la transación de sus datos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Producto extends MensajeError {

    private Long id;

    private String nombre;

    private Double precio;

    private Date createAt;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
