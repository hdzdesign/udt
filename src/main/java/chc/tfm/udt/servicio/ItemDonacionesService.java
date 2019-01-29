package chc.tfm.udt.servicio;

import chc.tfm.udt.DTO.ItemDonacion;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 *  Esta es la clase servicio de ITEMDONACIÓN , que se encarga de trabajar contra la base de datos y devolver los valores al
 *  controller.
 */
@Log
@Service("ItemDonacionesService")
public class ItemDonacionesService {

    private JdbcTemplate template;

    /**
     * Constructor y inyección de los objetos que vamos a usar en esta clase.
     * @param template Esta es la interfaz encargada de trabajar contra la base de datos
     */
    @Autowired
    public ItemDonacionesService(@Qualifier("JdbcTemplate") JdbcTemplate template) {
        this.template = template;
    }

    /**
     * Controlamos que no llegue ninún dato null.
     * Insertamos los items de las donaciones , recuperando la cantidad y el id del producto , unicamente vamos a
     * trabajar con el id del producto puesto que tenemos una clase sericio para ese crud.
     * @param itemDonacion
     * @param donacionId
     * @return Si esta insertado devolvemos un true
     * @throws IllegalArgumentException
     */
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

    /**
     * Utilizamos la query de SQL para actualizar los items que vienen del controller
     * Unicamente recuperamos la información porque a este metodo se llama desde Donacion , cuando queremos actualizar.
     * @param id
     * @param itemDonacion
     */

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

