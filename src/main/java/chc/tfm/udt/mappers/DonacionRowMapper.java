package chc.tfm.udt.mappers;

import chc.tfm.udt.DTO.Donacion;
import chc.tfm.udt.DTO.Jugador;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DonacionRowMapper implements RowMapper<Donacion> {
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
