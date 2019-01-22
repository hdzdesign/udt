package chc.tfm.udt.servicio;

import chc.tfm.udt.DTO.ItemDonacion;
import chc.tfm.udt.DTO.Producto;
import chc.tfm.udt.entidades.ItemDonacionEntity;
import chc.tfm.udt.convertidores.ItemConverter;
import chc.tfm.udt.repositorios.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("ItemDonacionesService")
public class ItemDonacionesService implements CrudService<ItemDonacion> {

    private ItemRepository repository;
    private ItemConverter converter;
    private ProductoService productoService;

    @Autowired
    public ItemDonacionesService(@Qualifier("ItemConverter") ItemConverter converter,
                                 @Qualifier("ItemRepository") ItemRepository repository,
                                 @Qualifier("ProductoService") ProductoService productoService) {
        this.converter = converter;
        this.repository = repository;
        this.productoService = productoService;
    }

    @Override
    public ItemDonacion createOne(ItemDonacion itemDonacion) {
        Producto p = productoService.findOne(itemDonacion.getProducto().getId());
        itemDonacion.setProducto(p);
        ItemDonacionEntity i = converter.convertToDatabaseColumn(itemDonacion);
        ItemDonacionEntity saved = repository.save(i);
        return converter.convertToEntityAttribute(saved);
    }

    @Override
    public ItemDonacion findOne(Long id) {
        ItemDonacion result = null;
        if (id != null) {
            Optional<ItemDonacionEntity> found = repository.findById(id);
            result = found.isPresent() ? converter.convertToEntityAttribute(found.get()) : null;
        }
        return result;
    }

    @Override
    public ItemDonacion updateOne(Long id, ItemDonacion itemDonacion) {
        ItemDonacion result = null;
        Optional<ItemDonacionEntity> found = repository.findById(id);
        if (found.isPresent()) {
            ItemDonacionEntity i = found.get();
            i.setCantidad(itemDonacion.getCantidad());
            ItemDonacionEntity saved = repository.save(i);
            result = converter.convertToEntityAttribute(saved);
        }
        return result;
    }

    @Override
    public Boolean deleteOne(Long id) {
        boolean deleted = false;
        Optional<ItemDonacionEntity> found = repository.findById(id);
        if (found.isPresent()) {
            repository.delete(found.get());
            deleted = true;
        }
        return deleted;
    }

    @Override
    public List<ItemDonacion> findAll() {
        return repository.findAll()
                .stream()
                .map(entity ->
                        converter.convertToEntityAttribute(entity))
                .collect(Collectors.toList());
    }
}

