package protensi.sita.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import protensi.sita.model.EvaluasiUgbModel;
import protensi.sita.model.MahasiswaModel;
import protensi.sita.model.UgbModel;
import java.util.*;

public interface EvaluasiUgbDb extends JpaRepository<EvaluasiUgbModel, Long> {

    EvaluasiUgbModel findByIdEvaluasiUgb(Long idUgb);

    @Query("SELECT o FROM EvaluasiUgbModel o WHERE o.ugb = :thisUgb ")
    List<EvaluasiUgbModel> getEvaluasiByUgb(@Param("thisUgb") UgbModel thisUgb);

}
