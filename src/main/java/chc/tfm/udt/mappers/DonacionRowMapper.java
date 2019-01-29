package chc.tfm.udt.mappers;

import chc.tfm.udt.DTO.Donacion;
import chc.tfm.udt.DTO.Jugador;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que contiene una implementación de la interfaz RowMapper y sobre- escritura del metodo MapRow, en esta clase
 * vamos a llamarla desde el servicio para realizar el Resulset de los campos.
 * ROW MAPPER DE DONACIÓN :
 *   {Creamos los  objetos Donación y Jugador con todas sus propiedades que nos llegan de base d datos , incluido el id del jugador,
 *   Serán asignadas a las propiedades definidas en el DTO para trabajar con ella. }
 */
public class DonacionRowMapper implements RowMapper<Donacion> {
    /**
     * Conseguimos asignar en cada propiedad del objeto Donación los datos que llegan de cada fila de la base de datos.
     * @param rs
     * @param rowNum
     * @return
     * @throws SQLException
     */
    @Override
    public Donacion mapRow(ResultSet rs, int rowNum) throws SQLException {
        Donacion d = new Donacion();
        Jugador j = new Jugador();
        d.setId(rs.getLong("id"));
        d.setDescripcion(rs.getString("descripcion"));
        d.setObservacion(rs.getString("observacion"));
        d.setCreateAt(rs.getDate("create_at"));
        j.setId(rs.getLong("jugador_id"));
        d.setJugador(j);
        return d;
    }
}
