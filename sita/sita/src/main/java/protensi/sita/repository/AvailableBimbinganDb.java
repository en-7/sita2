package protensi.sita.repository;

import protensi.sita.model.AvailableBimbinganModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableBimbinganDb extends JpaRepository<AvailableBimbinganModel, Long> {
    Optional<AvailableBimbinganModel> findByIdAvailableBimbingan(Long idAvailableBimbingan);
    List<AvailableBimbinganModel> findAllByPembimbing_IdUser(Long idUser);
    AvailableBimbinganModel findByPembimbing_IdUserAndStartBimbinganTime(Long idUser, LocalDateTime startBimbinganTime);
    AvailableBimbinganModel findByPembimbing_IdUserAndEndBimbinganTime(Long idUser, LocalDateTime endBimbinganTime);
    List<AvailableBimbinganModel> findByPembimbing_IdUserAndStartBimbinganTimeBetween(Long idUser, LocalDateTime startBimbinganTime, LocalDateTime endBimbinganTime);
    List<AvailableBimbinganModel> findByPembimbing_IdUserAndEndBimbinganTimeBetween(Long idUser, LocalDateTime startBimbinganTime, LocalDateTime endBimbinganTime);
    
    // buat filter per pekan
    List<AvailableBimbinganModel> findAllByPembimbing_IdUserAndStartBimbinganTimeBetween(
            Long idUser, LocalDateTime startDate, LocalDateTime endDate);

}
