package chc.tfm.udt.Controller;

import chc.tfm.udt.DTO.Donacion;
import chc.tfm.udt.servicio.CrudService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Clase REST-FULL  que controla el End-Point de jugadores y el End-Point de las donaciones respecto al jugador.
 *
 */
@Log
@RestController(value = "DonacionesController")
public class DonacionesController implements CrudController<Donacion> {
    private CrudService<Donacion> service;
    /**
     * Constructor que Inyecta el servicio de la clase Donacion, y inicializa.
     * @param service
     */
    @Autowired
    private DonacionesController(@Qualifier(value = "DonacionesService") CrudService<Donacion> service){
        this.service = service;
    }
    /**
     * End-Point que recupera todos las donaciones de la base de datos llamando al servicio y devolviendo en JSON.
     * @return
     */
    @GetMapping(value = "/donaciones")
    public ResponseEntity<List<Donacion>> getAll() {
        List<Donacion> list = service.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * End-Point que Inserta un Donacion de la base de datos llamando al servicio y devolviendo una respeusta OK
     * @param donacion
     * @return
     */
    @PostMapping(value = "/donaciones")
    public ResponseEntity<Donacion> createOne(@RequestBody Donacion donacion) {
        try {
            log.info("Entramos  en el controller");
            log.info(donacion.toString());
            Donacion result = service.createOne(donacion);
            log.info("Respuesta del servicio");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.severe(e.toString());
            log.severe(e.getMessage());
            Donacion d = new Donacion();
            d.setError(e.getMessage());
            return new ResponseEntity<>(d, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * End-Point que recupera una Donacion de la base de datos llamando al servicio y devolviendo en JSON.
     * @param id
     * @return
     */
    @GetMapping(value = "/donaciones/{id}")
    public ResponseEntity<Donacion> getOne(@PathVariable Long id) {
        if (id != null){
            Donacion result = service.findOne(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * End-Point que Actualiza un jugador de la base de datos llamando al servicio y devolviendo en JSON.
     * @param id
     * @param donacion
     * @return
     */
    @PutMapping(value = "/donaciones/{id}")
    public ResponseEntity<Donacion> updateOne(@PathVariable Long id, @RequestBody Donacion donacion) {
        if (id != null && donacion != null) {
            Donacion result = service.updateOne(id, donacion);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * End-Point que borra un jugador de la base de datos llamando al servicio Jugadores
     * @param id
     * @return
     */
    @DeleteMapping(value = "/donaciones/{id}")
    public ResponseEntity<HttpStatus> deleteOne(@PathVariable Long id) {
        if (id != null){
            Boolean result = service.deleteOne(id);
            if (result) return new ResponseEntity<>(HttpStatus.OK);
            else return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
