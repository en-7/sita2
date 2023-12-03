package protensi.sita.repository;

import protensi.sita.model.AvailableBimbinganModel;
import protensi.sita.model.JadwalBimbinganModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JadwalBimbinganDb extends JpaRepository<JadwalBimbinganModel, Long> {
    Optional<JadwalBimbinganModel> findByIdJadwalBimbingan(Long idJadwalBimbingan);
    JadwalBimbinganModel findByAvailableBimbingan_IdAvailableBimbingan(Long idAvailableBimbingan);
    List<JadwalBimbinganModel> findByMahasiswa_IdUser(Long idUser);

    @Query("SELECT j FROM JadwalBimbinganModel j WHERE j.availableBimbingan.pembimbing.idUser = :userId")
    List<JadwalBimbinganModel> findByPembimbingUserId(@Param("userId") Long userId);

    List<JadwalBimbinganModel> findAllByAvailableBimbingan_IdAvailableBimbinganAndAvailableBimbingan_StartBimbinganTimeBetween(
            Long idUser, LocalDateTime startDate, LocalDateTime endDate);
}