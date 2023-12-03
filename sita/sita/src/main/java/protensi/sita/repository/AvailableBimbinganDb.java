package protensi.sita.repository;

import protensi.sita.model.AvailableBimbinganModel;
import protensi.sita.model.PembimbingModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableBimbinganDb extends JpaRepository<AvailableBimbinganModel, Long> {
    Optional<AvailableBimbinganModel> findByIdAvailableBimbingan(Long idAvailableBimbingan);
    List<AvailableBimbinganModel> findAllByPembimbing_IdUser(Long idUser);
    AvailableBimbinganModel findByStartBimbinganTime(LocalDateTime startBimbinganTime);
    AvailableBimbinganModel findByEndBimbinganTime(LocalDateTime endBimbinganTime);
    List<AvailableBimbinganModel> findByStartBimbinganTimeBetween(LocalDateTime startBimbinganTime, LocalDateTime endBimbinganTime);
    List<AvailableBimbinganModel> findByEndBimbinganTimeBetween(LocalDateTime startBimbinganTime, LocalDateTime endBimbinganTime);
    

}
