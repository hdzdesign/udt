package chc.tfm.udt.Controller;

import chc.tfm.udt.DTO.Donacion;
import chc.tfm.udt.DTO.Jugador;
import chc.tfm.udt.servicio.JugadoresService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase REST-FULL  que controla el End-Point de jugadores y el End-Point de las donaciones respecto al jugador.
 *
 */
@RestController(value = "JugadoresController")
public class JugadoresController implements CrudController<Jugador> {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private JugadoresService service;

    /**
     * Constructor que Inyecta el servicio de la clase Jugador, y inicializa.
     * @param service
     */
    @Autowired
    public JugadoresController(@Qualifier(value = "JugadoresService") JugadoresService service){
        this.service = service;

    }

    /**
     * End-Point que recupera todos los jugadores de la base de datos llamando al servicio y devolviendo en JSON.
     * @return
     */
    @Override
    @GetMapping(value = "/jugadores")
    public ResponseEntity<List<Jugador>> getAll() {
        List<Jugador> jugadoresList = service.findAll();
        return new ResponseEntity<>(jugadoresList, HttpStatus.OK);
    }

    /**
     * End-Point que Inserta un jugador de la base de datos llamando al servicio y devolviendo una respeusta OK
     * @param jugador
     * @return
     */
    @Override
    @PostMapping(value = "/jugadores")
    public ResponseEntity<Jugador> createOne(@RequestBody Jugador jugador) {

        Jugador resultado = service.createOne(jugador);
        LOG.info("Introducir datos correctos");
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    /**
     * End-Point que recupera un jugador de la base de datos llamando al servicio y devolviendo en JSON.
     * @param id
     * @return
     */
    @Override
    @GetMapping(value = "/jugadores/{id}")
    public ResponseEntity<Jugador> getOne(@PathVariable Long id) {
        if (id != null){
            Jugador resultado = service.findOne(id);
            return new ResponseEntity<>(resultado,HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * End-Point para recuperar las donaciones de un jugador de la base de datos, llamando al servicio.
     * Jugadores.
     * @param id
     * @return
     */
    @GetMapping(value = "/jugadores/{id}/donaciones")
    public ResponseEntity<List<Donacion>> getJugadorDonations(@PathVariable Long id){
        List<Donacion> resultados = new ArrayList<>();
        if(id != null){
            resultados = service.findJugadorDonations(id);
            return new ResponseEntity<>(resultados, HttpStatus.OK);
        }
        else return new ResponseEntity<>(resultados, HttpStatus.BAD_REQUEST);
    }

    /**
     * End-Point que Actualiza un jugador de la base de datos llamando al servicio y devolviendo en JSON.
     * @param id
     * @param jugador
     * @return
     */
    @Override
    @PutMapping(value = "/jugadores/{id}")
    public ResponseEntity<Jugador> updateOne(@PathVariable Long id, @RequestBody Jugador jugador) {
        if(id != null && jugador !=null){
            Jugador resultado = service.updateOne(id,jugador);
            return new ResponseEntity<>(resultado, HttpStatus.OK);

        }
         else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * End-Point que borra un jugador de la base de datos llamando al servicio Jugadores
     * @param id
     * @return
     */
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
