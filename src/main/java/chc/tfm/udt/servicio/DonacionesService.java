package chc.tfm.udt.servicio;

import chc.tfm.udt.DTO.Donacion;
import chc.tfm.udt.DTO.ItemDonacion;
import chc.tfm.udt.mappers.DonacionRowMapper;
import chc.tfm.udt.mappers.ItemDonacionRowMapper;
import com.google.gson.Gson;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Esta es la clase servicio de DONACIÓN , que se encarga de trabajar contra la base de datos y devolver los valores al
 * controller.
 */
@Log
@Service(value = "DonacionesService")
public class DonacionesService implements CrudService<Donacion> {

    private ItemDonacionesService itemDonacionesService;
    private JdbcTemplate template;

    /**
     * Constructor y inyección de los objetos que vamos a usar en esta clase.
     * @param itemDonacionesService EL servicio de los items.
     * @param template
     */

    @Autowired
    public DonacionesService(@Qualifier("ItemDonacionesService") ItemDonacionesService itemDonacionesService,
            @Qualifier("JdbcTemplate") @Lazy JdbcTemplate template) {
        this.itemDonacionesService = itemDonacionesService;
        this.template = template;
    }

    /**
     * Usaremos JdbcTemplate para insertar en base de datos utilizando una Query SQL.
     * Recuperamos los valores que nos llegan por parametros del objeto Donación y los insertamos con el metodo
     * update de la interfaz jdbcTemplate.
     * Recuperamos el registro que acabamos de insertar en base de datos con el proposito de asignarle las propiedades,
     * de los objetos que tiene asociados.
     * Utilizaremos la interfaz RowMapper<Long> para recuperar una lista con el id, unicamente recuperara el ultimo
     * objeto insertado , por  eso sabemos que la primera posición de la lista tiene que encontrarse el que necesitamos.
     * Hacemos una llamada a la  propiedad de donacion List<Itmes> y la recorremos para asignarle cada linea de esta lista
     * haciendo la llamada a la clase ItemDonación con su metodo de crear, por eso la importancia de hacer 1 servicio para
     * independiente , para respetar el Principio de responsabilidad única , y que el código sea mas legible.
     * una vez insertado hacemos 1 busqueda por id y lo retornamos al controller para mostrarlo al usuario.
     * @param donacion
     * @return
     * @throws IllegalArgumentException
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Donacion createOne(Donacion donacion) throws IllegalArgumentException {
        log.info("DONACION -> SERVICE");
        Assert.notNull(donacion, "Donacion no puede ser null");
        Assert.notNull(donacion.getItems(), "Donacion necesita tener items");
        Assert.notNull(donacion.getJugador().getId(), "Id del jugador no puede ser null");
        String sql =
                "INSERT INTO donaciones" +
                "(create_at, descripcion, observacion, jugador_id)" +
                "VALUES (NOW(), ?, ?, ?)";
        this.template.update(
                sql,
                donacion.getDescripcion(),
                donacion.getObservacion(),
                donacion.getJugador().getId()
        );
        log.info("INSERTADO");
        log.info("RECUPERAMOS EL ID DE LA DONACION INSERTADA");
        Donacion inserted = null;
        //SQL que nos recupera el último registro de la base de datos.
        String sqlInserted =
                "SELECT id " +
                "FROM donaciones " +
                "WHERE donaciones.id=LAST_INSERT_ID()";
        RowMapper<Long> idMapper = ((rs, i) -> rs.getLong("id"));
        List<Long> ids = this.template.query(sqlInserted, idMapper);
        if (ids.size() > 0) {
            log.info("INSERTANDO ITEMS");
            donacion.getItems().forEach(item -> {
                try {
                    this.itemDonacionesService.createOne(item, ids.get(0));
                }catch (Exception e) {
                    log.severe(e.toString());
                    log.severe(e.getMessage());
                }
            });
            log.info("ITEMS INSERTADOS");
            log.info("RECUPERANDO TODA LA INFORMACION DE LA DONACION");
            inserted = this.findOne(ids.get(0));
            log.info("INSERTADA: " + inserted.toString());
        }
        log.info("DONACIÓN CREADA");
        return inserted;
    }

    /**
     * En este metodo utilizaremos JdbcTemplate para realizar la busqueda de una unica donación y sus items que contien
     * los productos.
     * Creamos 1 objeto Donación  que será lo que vamos a devolver a la vista ya con todos los datos cargados de base de datos.
     * Utilizaremos una Query Sql para hacer la busqueda de la donación en concreto.
     * Una vez realizada la busqueda entra en acción la clase DonacionRowMapper, al cual le vamso a pasar con el objeto
     * template.query la sql un array de objetos con el paremetro id y creamos la isntancia de la clase mapper para hacer el
     * mapeo de los campos, una vez recuperada la donación la seteamos en un objeto donación y procedemos a la busqueda de sus items.
     * Realizamos la SQL para recuperar los items asociados a la donación y con la clase MApper de Items , mapeamos el objeto y
     * se lo asignamos en la propiedad de donacion list items.
     *
     * @param id la clave única de cada registro
     * @return Objeto cargado de datos de la base de datos y con una lista de los items asociados a ella.
     */
    @Override
    @Transactional(readOnly = true)
    public Donacion findOne(Long id) {
        log.info("DONACIONES - FIND ONE");
        Donacion donacion = new Donacion();
        String sqlDonacion =
                "SELECT A.id, A.descripcion, A.observacion, A.create_at, A.jugador_id " +
                "FROM donaciones A " +
                "WHERE A.id=?";
        List<Donacion> resultado = this.template.query(sqlDonacion, new Object[]{id}, new DonacionRowMapper());
        log.info("ENCONTRADOS: " + new Gson().toJson(resultado));
        if (resultado.size() > 0) {
            donacion = resultado.get(0);
            String sqlItems =
                    "SELECT A.id AS item_id, A.cantidad, B.id AS producto_id, B.nombre, B.precio, B.create_at " +
                    "FROM donaciones_items A " +
                    "LEFT JOIN productos B " +
                    "ON A.producto_id=B.id " +
                    "INNER JOIN donaciones C " +
                    "ON C.id=A.donacion_id " +
                    "WHERE C.id=?";
            List<ItemDonacion> items = this.template.query(sqlItems, new Object[]{id}, new ItemDonacionRowMapper());
            donacion.setItems(items);
        }
        return donacion;
    }

