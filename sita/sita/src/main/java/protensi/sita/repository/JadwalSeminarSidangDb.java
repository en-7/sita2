package protensi.sita.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import protensi.sita.model.JadwalSidangModel;

import java.util.Optional;

@Repository
public interface JadwalSeminarSidangDb extends JpaRepository<JadwalSidangModel, Long>{

//    Optional<JadwalSidangModel> findById(Long id);
}
