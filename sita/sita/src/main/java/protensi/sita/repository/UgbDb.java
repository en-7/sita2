package protensi.sita.repository;

import protensi.sita.model.MahasiswaModel;
import protensi.sita.model.UgbModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface UgbDb extends JpaRepository<UgbModel, Long> {
   @Query("SELECT o FROM UgbModel o WHERE o.statusDokumen = :status ")
   List<UgbModel> getUgbBasedOnStatus(@Param("status") String status);

   UgbModel findByIdUgb(Long idUgb);
   
   <Optional> UgbModel findByMahasiswa(MahasiswaModel mahasiswa);
   
}
