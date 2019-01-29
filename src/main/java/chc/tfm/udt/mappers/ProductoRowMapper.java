package chc.tfm.udt.mappers;

import chc.tfm.udt.DTO.Producto;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que contiene una implementación de la interfaz RowMapper y sobre- escritura del metodo MapRow, en esta clase
 * vamos a llamarla desde el servicio para realizar el Resulset de los campos.
 * ROW MAPPER DE DONACIÓN :
 *   {Creamos el objeto Producto y todas sus propiedades que nos llegan de base d datos , incluido el id del jugador,
 *   Serán asignadas a las propiedades definidas en el DTO para trabajar con ella. }
 */
public class ProductoRowMapper implements RowMapper<Producto> {
    /**
     * Conseguimos asignar en cada propiedad del objeto Producto los datos que llegan de cada fila de la base de datos.
     * @param rs
     * @param rowNum
     * @return
     * @throws SQLException
     */
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
