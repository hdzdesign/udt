package chc.tfm.udt.servicio;

import chc.tfm.udt.DTO.Donacion;
import chc.tfm.udt.DTO.Jugador;
import chc.tfm.udt.mappers.JugadorRowMapper;
import com.google.gson.Gson;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  Esta es la clase servicio de Jugador , que se encarga de trabajar contra la base de datos y devolver los valores al
 *  controller.
 */
@Log
@Service(value = "JugadoresService")
public class JugadoresService implements CrudService<Jugador> {

    private CrudService<Donacion> donacionesService;
    private JdbcTemplate jdbcTemplate;

    /**
     * Constructor y inyección de los objetos que vamos a usar en la clase.
     * @param donacionesService
     * @param jdbcTemplate
     */

    public JugadoresService (@Qualifier("DonacionesService") @Lazy CrudService<Donacion> donacionesService,
                             @Qualifier("JdbcTemplate")JdbcTemplate  jdbcTemplate ){
        this.donacionesService = donacionesService;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Usaremos JdbcTemplate  y la Query de SQL para insertar el jugador en la base de datos.
     * Recuperamos los datos del controller y los insertamos con el metodo .update de jdbctemplate.
     * Una vez insertado el jugador vamos a recuperar el ultimo Id  utilizando el Mapper de la clase jugador.
     * Recuperamos la lista que devuelve el Mapper y con el get recuperamos el primer valor de la lista , así podemos
     * trabajar con el para recuperar la lista de donaciones de este jugador y actualizarlas
     * @param jugador
     * @return
     */
    @Transactional
    @Override
    public Jugador createOne(Jugador jugador) {
        log.info("Entramos en el INSERT de JUGADORES");

        Jugador inserted = null;

        String sqlJugador =
            "INSERT INTO jugadores " +
            "(nombre, apellido1, apellido2, mail, inscripcion, dni, dorsal, foto, edad, nacimiento, nacionalidad, telefono)" +
            " VALUES(?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?);";

        this.jdbcTemplate.update(sqlJugador,
                jugador.getNombre(),
                jugador.getApellido1(),
                jugador.getApellido2(),
                jugador.getMail(),
                jugador.getInscripcion(),
                jugador.getDni(),
                jugador.getDorsal(),
                jugador.getFoto(),
                jugador.getEdad(),
                jugador.getNacimiento(),
                jugador.getNacionalidad(),
                jugador.getTelefono());
        log.info("INSERTADO ");
        log.info("RECUPERANDO LA INFO");
        String sqlInserted =
                "SELECT id, nombre, apellido1,apellido2, mail, inscripcion, dni, dorsal, foto, edad, nacimiento, nacionalidad, telefono " +
                "FROM jugadores " +
                "WHERE jugadores.id=LAST_INSERT_ID()";
        List<Jugador> results = this.jdbcTemplate.query(sqlInserted, new JugadorRowMapper());
        if (results.size() > 0) {
            inserted = results.get(0);
            log.info("INSERTADO: " + inserted.toString());
        }

        log.info("JUGADOR CREADO");
        log.info("ACTUALIZANDO DONACIONES DEL JUGADOR");
        List<Donacion> list = jugador.getDonaciones();
        list.forEach(d -> {
            String updateDonacion =
                    "UPDATE donaciones " +
                    "SET jugador_id=LAST_INSERT_ID() " +
                    "WHERE id=?";
            this.jdbcTemplate.update(updateDonacion, d.getId());
        });
        log.info("DONACIONES ACTUALIZADAS");

        log.info("EL JUGADOR SE HA INSERTADO CORRECTAMENTE");

        return inserted;
    }

    /**
     * Utilizaremos JdbcTemplate para realizar la busqueda de un Jugador
     * Utilizamos la clase Mapper de jugador para deolver los resultados al controller
     * @param id la clave única de cada registro
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Jugador findOne(Long id) {

        log.info("JugadoresService - findOne");
        Jugador resultado = null;


        String sql = "SELECT id, apellido1, Apellido2, dni, dorsal, edad," +
                        " foto, inscripcion, mail, nacimiento, nacionalidad, nombre, telefono " +
                    "FROM jugadores J " +
                    "WHERE J.id=?";

        List<Jugador> results = this.jdbcTemplate.query(sql, new Object[]{id}, new JugadorRowMapper());

        if(results.size() > 0){
            resultado = results.get(0);
            log.info("RECUPERADO"  + resultado.toString());
        }
        return resultado;
    }

    /**
     * Utilizaremos JdbcTemplate para realizar la busqueda de una donación asociada a 1 Jugador
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public List<Donacion> findJugadorDonations(Long id){
        log.info("SERVICE -> Busqueda de jugador y sus Donaciones");
        List<Donacion> resultado = null;

        String sqlDonacion =
                "SELECT A.id " +
                "FROM donaciones A " +
                "WHERE A.jugador_id=?";
        RowMapper<Long> idMapper = ((rs, i) -> rs.getLong("id"));

        List<Long> donacionesIds = this.jdbcTemplate.query(sqlDonacion, new Object[]{id}, idMapper);
        log.info("DONACIONES DEL JUGADOR: " + new Gson().toJson(donacionesIds));

        resultado = donacionesIds
                        .stream()
                        .map((donacionId) -> this.donacionesService.findOne(donacionId))
                        .collect(Collectors.toList());

        log.info("RESULTADO DE LA BUSQUEDA " + new Gson().toJson(resultado));
        return resultado;
    }

    @Override
    public Jugador updateOne(Long id, Jugador jugador) {
        log.info("UPDATE -> JUGADOR");
        Jugador resultado = null;

        String sql =    "UPDATE jugadores A " +
                        "SET A.nombre=?, " +
                        "A.apellido1=?, " +
                        "A.apellido2=?, " +
                        "A.dni=?, " +
                        "A.dorsal=?, " +
                        "A.foto=?, " +
                        "A.inscripcion=?, " +
                        "A.mail=?, " +
                        "A.nacimiento=?, " +
                        "A.nacionalidad=?, " +
                        "A.telefono=? " +
                        "WHERE A.id=?";

        int row = jdbcTemplate.update(
                sql,
                jugador.getNombre(),
                jugador.getApellido1(),
                jugador.getApellido2(),
                jugador.getDni(),
                jugador.getDorsal(),
                jugador.getFoto(),
                jugador.getInscripcion(),
                jugador.getMail(),
                jugador.getNacimiento(),
                jugador.getNacionalidad(),
                jugador.getTelefono(),
                id
        );
        log.info("JUGADOR ACTUALIZADO");
        if (row > 0) {
            log.info("RECUPERANDO INFORMACION");
            resultado = this.findOne(id);
        }

        return resultado;
    }

    @Override
    public Boolean deleteOne(Long id) {
        log.info("DELETE -> JUGADOR");

        String sql =    "DELETE " +
                        "FROM jugadores " +
                        "WHERE jugadores.id=?";

        log.info(sql.replace("?", id.toString()));
        int rows = jdbcTemplate.update(sql,id);
        log.info("Jugador borrado correctamente");
        return rows > 0;
    }

    @Override
    public List<Jugador> findAll() {
        log.info("FIND ALL -> ");

        String sql =    "SELECT id, apellido1, Apellido2, dni, dorsal, edad," +
                                " foto, inscripcion, mail, nacimiento, nacionalidad, nombre, telefono " +
                         "FROM jugadores J ";

        List<Jugador> resultados = this.jdbcTemplate.query(sql, new JugadorRowMapper());
        log.info("FIND ALL TERMINADO");
        return resultados;

    }

}
