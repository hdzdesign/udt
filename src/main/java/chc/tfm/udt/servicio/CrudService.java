package chc.tfm.udt.servicio;

import java.util.List;

/**
 * INTERAZ que vamos a usar para todas las clases del Servicio, con una estructura básica del CRUDç
 * @param <T>
 */
public interface CrudService<T> {
    /**
     * Usaremos este metodo para crear un nuevo registro en la base de datos
     * @param t El objeto que vamos a introducir en base de datos
     * @return retornamos el objeto insertado en base de datos para mostrarlo al usuario.
     */

    T createOne(T t);

    /**
     * Usaremos este metodo para buscar 1 registro en la base de datos
     * @param id la clave única de cada registro
     * @return el objeto que hemos encontrado en base de datos
     */

    T findOne(Long id);

    /**
     * Usaremos este metodo para actualizar 1 registro en la base de datos,
     * @param id la clave única de cada registro
     * @param t el objeto que llega de la vista para modificar en base de datos
     * @return el objeto que hemos modificado en base de datos , ya modificado.
     */

    T updateOne(Long id, T t);

    /**
     * Usaremos este metodo para borrar 1 registro en base de datos
     * @param id Es el identificador del registro que vamos a borrar de la base de datos
     * @return Un true o false, true se ha borrado , false no
     */

    Boolean deleteOne(Long id);

    /**
     * Usaremos este metodo para retornar todos los registros de la base de datos , dependiendo del objeto a buscar
     * @return Una lista de todos los registros que hayamos buscado en base de datos.
     */

    List<T> findAll();

}
