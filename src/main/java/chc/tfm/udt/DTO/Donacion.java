package chc.tfm.udt.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public List<ItemDonacion> getItems() {
        return items;
    }

    public void setItems(List<ItemDonacion> items) {
        this.items = items;
    }
}
