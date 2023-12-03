package protensi.sita.repository;

import protensi.sita.model.PembimbingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PembimbingDb extends JpaRepository<PembimbingModel, Long> {
    @Query("SELECT idUser FROM PembimbingModel ")
    List<Long> findAllPembimbing();
    PembimbingModel findByIdUser(Long idUser);

    PembimbingModel findByUsername(String username);
}
