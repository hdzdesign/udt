package chc.tfm.udt.servicio;

import chc.tfm.udt.DTO.Donacion;
import chc.tfm.udt.DTO.Jugador;
import chc.tfm.udt.entidades.DonacionEntity;
import chc.tfm.udt.entidades.JugadorEntity;
import chc.tfm.udt.convertidores.DonacionConverter;
import chc.tfm.udt.convertidores.JugadorConverter;
import chc.tfm.udt.repositorios.JugadorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "JugadoresService")
public class JugadoresService implements CrudService<Jugador> {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private JugadorRepository jugadorRepository;
    private CrudService<Donacion> donacionesService;
    private JugadorConverter converter;
    private DonacionConverter donacionConverter;



    public JugadoresService (@Qualifier("JugadorRepository") JugadorRepository jugadorRepository,
                             @Qualifier("DonacionesService") @Lazy CrudService<Donacion> donacionesService,
                             @Qualifier("JugadorConverter") JugadorConverter converter,
                             @Qualifier("DonacionConverter")DonacionConverter donacionConverter){
        this.jugadorRepository = jugadorRepository;
        this.donacionesService = donacionesService;
        this.converter = converter;
        this.donacionConverter = donacionConverter;
    }

    @Override
    public Jugador createOne(Jugador jugador) {
        LOG.info("Recuperamos las donaciones del jugador");
        List<Donacion> donaciones = this.getDonacionesFromJugadores(jugador);
        jugador.setDonaciones(donaciones);
        LOG.info("Convertimos el jugador Entity a jugador.");
        JugadorEntity j = converter.convertToDatabaseColumn(jugador);
        JugadorEntity saved = jugadorRepository.save(j);
        Jugador returned = converter.convertToEntityAttribute(saved);

        return returned;
    }

    @Override
    @Transactional(readOnly = true)
    public Jugador findOne(Long id) {
        LOG.info("JugadoresService - findOne");
        Jugador resultado = null;
        Optional<JugadorEntity> buscar = jugadorRepository.findById(id);
        LOG.info("He buscado");
        if(buscar.isPresent()){
            JugadorEntity encontrado = buscar.get();
            resultado = converter.convertToEntityAttribute(encontrado);
            LOG.info("Find completado: " + resultado.toString());
        }
        return resultado;
    }

    @Override
    public Jugador updateOne(Long id, Jugador jugador) {
        Jugador resultado = null;

        Optional<JugadorEntity> buscar = jugadorRepository.findById(id);
        if(buscar.isPresent()){
            JugadorEntity encontrado = buscar.get();

            // setear atributos en el entity encontrado
            encontrado.setNombre(jugador.getNombre());
            encontrado.setApellido1(jugador.getApellido1());
            encontrado.setApellido2(jugador.getApellido2());
            encontrado.setEdad(jugador.getEdad());
            encontrado.setNacionalidad(jugador.getNacionalidad());
            encontrado.setDni(jugador.getDni());
            encontrado.setMail(jugador.getMail());
            encontrado.setTelefono(jugador.getTelefono());
            encontrado.setDorsal(jugador.getDorsal());
            encontrado.setInscripcion(jugador.getInscripcion());
            encontrado.setFoto(jugador.getFoto());

            // encontrar las donaciones del jugador
            List<DonacionEntity> donacionEntities = findDonacionesFromJugador(jugador);

           encontrado.setDonaciones(donacionEntities);

            // guardar cambios
            JugadorEntity guardado = jugadorRepository.save(encontrado);
            resultado = converter.convertToEntityAttribute(guardado);
        }
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


    private List<DonacionEntity> findDonacionesFromJugador(Jugador j) {
        List<DonacionEntity> donacionEntities = new ArrayList<>();

        j.getDonaciones().forEach(dto -> {
            Donacion e = donacionesService.findOne(dto.getId());
            if (e != null){
                DonacionEntity convertido = donacionConverter.convertToDatabaseColumn(e);
                donacionEntities.add(convertido);
            }
        });
        return donacionEntities;
    }

    public List<Donacion> getDonacionesFromJugadores(Jugador j ){
        List<Donacion> todasDonaciones = new ArrayList<>();
        List<Donacion> donaciones = j.getDonaciones();
        if(donaciones !=null){
            donaciones.forEach(e ->{
                Donacion donacion = donacionesService.findOne(e.getId());
                if(donacion !=null) todasDonaciones.add(donacion);
            });
        }
        return todasDonaciones;
    }
}
