package protensi.sita.service;

import protensi.sita.model.AvailableBimbinganModel;
import protensi.sita.model.UgbModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AvailableBimbinganService {
    AvailableBimbinganModel findById(Long idAvailableBimbingan);
    List<AvailableBimbinganModel> findAll();
    AvailableBimbinganModel save(AvailableBimbinganModel availableBimbingan);
    void delete(Long idAvailableBimbingan);
    void add(AvailableBimbinganModel availableBimbingan);
    List<AvailableBimbinganModel> findAllByIdPembimbing(Long idUser);
    List<AvailableBimbinganModel> listAvailablePembimbing(UgbModel ugb, LocalDate startDate, LocalDate endDate);
    AvailableBimbinganModel findByStartBimbinganTime(Long idUser, LocalDateTime startBimbinganTime);
    AvailableBimbinganModel findByEndBimbinganTime(Long idUser, LocalDateTime endBimbinganTime);
    List<AvailableBimbinganModel> findByStartBimbinganTimeBetween(Long idUser, LocalDateTime startBimbinganTime, LocalDateTime endBimbinganTime);
    List<AvailableBimbinganModel> findByEndBimbinganTimeBetween(Long idUser, LocalDateTime startBimbinganTime, LocalDateTime endBimbinganTime);
    List<AvailableBimbinganModel> findAllByIdPembimbingAndDateRange(Long idUser, LocalDate startDate, LocalDate endDate);
}
