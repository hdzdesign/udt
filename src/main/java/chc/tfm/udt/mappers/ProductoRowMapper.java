package chc.tfm.udt.mappers;

import chc.tfm.udt.DTO.Producto;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoRowMapper implements RowMapper<Producto> {
    @Override
    public Producto mapRow(ResultSet rs, int rowNum) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getLong("id"));
        p.setPrecio(rs.getDouble("precio"));
        p.setNombre(rs.getString("nombre"));
        p.setCreateAt(rs.getDate("create_at"));
        return p;
    }
}
