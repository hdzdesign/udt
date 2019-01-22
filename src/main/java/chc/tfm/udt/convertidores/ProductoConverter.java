package chc.tfm.udt.convertidores;

import chc.tfm.udt.DTO.Producto;
import chc.tfm.udt.entidades.ProductoEntity;
import org.springframework.stereotype.Component;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
@Component("ProductoConverter")
public class ProductoConverter implements AttributeConverter<Producto, ProductoEntity> {

    //Conversor de producto a productoEntity
    @Override
    public ProductoEntity convertToDatabaseColumn(Producto attribute) {
        ProductoEntity p = new ProductoEntity();
        p.setId(attribute.getId());
        p.setCreateAt(attribute.getCreateAt());
        p.setNombre(attribute.getNombre());
        p.setPrecio(attribute.getPrecio());
        return p;
    }
    //Conversor de ProductoEntity a producto.
    @Override
    public Producto convertToEntityAttribute(ProductoEntity dbData) {
        Producto dto = new Producto();
        dto.setId(dbData.getId());
        dto.setCreateAt(dbData.getCreateAt());
        dto.setNombre(dbData.getNombre());
        dto.setPrecio(dbData.getPrecio());
        return dto;
    }
}
