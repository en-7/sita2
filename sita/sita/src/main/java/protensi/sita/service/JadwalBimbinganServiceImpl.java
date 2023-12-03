package protensi.sita.service;

import protensi.sita.model.AvailableBimbinganModel;
import protensi.sita.model.JadwalBimbinganModel;
import protensi.sita.model.SeminarProposalModel;
import protensi.sita.model.UgbModel;
import protensi.sita.model.UserModel;
import protensi.sita.repository.JadwalBimbinganDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

@Service
@Transactional
public class JadwalBimbinganServiceImpl implements JadwalBimbinganService {
    @Autowired
    JadwalBimbinganDb jadwalBimbinganDb;

    @Override
    public JadwalBimbinganModel findById(Long idJadwalBimbingan) {
        Optional<JadwalBimbinganModel> result = jadwalBimbinganDb.findById(idJadwalBimbingan);
        return result.orElse(null);
    }

    @Override
    public List<JadwalBimbinganModel> findAll() {
        return jadwalBimbinganDb.findAll();
    }

    @Override
    public void save(JadwalBimbinganModel jadwalBimbingan) {
        jadwalBimbinganDb.save(jadwalBimbingan);
    }

    @Override
    public JadwalBimbinganModel update(JadwalBimbinganModel jadwalBimbingan) {
        jadwalBimbinganDb.save(jadwalBimbingan);
        return jadwalBimbingan;
    }

    @Override
    public void delete(Long idJadwalBimbingan) {
        jadwalBimbinganDb.deleteById(idJadwalBimbingan);
    }

    @Override
    public JadwalBimbinganModel findByAvaialableBimbingan(Long idAvailableBimbingan){
        JadwalBimbinganModel result = jadwalBimbinganDb.findByAvailableBimbingan_IdAvailableBimbingan(idAvailableBimbingan);
        return result;
    }

    @Override
    public List<JadwalBimbinganModel> findBimbinganByIdMahasiswa(Long idUser){
        return jadwalBimbinganDb.findByMahasiswa_IdUser(idUser);
    }

    @Override
    @Transactional
    public List<JadwalBimbinganModel> findBimbinganByListAvailable(List<AvailableBimbinganModel> listAvailable){
        List<JadwalBimbinganModel> listjadwalBimbingan= new ArrayList<JadwalBimbinganModel>();
        for (AvailableBimbinganModel available : listAvailable) {
            JadwalBimbinganModel jadwalbimbingan = jadwalBimbinganDb.findByAvailableBimbingan_IdAvailableBimbingan(available.getIdAvailableBimbingan());
            if(jadwalbimbingan != null){
                listjadwalBimbingan.add(jadwalbimbingan);
            }
        }
        return listjadwalBimbingan;
    }

    @Override
    public List<JadwalBimbinganModel> findBimbinganByIdPembimbing(Long idPembimbing){
        return jadwalBimbinganDb.findByPembimbingUserId(idPembimbing);
    }

    @Override
    public List<JadwalBimbinganModel> findAllByIdPembimbingAndDateRange(Long idUser, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay().plusDays(1).minusSeconds(1);
        return jadwalBimbinganDb.findAllByAvailableBimbingan_IdAvailableBimbinganAndAvailableBimbingan_StartBimbinganTimeBetween(idUser, startDateTime, endDateTime);
    }

}
