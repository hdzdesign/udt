package chc.tfm.udt.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase para mostrar error de falta de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensajeError {

    String error;
}
