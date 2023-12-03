package protensi.sita.service;

import protensi.sita.model.AvailableBimbinganModel;
import protensi.sita.model.UgbModel;
import protensi.sita.model.UserModel;
import protensi.sita.repository.AvailableBimbinganDb;

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
public class AvailableBimbinganServiceImpl implements AvailableBimbinganService {
    @Autowired
    AvailableBimbinganDb availableBimbinganDb;

    @Override
    public AvailableBimbinganModel findById(Long idAvailableBimbingan) {
        Optional<AvailableBimbinganModel> result = availableBimbinganDb.findById(idAvailableBimbingan);
        return result.orElse(null);
    }

    @Override
    public List<AvailableBimbinganModel> findAll() {
        return availableBimbinganDb.findAll();
    }

    @Override
    public AvailableBimbinganModel save(AvailableBimbinganModel availableBimbingan) {
        return availableBimbinganDb.save(availableBimbingan);
    }

    @Override
    public void delete(Long idAvailableBimbingan) {
        availableBimbinganDb.deleteById(idAvailableBimbingan);
    }

    @Override
    public void add(AvailableBimbinganModel availableBimbingan) {
        availableBimbinganDb.save(availableBimbingan);
    }

    @Override
    public List<AvailableBimbinganModel> findAllByIdPembimbing(Long idUser){
        return availableBimbinganDb.findAllByPembimbing_IdUser(idUser);
    }

    @Override
    @Transactional
    public List<AvailableBimbinganModel> listAvailablePembimbing(UgbModel ugb, LocalDate startDate, LocalDate endDate){
        Set<UserModel> pembimbingSet = ugb.getPembimbing();
        List<UserModel> listPembimbing= new ArrayList<>(pembimbingSet);
        List<AvailableBimbinganModel> listAvailable = new ArrayList<AvailableBimbinganModel>();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay().plusDays(1).minusSeconds(1);
        for (UserModel pembimbing : listPembimbing) {
            List<AvailableBimbinganModel> listAvailablePembimbing = availableBimbinganDb.findAllByPembimbing_IdUserAndStartBimbinganTimeBetween(pembimbing.getIdUser(), startDateTime, endDateTime);
            listAvailable.addAll(listAvailablePembimbing);
        }
        return listAvailable;
    }

    @Override
    public AvailableBimbinganModel findByStartBimbinganTime(Long idUser, LocalDateTime startBimbinganTime) {
        return availableBimbinganDb.findByPembimbing_IdUserAndStartBimbinganTime(idUser, startBimbinganTime);
    }

    @Override
    public AvailableBimbinganModel findByEndBimbinganTime(Long idUser, LocalDateTime endBimbinganTime) {
        return availableBimbinganDb.findByPembimbing_IdUserAndEndBimbinganTime(idUser, endBimbinganTime);
    }

    @Override
    public List<AvailableBimbinganModel> findByStartBimbinganTimeBetween(Long idUser, LocalDateTime startBimbinganTime, LocalDateTime endBimbinganTime) {
        return availableBimbinganDb.findByPembimbing_IdUserAndStartBimbinganTimeBetween(idUser, startBimbinganTime, endBimbinganTime);
    }

    @Override
    public List<AvailableBimbinganModel> findByEndBimbinganTimeBetween(Long idUser, LocalDateTime startBimbinganTime, LocalDateTime endBimbinganTime) {
        return availableBimbinganDb.findByPembimbing_IdUserAndEndBimbinganTimeBetween(idUser, startBimbinganTime, endBimbinganTime);
    }

    @Override
    public List<AvailableBimbinganModel> findAllByIdPembimbingAndDateRange(Long idUser, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay().plusDays(1).minusSeconds(1);
        return availableBimbinganDb.findAllByPembimbing_IdUserAndStartBimbinganTimeBetween(idUser, startDateTime, endDateTime);
    }

}
