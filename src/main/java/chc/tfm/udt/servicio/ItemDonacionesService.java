package chc.tfm.udt.servicio;

import chc.tfm.udt.DTO.ItemDonacion;
import chc.tfm.udt.DTO.Producto;
import chc.tfm.udt.entidades.ItemDonacionEntity;
import chc.tfm.udt.convertidores.ItemConverter;
import chc.tfm.udt.repositorios.ItemRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Log
@Service("ItemDonacionesService")
public class ItemDonacionesService {

    private JdbcTemplate template;

    @Autowired
    public ItemDonacionesService(@Qualifier("JdbcTemplate") JdbcTemplate template) {
        this.template = template;
    }

    public boolean createOne(ItemDonacion itemDonacion, Long donacionId) throws IllegalArgumentException {
        log.info("ITEM SERVICE - CREATE ONE");
        Assert.notNull(itemDonacion, "Item donacion no puede ser null");
        Assert.notNull(itemDonacion.getProducto(), "Producto no puede ser null");
        Assert.notNull(itemDonacion.getProducto().getId(), "Id del producto no puede ser null");
        Assert.notNull(donacionId, "Id de la donacion no puede ser null");
        log.info("COMPROBACIONES DE ITEM OKAY");

        String sql =
                "INSERT INTO donaciones_items" +
                "(cantidad, producto_id, donacion_id)" +
                "VALUES ( ?, ?, ?)";

        int rows = this.template.update(
                sql,
                itemDonacion.getCantidad(),
                itemDonacion.getProducto().getId(),
                donacionId);
        log.info("ITEM AÑADIDO");
        return rows > 0;
    }

    public ItemDonacion findOne(Long id) {
//        ItemDonacion result = null;
//        if (id != null) {
//            Optional<ItemDonacionEntity> found = repository.findById(id);
//            result = found.isPresent() ? converter.convertToEntityAttribute(found.get()) : null;
//        }
//        return result;
        return null;
    }

    public ItemDonacion updateOne(Long id, ItemDonacion itemDonacion) {
//        ItemDonacion result = null;
//        Optional<ItemDonacionEntity> found = repository.findById(id);
//        if (found.isPresent()) {
//            ItemDonacionEntity i = found.get();
//            i.setCantidad(itemDonacion.getCantidad());
//            ItemDonacionEntity saved = repository.save(i);
//            result = converter.convertToEntityAttribute(saved);
//        }
//        return result;
        return null;
    }

    public Boolean deleteOne(Long id) {
//        boolean deleted = false;
//        Optional<ItemDonacionEntity> found = repository.findById(id);
//        if (found.isPresent()) {
//            repository.delete(found.get());
//            deleted = true;
//        }
//        return deleted;
        return null;
    }

    public List<ItemDonacion> findAll() {
//        return repository.findAll()
//                .stream()
//                .map(entity ->
//                        converter.convertToEntityAttribute(entity))
//                .collect(Collectors.toList());
        return null;
    }
}

