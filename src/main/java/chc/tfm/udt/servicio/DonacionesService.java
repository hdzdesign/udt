package chc.tfm.udt.servicio;

import chc.tfm.udt.DTO.Donacion;
import chc.tfm.udt.DTO.ItemDonacion;
import chc.tfm.udt.DTO.Jugador;
import chc.tfm.udt.entidades.DonacionEntity;
import chc.tfm.udt.entidades.ItemDonacionEntity;
import chc.tfm.udt.convertidores.DonacionConverter;
import chc.tfm.udt.convertidores.ItemConverter;
import chc.tfm.udt.repositorios.DonacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;


@Service(value = "DonacionesService")
public class DonacionesService implements CrudService<Donacion> {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private DonacionRepository donacionRepository;
    private ItemDonacionesService itemDonacionesService;
    private DonacionConverter converter;
    private ItemConverter itemConverter;
    private JugadoresService jugadoresService;

    @Autowired
    public DonacionesService(@Qualifier("IDonacionRepository") DonacionRepository donacionRepository,
                             @Qualifier("DonacionConverter") DonacionConverter converter,
                             @Qualifier("ItemConverter")ItemConverter itemConverter,
                             @Qualifier("ItemDonacionesService") ItemDonacionesService itemDonacionesService,
                             @Qualifier("JugadoresService") @Lazy JugadoresService jugadoresService)
    {
        this.donacionRepository = donacionRepository;
        this.converter = converter;
        this.itemConverter = itemConverter;
        this.itemDonacionesService = itemDonacionesService;
        this.jugadoresService = jugadoresService;
    }

    @Override
    public Donacion createOne(Donacion donacion) {
        LOG.info("LLegamos al servicio");
        // recuperar datos del jugador
        Jugador recuperado = jugadoresService.findOne(donacion.getJugador().getId());
        donacion.setJugador(recuperado);
        DonacionEntity d = converter.convertToDatabaseColumn(donacion);

        LOG.info("Convertimos a ENTITY");
        LOG.info(d.toString());


        DonacionEntity saved = donacionRepository.save(d);
        LOG.info("Guardamos en la base de datos.");
        Donacion returned = converter.convertToEntityAttribute(saved);
        LOG.info("Convertimos de nuevo a DTO ");
        return returned;
    }
    @Override
@Transactional(readOnly = true)    
    public Donacion findOne(Long id) {
        Donacion resultado = null;
        Optional<DonacionEntity> buscar = donacionRepository.findById(id);
        if(buscar.isPresent()){
            DonacionEntity encontrado = buscar.get();
            resultado = converter.convertToEntityAttribute(encontrado);
        }
        return resultado;
    }

    @Override
    @Transactional
        public Donacion updateOne(Long id, Donacion donacion) {
        Donacion resultado = null;

        Optional<DonacionEntity> buscar = donacionRepository.findById(id);
        if(buscar.isPresent()){
            DonacionEntity encontrado = buscar.get();

            encontrado.setId(donacion.getId());
            encontrado.setObservacion(donacion.getObservacion());
            encontrado.setDescripcion(donacion.getDescripcion());
            encontrado.setCreateAt(donacion.getCreateAt());
            if(encontrado.getId() != null){
                //encontrado.setJugadorEntity(donacion.getJugador());

            }

            //Encontrar los items de cada donacion
            List<ItemDonacionEntity> itemsDonacion = findItemsDonacionFromDonacion(donacion);
            encontrado.setItems(itemsDonacion);
            // Guardamos
           DonacionEntity guardado = donacionRepository.save(encontrado);
           resultado = converter.convertToEntityAttribute(guardado);

        }

        return resultado;
    }

    @Override
    public Boolean deleteOne(Long id) {
        if(donacionRepository.findById(id).isPresent()) {
            donacionRepository.deleteById(id);
        }
        return true;
    }

    @Override
    public List<Donacion> findAll() {
        List<Donacion> resultado = donacionRepository.findAll().
                stream().
                map(d -> converter.convertToEntityAttribute(d)).
                collect(Collectors.toList());
        return resultado;
    }

    private List<ItemDonacionEntity> findItemsDonacionFromDonacion(Donacion d){
        List<ItemDonacionEntity> itemsEntities = new ArrayList<>();

        d.getItems().forEach(dto -> {
            ItemDonacion e = itemDonacionesService.findOne(dto.getId());
            if(d != null){
                ItemDonacionEntity convertido = itemConverter.convertToDatabaseColumn(e);
                itemsEntities.add(convertido);
            }
        });
        return itemsEntities;
    }
}
