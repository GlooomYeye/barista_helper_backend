package my.cousework.barista_helper.store.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import my.cousework.barista_helper.store.entities.StepEntity;


@Repository
public interface StepRepository extends JpaRepository<StepEntity, Long> {

}
