package chc.tfm.udt.mappers;

import chc.tfm.udt.DTO.ItemDonacion;
import chc.tfm.udt.DTO.Producto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que contiene una implementación de la interfaz RowMapper y sobre-escritura del metodo MapRow, en esta clase
 * vamos a llamarla desde el servicio para realizar el Resulset de los campos.
 *
 * ROW MAPPER DE ItemDonacion :
 *   {Creamos el objeto ItemDonación y Producto con todas sus propiedades que nos llegan de base d datos,
 *   Serán asignadas a las propiedades definidas en el DTO para trabajar con ella. }
 *   Esta clase esta relacionada con el producto.
 */
public class ItemDonacionRowMapper implements RowMapper<ItemDonacion> {
    /**
     * Conseguimos asignar en cada propiedad del objeto ItemDonación los datos que llegan de cada fila de la base de datos.
     * @param rs
     * @param rowNum
     * @return
     * @throws SQLException
     */
    @Override
    public ItemDonacion mapRow(ResultSet rs, int rowNum) throws SQLException {
        ItemDonacion item = new ItemDonacion();
        Producto p = new Producto();
        item.setId(rs.getLong("item_id"));
        item.setCantidad(rs.getLong("cantidad"));
        p.setId(rs.getLong("producto_id"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecio(rs.getDouble("precio"));
        p.setCreateAt(rs.getDate("create_at"));

        item.setProducto(p);
        return item;
    }
}
