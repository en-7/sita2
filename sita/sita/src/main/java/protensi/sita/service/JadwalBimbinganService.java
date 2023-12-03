package protensi.sita.service;

import protensi.sita.model.AvailableBimbinganModel;
import protensi.sita.model.JadwalBimbinganModel;

import java.time.LocalDate;
import java.util.List;

public interface JadwalBimbinganService {
    JadwalBimbinganModel findById(Long idJadwalBimbingan);
    JadwalBimbinganModel findByAvaialableBimbingan(Long idAvailableBimbingan);
    List<JadwalBimbinganModel> findAll();
    List<JadwalBimbinganModel> findBimbinganByIdMahasiswa(Long idMahasiswa);
    List<JadwalBimbinganModel> findBimbinganByIdPembimbing(Long idPembimbing);
    List<JadwalBimbinganModel> findBimbinganByListAvailable(List<AvailableBimbinganModel> listAvailable);
    void save(JadwalBimbinganModel jadwalBimbingan);
    void delete(Long idJadwalBimbingan);
    JadwalBimbinganModel update(JadwalBimbinganModel jadwalBimbingan);
    List<JadwalBimbinganModel> findAllByIdPembimbingAndDateRange(Long idUser, LocalDate startDate, LocalDate endDate);
}
