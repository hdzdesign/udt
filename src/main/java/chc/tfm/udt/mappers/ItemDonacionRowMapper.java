package chc.tfm.udt.mappers;

import chc.tfm.udt.DTO.ItemDonacion;
import chc.tfm.udt.DTO.Producto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemDonacionRowMapper implements RowMapper<ItemDonacion> {

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
