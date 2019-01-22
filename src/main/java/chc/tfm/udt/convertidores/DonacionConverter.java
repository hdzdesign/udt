package chc.tfm.udt.convertidores;

import chc.tfm.udt.DTO.Donacion;
import chc.tfm.udt.DTO.ItemDonacion;
import chc.tfm.udt.DTO.Jugador;
import chc.tfm.udt.entidades.DonacionEntity;
import chc.tfm.udt.entidades.ItemDonacionEntity;
import chc.tfm.udt.entidades.JugadorEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Converter
@Component("DonacionConverter")
public class DonacionConverter implements AttributeConverter<Donacion, DonacionEntity> {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("ItemConverter")
    private ItemConverter itemConverter;

    @Autowired
    @Qualifier("JugadorConverter")
    private JugadorConverter jugadorConverter;

    //Convertir de DTO a ENTITY
    @Override
    public DonacionEntity convertToDatabaseColumn(Donacion attribute) {
        DonacionEntity e = new DonacionEntity();
        LOG.info("Creamos el objeto DonacionEntity");
        e.setId(attribute.getId());
        e.setObservacion(attribute.getObservacion());
        e.setDescripcion(attribute.getDescripcion());
        e.setCreateAt(attribute.getCreateAt());
        // Set de lista de items de la factura
        LOG.info("Porque no seteas");

        List<ItemDonacion> items = attribute.getItems();
        e.setItems(items != null
                        ? items.
                            stream().
                            map(dto -> itemConverter.convertToDatabaseColumn(dto)).
                            collect(Collectors.toList())
                        : new ArrayList<>());
        LOG.info("No ha seteado");
        // Set del jugador
        Jugador jugador = attribute.getJugador();
        e.setJugadorEntity(jugador != null ?
                jugadorConverter.convertToDatabaseColumn(jugador)
                : new JugadorEntity());
        LOG.info("Se ha seteado bien el entity");
        LOG.info(e.toString());
        return e;
    }

    //Convertir de entity a dto
    @Override
    public Donacion convertToEntityAttribute(DonacionEntity dbData) {
        Donacion d = new Donacion();
        LOG.info("Creamos el Donacion DTO");
        d.setDescripcion(dbData.getDescripcion());
        d.setObservacion(dbData.getObservacion());
        d.setCreateAt(dbData.getCreateAt());
        d.setId(dbData.getId());
        // Seteo de la lista de items de la factura
       List<ItemDonacionEntity> itemDonaciones = dbData.getItems();
        d.setItems(itemDonaciones != null
                ? itemDonaciones
                    .stream()
                    .map(item -> itemConverter.convertToEntityAttribute(item))
                    .collect(Collectors.toList())
                : new ArrayList<>());
        // Seteamos al jugador lo que viene de la entity
        JugadorEntity jugadorEntity = dbData.getJugadorEntity();
        d.setJugador(jugadorEntity != null ?
                jugadorConverter.convertToEntityAttribute(jugadorEntity)
                : new Jugador());
        LOG.info("Se ha seteado bien el dto");
        LOG.info(d.toString());

        return d;
    }
}
