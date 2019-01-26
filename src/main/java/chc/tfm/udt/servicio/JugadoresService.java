package chc.tfm.udt.servicio;

import chc.tfm.udt.DTO.Donacion;
import chc.tfm.udt.DTO.Jugador;
import chc.tfm.udt.entidades.DonacionEntity;
import chc.tfm.udt.entidades.JugadorEntity;
import chc.tfm.udt.convertidores.JugadorConverter;
import chc.tfm.udt.mappers.JugadorRowMapper;
import chc.tfm.udt.repositorios.JugadorRepository;
import com.google.gson.Gson;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log
@Service(value = "JugadoresService")
public class JugadoresService implements CrudService<Jugador> {

    private JugadorRepository jugadorRepository;
    private CrudService<Donacion> donacionesService;
    private JugadorConverter converter;
    private JdbcTemplate jdbcTemplate;



    public JugadoresService (@Qualifier("JugadorRepository") JugadorRepository jugadorRepository,
                             @Qualifier("DonacionesService") @Lazy CrudService<Donacion> donacionesService,
                             @Qualifier("JugadorConverter") JugadorConverter converter,
                             @Qualifier("JdbcTemplate")JdbcTemplate  jdbcTemplate ){
        this.jugadorRepository = jugadorRepository;
        this.donacionesService = donacionesService;
        this.converter = converter;
        this.jdbcTemplate = jdbcTemplate;
    }

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

    @Transactional(readOnly = true)
    public List<Donacion> findJugadorDonations(Long id){
        log.info("SERVICE -> Busqueda de jugador y sus Donaciones");
        List<Donacion> resultado = null;

        String sqlDonacion =
                "SELECT DISTINCT A.id " +
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
        Jugador resultado = null;

//        Optional<JugadorEntity> buscar = jugadorRepository.findById(id);
//        if(buscar.isPresent()){
//            JugadorEntity encontrado = buscar.get();
//
//            // setear atributos en el entity encontrado
//            encontrado.setNombre(jugador.getNombre());
//            encontrado.setApellido1(jugador.getApellido1());
//            encontrado.setApellido2(jugador.getApellido2());
//            encontrado.setEdad(jugador.getEdad());
//            encontrado.setNacionalidad(jugador.getNacionalidad());
//            encontrado.setDni(jugador.getDni());
//            encontrado.setMail(jugador.getMail());
//            encontrado.setTelefono(jugador.getTelefono());
//            encontrado.setDorsal(jugador.getDorsal());
//            encontrado.setInscripcion(jugador.getInscripcion());
//            encontrado.setFoto(jugador.getFoto());
//
//            // encontrar las donaciones del jugador
//            List<DonacionEntity> donacionEntities = findDonacionesFromJugador(jugador);
//
//           encontrado.setDonaciones(donacionEntities);
//
//            // guardar cambios
//            JugadorEntity guardado = jugadorRepository.save(encontrado);
//            resultado = converter.convertToEntityAttribute(guardado);
//        }
        return resultado;
    }

    @Override
    public Boolean deleteOne(Long id) {
        if(jugadorRepository.findById(id).isPresent()){
            jugadorRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Jugador> findAll() {
        List<Jugador> resultado = jugadorRepository.findAll().
                stream().
                map(entity -> converter.convertToEntityAttribute(entity)).
                collect(Collectors.toList());
        return resultado;
    }

}
