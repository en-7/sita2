package protensi.sita.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import protensi.sita.model.AnnouncementModel;

@Repository
public interface AnnouncementDb extends JpaRepository<AnnouncementModel, Long>{
    
}
