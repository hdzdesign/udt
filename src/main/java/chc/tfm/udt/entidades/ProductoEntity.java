package chc.tfm.udt.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

/**
 * Clase Entity que se comunica con la base de datos para trabajar con los productos relacionados con cada jugador.
 */
@Data
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class ProductoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "precio")
    private Double precio;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_at")
    private Date createAt;

    //Metodo que usaremos para persistir la fecha justo en el momento de crear la claes
    @PrePersist
    public void prePersist() {
        createAt = new Date();
    }
    public ProductoEntity(){}

}
