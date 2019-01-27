package chc.tfm.udt.servicio;

import chc.tfm.udt.DTO.ItemDonacion;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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

    public void updateOne(Long id, ItemDonacion itemDonacion) {
        log.info("UPDATE -> ITEMS");
        ItemDonacion resultado = null;

        String sqlUpdate =      "UPDATE donaciones_items " +
                                "SET cantidad=?, producto_id=? " +
                                "WHERE id=? ";
        int row = template.update(sqlUpdate,
                itemDonacion.getCantidad(),
                itemDonacion.getProducto().getId(),
                id);
        log.info("ITEM ACTUALIZADO");

    }

}

