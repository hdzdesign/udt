package chc.tfm.udt.convertidores;

import chc.tfm.udt.DTO.ItemDonacion;
import chc.tfm.udt.DTO.Producto;
import chc.tfm.udt.entidades.ItemDonacionEntity;
import chc.tfm.udt.entidades.ProductoEntity;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@NoArgsConstructor
@Converter
@Component("ItemConverter")
public class ItemConverter implements AttributeConverter<ItemDonacion, ItemDonacionEntity> {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("ProductoConverter")
    private ProductoConverter productoConverter;

    @Override
    public ItemDonacionEntity convertToDatabaseColumn(ItemDonacion attribute) {
        ItemDonacionEntity e = new ItemDonacionEntity();

        e.setId(attribute.getId());
        e.setCantidad(attribute.getCantidad());
        e.setProductoEntity(productoConverter.convertToDatabaseColumn(attribute.getProducto()));
        LOG.info("Se ha convertido bien el deteo");
        LOG.info(e.toString());
        return e;
    }

    @Override
    public ItemDonacion convertToEntityAttribute(ItemDonacionEntity dbData) {
        ItemDonacion d = new ItemDonacion();
        d.setId(dbData.getId());
        d.setCantidad(dbData.getCantidad());
        d.setProducto(productoConverter.convertToEntityAttribute(dbData.getProductoEntity()));
        LOG.info("Se ha convertido bien a entity");
        LOG.info(d.toString());
        return d;
    }
}
