package protensi.sita.repository;

import protensi.sita.model.TimelineModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TimelineDb extends JpaRepository<TimelineModel, Long>{
    TimelineModel findByPeriode(String periode);
}
