package chc.tfm.udt.DTO;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class Donacion {

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
    public Donacion(){}
}
