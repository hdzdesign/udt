package chc.tfm.udt.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface CrudController<T> {

    ResponseEntity<List<T>> getAll();

    ResponseEntity<T> createOne(T t);

    ResponseEntity<T> getOne(Long id);

    ResponseEntity<T> updateOne(Long id, T t);

    ResponseEntity<HttpStatus> deleteOne(Long id);

}
