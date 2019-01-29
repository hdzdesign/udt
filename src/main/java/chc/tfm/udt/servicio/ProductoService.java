package chc.tfm.udt.servicio;

import chc.tfm.udt.DTO.Producto;
import chc.tfm.udt.mappers.ProductoRowMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 *  Esta es la clase servicio de DONACIÓN , que se encarga de trabajar contra la base de datos y devolver los valores al
 *  controller.
 */
@Log
@Service(value = "ProductoService")
public class ProductoService implements CrudService<Producto> {

    private JdbcTemplate jdbcTemplate;

    /**
     * Constructor y inyección de los objetos que vamos a usar en esta clase.
     * @param jdbcTemplate
     */
    public ProductoService(@Qualifier(value = "JdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Utilizamos la query de SQL para insertar en base de datos las propiedades que vienen de la vista.
     * Recuperamos el ultimo id insertado con un select y la condición LAST_INSERT_ID
     * Mapeamos el objeto con la ayuda de nuestra clase MApper de producto y lo deolemos a la ista
     * @param producto
     * @return
     */
    @Override
    @Transactional
    public Producto createOne(Producto producto) {
        log.info("Entramos en el INSERT DE PRODUCTO");
        Producto inserted = null;

        String sqlProducto = "INSERT INTO productos (nombre, precio, create_at) VALUES (?, ?, NOW())";

        this.jdbcTemplate.update(sqlProducto, producto.getNombre(), producto.getPrecio());
        log.info("PRODUCTO INSERTADO CORRECTAMENTE :)");

        String sqlInserted = "SELECT A.id, A.nombre, A.precio, A.create_at " +
                             "FROM productos A " +
                             "WHERE A.id=LAST_INSERT_ID()";
        List<Producto> results = this.jdbcTemplate.query(sqlInserted, new ProductoRowMapper());
        if (results.size() > 0) {
            inserted = results.get(0);
            log.info("INSERTADO: " + inserted.toString());
        }

        return inserted;
    }

    /**
     * Utilizamos la Query de SQL para recuperar 1 fila de la tabla productos
     * Realizamos el mapeo con nuestra clase Mapper de producto y la devolvemos al controller
     * @param id la clave única de cada registro
     * @return
     */

    @Override
    @Transactional (readOnly = true)
    public Producto findOne(Long id) {
        log.info("Entramos en el FIND ONE de producto");
        Producto resultado = null;

        String sql =    "SELECT A.id, A.nombre, A.precio, A.create_at " +
                        "FROM productos A " +
                        "WHERE A.id=?";
        List<Producto> results = this.jdbcTemplate.query(sql, new Object[]{id}, new ProductoRowMapper());
        if (results.size() > 0) {
            resultado = results.get(0);
            log.info("RECUPERADO: " + resultado.toString());
        }
        return resultado;
    }

    /**
     * Utilizamos la Query de SQL para actualizar 1 registro de la base de datos
     * Recuperamos los valores que llegan desde el controller y utilizamos el findOne para asignarle los valores
     * recogidos del controller
     * @param id la clave única de cada registro
     * @param producto
     * @return el resultado cargado de los nuevos valores.
     */
    @Override
    @Transactional
    public Producto updateOne(Long id, Producto producto) {
       log.info("Entramos en update");
        Producto resultado = null;
        String sqlUpdate =      "UPDATE productos " +
                                "SET nombre=?, precio=? " +
                                "WHERE productos.id=?";

       int row = jdbcTemplate.update(
               sqlUpdate,
               producto.getNombre(),
               producto.getPrecio(),
               id);

       if (row > 0) resultado = this.findOne(id);

       log.info("UPDATE REALIZADO");

        return resultado;
    }

    /**
     * Utilizamos la Query de SQL para borrar 1 registro de la base de datos
     * @param id Es el identificador del registro que vamos a borrar de la base de datos
     * @return true , si se ha borrado correctamente / false si algo fallo
     */
    @Transactional
    @Override
    public Boolean deleteOne(Long id) {

        String sql =    "DELETE " +
                        "FROM productos " +
                        "WHERE productos.id=?";
        log.info(sql.replace("?", id.toString()));
        int rows = jdbcTemplate.update(sql, id);
        log.info("DELETED ROWS: " + rows);
        return rows > 0;
    }

    /**
     * Utilizamos la Query de SQL para buscar todos los registros de la tabla productos
     * Rellenamos la lista utilizando el objeto Mapper de producto
     * @return la lista cargada de los resultados obtenidos de la base de datos.
     */
    @Override
    public List<Producto> findAll() {
        log.info("FIND ALL -> ");

        String sql = "SELECT A.id, A.nombre, A.precio, A.create_at " +
                     "FROM productos A ";

        List<Producto> resultados = this.jdbcTemplate.query(sql, new ProductoRowMapper());

        log.info("FIND ALL TERMINADO");
        return resultados;
    }
}
