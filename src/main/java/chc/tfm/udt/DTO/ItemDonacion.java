package chc.tfm.udt.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDonacion {

    private Long id;

    private Long cantidad;

    private Producto producto;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
    public ItemDonacion(){}
}
