package chc.tfm.udt.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

/**
 * Esta clase nos va a servir para definir las lineas que va a contener la donación , por el momento solo el id
 * y la cantidad donada al jugador.
 */
@Data
@AllArgsConstructor
@Entity
@Table(name = "donaciones_items")
public class ItemDonacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cantidad")
    private Long cantidad;
    /**
     * ManyToOne: Muchos items en un solo producto.
     * JoinColumn: Indicamos el nombre de la llave foranea de la tabla donaciones_items, aunque se crearía de forma automatica
     * si no lo especificamos
     * ItemDonaciónEntity , tiene una relación unidireccional con EquipaciónEntity, por eso unicamente se especifica aqui la relación.
     */
    @ManyToOne(fetch = FetchType.LAZY/*, targetEntity = ProductoEntity.class*/)
    @JoinColumn(name = "producto_id"/*, referencedColumnName = "id"*/)
    private ProductoEntity productoEntity;

    public ItemDonacionEntity(){}

}
