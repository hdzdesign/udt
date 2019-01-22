package chc.tfm.udt.servicio;

import chc.tfm.udt.DTO.Producto;
import chc.tfm.udt.entidades.ProductoEntity;
import chc.tfm.udt.convertidores.ProductoConverter;
import chc.tfm.udt.repositorios.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "ProductoService")
public class ProductoService implements CrudService<Producto> {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private ProductoRepository productoRepository;
    private ProductoConverter productoConverter;

    public ProductoService(@Qualifier(value = "ProductoRepository") ProductoRepository productoRepository,
                           @Qualifier(value = "ProductoConverter") ProductoConverter productoConverter) {
        this.productoRepository = productoRepository;
        this.productoConverter = productoConverter;
    }

    @Override
    public Producto createOne(Producto producto) {
        ProductoEntity pe = productoConverter.convertToDatabaseColumn(producto);
        ProductoEntity saved = productoRepository.save(pe);
        Producto returned = productoConverter.convertToEntityAttribute(saved);
        return returned;
    }

    @Override
    @Transactional (readOnly = true)
    public Producto findOne(Long id) {
        Producto resultado = null;
        Optional<ProductoEntity> buscar = productoRepository.findById(id);
        if(buscar.isPresent()){
            ProductoEntity encontrado = buscar.get();
            resultado = productoConverter.convertToEntityAttribute(encontrado);
        }
        return resultado;
    }

    @Override
    public Producto updateOne(Long id, Producto producto) {
        LOG.info("Entramos en update");
        Producto resultado = null;
        Optional<ProductoEntity> buscar = productoRepository.findById(id);
        if(buscar.isPresent()){
            ProductoEntity encontrado = buscar.get();

            encontrado.setNombre(producto.getNombre());
            encontrado.setPrecio(producto.getPrecio());
            encontrado.setCreateAt(producto.getCreateAt());

            ProductoEntity guardado = productoRepository.save(encontrado);
            resultado = productoConverter.convertToEntityAttribute(guardado);
        }
        return resultado;
    }

    @Override
    public Boolean deleteOne(Long id) {
        if(productoRepository.findById(id).isPresent()){
            productoRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<Producto> findAll() {
        List<Producto> resultado = productoRepository.findAll().
                stream().
                map(entity -> productoConverter.convertToEntityAttribute(entity)).
                collect(Collectors.toList());
        return resultado;
    }
}
