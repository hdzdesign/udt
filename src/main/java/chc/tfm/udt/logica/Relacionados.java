/*
package chc.tfm.udt.logica;

import chc.tfm.udt.DTO.Donacion;
import chc.tfm.udt.DTO.ItemDonacion;
import chc.tfm.udt.DTO.Producto;
import chc.tfm.udt.convertidores.ProductoConverter;
import chc.tfm.udt.entidades.ItemDonacionEntity;
import chc.tfm.udt.servicio.DonacionesService;
import chc.tfm.udt.servicio.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("Relacionados")
public class Relacionados {

    private ProductoService productoService;
    private Donacion donacion;
    private List<ItemDonacion> listaItems;
   private DonacionesService donacionesService;

    @Autowired
    public Relacionados(@Qualifier("ProductoService") ProductoService productoService,
                        @Qualifier("DonacionesService") DonacionesService donacionesService){
        this.productoService = productoService;
        this.donacion = donacion;
        this.listaItems = new ArrayList<>();
        this.donacionesService = donacionesService;
    }

    public Donacion enlazar (Long[] itemsId, Integer[] cantidad){
        for(int i=0; i < itemsId.length; i++){

            Producto producto = productoService.findOne(itemsId[i]);

            ItemDonacion linea = new ItemDonacion();
            linea.setCantidad(cantidad[i]);
            linea.setProducto(producto);
            listaItems.add(linea);

        }
        donacionesService.createOne(donacion);

        return donacion;
*/


   /* }

    public void addItemDonacion(ItemDonacion item) {
        donacion.setItems(listaItems);

    }
*/



//