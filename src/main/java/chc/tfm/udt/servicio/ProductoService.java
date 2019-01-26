package chc.tfm.udt.servicio;

import chc.tfm.udt.DTO.Producto;
import chc.tfm.udt.mappers.ProductoRowMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Log
@Service(value = "ProductoService")
public class ProductoService implements CrudService<Producto> {

    private JdbcTemplate jdbcTemplate;

    public ProductoService(@Qualifier(value = "JdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
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

    @Override
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
