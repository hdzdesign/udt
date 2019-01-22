package chc.tfm.udt.Controller;

import chc.tfm.udt.DTO.Jugador;
import chc.tfm.udt.servicio.CrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController(value = "JugadoresController")
public class JugadoresController implements CrudController<Jugador> {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private CrudService<Jugador> service;
    @Autowired
    public JugadoresController(@Qualifier(value = "JugadoresService") CrudService<Jugador> service){
        this.service = service;

    }
    @Override
    @GetMapping(value = "/jugadores")
    public ResponseEntity<List<Jugador>> getAll() {
        List<Jugador> jugadoresList = service.findAll();
        return new ResponseEntity<>(jugadoresList, HttpStatus.OK);
    }

    @Override
    @PostMapping(value = "/jugadores")
    public ResponseEntity<Jugador> createOne(@RequestBody Jugador jugador) {

        Jugador resultado = service.createOne(jugador);
        LOG.info("Introducir datos correctos");
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @Override
    @GetMapping(value = "/jugadores/{id}")
    public ResponseEntity<Jugador> getOne(@PathVariable Long id) {
        if (id != null){
            Jugador resultado = service.findOne(id);
            return new ResponseEntity<>(resultado,HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @PutMapping(value = "/jugadores/{id}")
    public ResponseEntity<Jugador> updateOne(@PathVariable Long id, @RequestBody Jugador jugador) {
        if(id != null && jugador !=null){
            Jugador resultado = service.updateOne(id,jugador);
            return new ResponseEntity<>(resultado, HttpStatus.OK);

        }
         else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    @DeleteMapping(value = "/jugadores/{id}")
    public ResponseEntity<HttpStatus> deleteOne(@PathVariable Long id) {
        if(id != null){
            Boolean resultado = service.deleteOne(id);
            if (resultado) return new ResponseEntity<>(HttpStatus.OK);
            else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