    /**
     * Usamos SQL para generar una query en la que vamos a  modificar los campos de la base de datos con los que vengan
     * del controlador.
     * Como es 1 objeto que a su vez tiene asociado una lista de items que contiene información , haremos 1 bucle
     * para recorrer la lsita que viene del controlador, llamando por cada vuelta al sericio de itemDonacion update.
     *
     * @param id la clave única de cada registro
     * @param donacion
     * @return El objeto Donación cargado con los datos que hemos modificado en la base dedatos.
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Donacion updateOne(Long id, Donacion donacion) {
        log.info("UPDATE - > DONACION");
        Donacion resultado = null;

        String sql =    "UPDATE donaciones D " +
                        "SET D.descripcion=?, " +
                        "D.observacion=?, " +
                        "D.jugador_id=? " +
                        "WHERE D.id=?";
        int row = template.update(sql,
                donacion.getDescripcion(),
                donacion.getObservacion(),
                donacion.getJugador().getId(),
                donacion.getId());
        log.info("DONACION ACTUALIZADA");

        log.info("ACTUALIZANDO ITEMS DE LA DONACION");
        donacion.getItems().forEach(item -> {
            this.itemDonacionesService.updateOne(item.getId(), item);
        });
        log.info("ITEMS ACTUALIZADOS");

        log.info("RECUPERANDO INFORMACION ACTUALIZADA");
        resultado = this.findOne(id);
        return resultado;
    }

    /**
     * Creamos la SQL que nos va a permitir borrar el registro de la condición
     * @param id Es el identificador del registro que vamos a borrar de la base de datos
     * @return
     */
    @Override
    @Transactional (rollbackFor = {Exception.class})
    public Boolean deleteOne(Long id) {
        log.info("DELETE - > DONACIONES");
        String sql =
                "DELETE " +
                "FROM donaciones" +
                "WHERE donaciones.id=?";
        log.info(sql.replace("?", id.toString()));
        int rows = template.update(sql, id);
        if (rows > 0) {
            log.info("ELIMINADO CORRECTAMENTE");
            return true;
        } else {
            log.severe("NO SE HA PODIDO BORRAR EL JUGADOR.");
            return false;
        }
    }

    /**
     * Utilizamos la SQL para recuperar todos los registros de la tabla donaciones
     * Después usamos la clase Mapper de donación para mapear todos los resultados en un objeto.
     * Reccorremos con un bucle el objeto List items de la clase Donaciones, el cual mapeamos con su clase Mapper
     * lo seteamos a la propiedad de donación list items.
     *
     * @return El objeto Donaciones cargado de todos los datos.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Donacion> findAll() {
        log.info("FIND ALL - > Donaciones");
        String sql =
                "SELECT A.id, A.descripcion, A.observacion, A.create_at, A.jugador_id " +
                "FROM donaciones A";
        List<Donacion> resultados = this.template.query(sql, new DonacionRowMapper());
//        log.info("DONACIONES: " + new Gson().toJson(resultados));
        if (resultados.size() > 0) {
            resultados.forEach(e -> {        String sqlItems =
                        "SELECT A.id AS item_id, A.cantidad, B.id AS producto_id, B.nombre, B.precio, B.create_at " +
                        "FROM donaciones_items A " +
                        "LEFT JOIN productos B " +
                        "ON A.producto_id=B.id " +
                        "INNER JOIN donaciones C " +
                        "ON C.id=A.donacion_id " +
                        "WHERE C.id=? ";

                List<ItemDonacion> items = this.template.query(sqlItems, new Object[]{e.getId()}, new ItemDonacionRowMapper());
//                log.info("DONACION: " + e.getId() + ",items: " + new Gson().toJson(items));
                e.setItems(items);
            });
        }
        log.info("Donaciones FindAll terminado");
        return resultados;
    }
}
