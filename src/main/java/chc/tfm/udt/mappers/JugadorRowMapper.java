package chc.tfm.udt.mappers;


import chc.tfm.udt.DTO.Jugador;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JugadorRowMapper  implements RowMapper<Jugador> {

    @Override
    public Jugador mapRow(ResultSet rs, int rowNum) throws SQLException {

        Jugador j = new Jugador();
        j.setId(rs.getLong("id"));
        j.setNombre(rs.getString("nombre"));
        j.setApellido1(rs.getString("apellido1"));
        j.setApellido2(rs.getString("apellido2"));
        j.setMail(rs.getString("mail"));
        j.setInscripcion(rs.getDate("inscripcion"));
        j.setDni(rs.getString("dni"));
        j.setDorsal(rs.getString("dorsal"));
        j.setFoto(rs.getString("foto"));
        j.setEdad(rs.getString("edad"));
        j.setNacimiento(rs.getDate("nacimiento"));
        j.setNacionalidad(rs.getString("nacionalidad"));
        j.setTelefono(rs.getString("telefono"));
        return j;
    }
}
