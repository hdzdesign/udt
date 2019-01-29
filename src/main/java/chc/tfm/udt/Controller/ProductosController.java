package chc.tfm.udt.Controller;

import chc.tfm.udt.DTO.Producto;
import chc.tfm.udt.servicio.CrudService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  Clase REST-FULL  que controla el End-Point de productos y el End-Point de las donaciones respecto al jugador.
 */
@RestController(value = "ProductosController")
public class ProductosController implements CrudController<Producto> {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private CrudService<Producto> service;

    /**
     * Constructor que Inyecta el servicio de la clase Producto, y inicializa.
     * @param service
     */
    @Autowired
    public ProductosController(@Qualifier(value = "ProductoService")CrudService<Producto> service) {
        this.service = service;
    }

    /**
     *  End-Point que recupera todos los productos de la base de datos llamando al servicio y devolviendo en JSON.
     * @return
     */
    @Override
    @GetMapping(value = "/productos")
    public ResponseEntity<List<Producto>> getAll() {
        List<Producto> productosList = service.findAll();
       return new ResponseEntity<>(productosList, HttpStatus.OK);
    }

    /**
     *   End-Point que Inserta un jugador de la base de datos llamando al servicio y devolviendo una respeusta OK
     * @param producto
     * @return
     */
    @Override
    @PostMapping(value = "/productos")
    public ResponseEntity<Producto> createOne(@RequestBody Producto producto) {
        Producto resultado = service.createOne(producto);
        LOG.info("Se han introducido correctamente");
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    /**
     * End-Point que recupera un producto de la base de datos llamando al servicio y devolviendo en JSON.
     * @param id
     * @return
     */
    @Override
    @GetMapping(value = "/productos/{id}")
    public ResponseEntity<Producto> getOne(@PathVariable Long id) {
        if(id != null){
            Producto resultado = service.findOne(id);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * End-Point que Actualiza un producto de la base de datos llamando al servicio y devolviendo en JSON.
     * @param id
     * @param producto
     * @return
     */
    @Override
    @PutMapping(value = "/productos/{id}")
    public ResponseEntity<Producto> updateOne(@PathVariable Long id, @RequestBody Producto producto) {
        LOG.info("Estamos en controller.");
        if(id != null && producto != null){
            Producto resultado = service.updateOne(id,producto);
            LOG.info("Resultado");
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * End-Point que borra un jugador de la base de datos llamando al servicio Jugadores
     * @param id
     * @return
     */
    @Override
    @DeleteMapping(value = "/productos/{id}")
    public ResponseEntity<HttpStatus> deleteOne(@PathVariable Long id) {
        if(id != null){
            Boolean resultado = service.deleteOne(id);
            if(resultado) return new ResponseEntity<>(HttpStatus.OK);
            else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
