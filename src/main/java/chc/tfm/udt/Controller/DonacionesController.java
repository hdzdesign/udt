package chc.tfm.udt.Controller;

import chc.tfm.udt.DTO.Donacion;
import chc.tfm.udt.servicio.CrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController(value = "DonacionesController")
public class DonacionesController implements CrudController<Donacion> {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private CrudService<Donacion> service;

    @Autowired
    private DonacionesController(@Qualifier(value = "DonacionesService") CrudService<Donacion> service){
        this.service = service;
    }

    @GetMapping(value = "/donaciones")
    public ResponseEntity<List<Donacion>> getAll() {
        List<Donacion> list = service.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping(value = "/donaciones")
    public ResponseEntity<Donacion> createOne(@RequestBody Donacion donacion) {
        LOG.info("Entramos  en el controller");
        LOG.info(donacion.toString());
        Donacion result = service.createOne(donacion);
       LOG.info("Respeusta del servicio");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/donaciones/{id}")
    public ResponseEntity<Donacion> getOne(@PathVariable Long id) {
        if (id != null){
            Donacion result = service.findOne(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/donaciones/{id}")
    public ResponseEntity<Donacion> updateOne(@PathVariable Long id, @RequestBody Donacion donacion) {
        if (id != null && donacion != null) {
            Donacion result = service.updateOne(id, donacion);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/donaciones/{id}")
    public ResponseEntity<HttpStatus> deleteOne(@PathVariable Long id) {
        if (id != null){
            Boolean result = service.deleteOne(id);
            if (result) return new ResponseEntity<>(HttpStatus.OK);
            else return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
