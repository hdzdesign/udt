package chc.tfm.udt.servicio;

import java.util.List;

public interface CrudService<T> {

    T createOne(T t);

    T findOne(Long id);

    T updateOne(Long id, T t);

    Boolean deleteOne(Long id);

    List<T> findAll();

}
