package protensi.sita.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;
import protensi.sita.model.AnnouncementModel;

@Repository
public interface AnnouncementDb extends JpaRepository<AnnouncementModel, Long>{
    

    @Query("SELECT a FROM AnnouncementModel a ORDER BY a.idAnnouncement DESC")
    List<AnnouncementModel> findAllAnnounceByIdDesc();

}
