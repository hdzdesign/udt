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

@Log
@Service(value = "DonacionesService")
public class DonacionesService implements CrudService<Donacion> {

    private ItemDonacionesService itemDonacionesService;
    private JdbcTemplate template;

    @Autowired
    public DonacionesService(@Qualifier("ItemDonacionesService") ItemDonacionesService itemDonacionesService,
            @Qualifier("JdbcTemplate") @Lazy JdbcTemplate template) {
        this.itemDonacionesService = itemDonacionesService;
        this.template = template;
    }

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
