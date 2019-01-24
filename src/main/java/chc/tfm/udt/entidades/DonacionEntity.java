package chc.tfm.udt.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Clase que va a Persistir en base de datos los datos básicos de la adquisición de 1 equipación de futbol que el equipo
 * dona al jugador con su incorporación al equipo , automaticamente cuando un jugador entra en el equipo se le asigna 1 equipación
 * por tanto estará implementado en el registro del jugador.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "donaciones")
public class DonacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "observacion")
    private String observacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_at")
    private Date createAt;

    /**
     * Muchas Donaciones un solo jugador.
     * Existe una relación bidireccional.
     */
    @ManyToOne(fetch = FetchType.LAZY/*, targetEntity = JugadorEntity.class*/)
    //@JoinColumn(name = "jugador_id", referencedColumnName = "id")
    // Solo se realiza la consulta cuando se invoca al metodo
    private JugadorEntity jugadorEntity;

    /**
     * Una donación contiene muchos itemsDonaciónEntity.
     * JoinColum: Indicamos a la base de datos cual es la llave foranea de la relación ,
     * puesto que es una relación unidireccional.
     * Hay que crear el campo donacion_id en la tabla  en base datos no en el Entity.
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true/*, targetEntity = ItemDonacionEntity.class*/)
    @JoinColumn(name = "donacion_id"/*, referencedColumnName = "id"*/)
    private List<ItemDonacionEntity> items;


    //Metodo que usaremos para persistir la fecha justn en el momento de crear la claes
    @PrePersist
    public void prePersist() {
        createAt = new Date();
    }

}
