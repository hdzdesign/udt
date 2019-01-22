package chc.tfm.udt.repositorios;

import chc.tfm.udt.entidades.DonacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "IDonacionRepository")
public interface DonacionRepository extends JpaRepository<DonacionEntity, Long> {

}
