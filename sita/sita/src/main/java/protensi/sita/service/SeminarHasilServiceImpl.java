package protensi.sita.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import protensi.sita.model.SeminarHasilModel;
import protensi.sita.model.SeminarProposalModel;
import protensi.sita.repository.SeminarHasilDb;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SeminarHasilServiceImpl implements SeminarHasilService {
    @Autowired
    SeminarHasilDb seminarHasilDb;

    @Override
    public void addSeminarHasil(SeminarHasilModel seminarHasil) {
        seminarHasilDb.save(seminarHasil);
    }

    @Override
    public SeminarHasilModel updateSemhas(SeminarHasilModel seminarHasil) {
        seminarHasilDb.save(seminarHasil);
        return seminarHasil;
    }

    @Override
    public List<SeminarHasilModel> findAllSeminarHasil() {
        return seminarHasilDb.findAll();
    }

    @Override
    public SeminarHasilModel findSemhasById(Long idSeminarHasil){
        Optional<SeminarHasilModel> semhas = seminarHasilDb.findById(idSeminarHasil);
        if(semhas.isPresent()){
            return semhas.get();
        }
        else return null;
    }

    @Override
    public List<SeminarHasilModel> findAllByPembimbing(Long pembimbingId) {
        return seminarHasilDb.findAllByPembimbing(pembimbingId);
    }

    @Override
    public List<SeminarHasilModel> findAllByPenguji(Long pengujiId) {
        return seminarHasilDb.findAllByPenguji(pengujiId);
    }

//    @Override
//    public SeminarHasilModel findSemhasById(Long idSeminarHasil) {
//        Optional<SeminarHasilModel> semhas = seminarHasilDb.findByIdSeminarHasil(idSeminarHasil);
//        if (semhas.isPresent()) {
//            return semhas.get();
//        } else
//            return null;
//    }

    @Override
    public List<SeminarHasilModel> findSemhasByStatusDokumen(String statusDokumen) {
        return seminarHasilDb.findAllByStatusDokumen(statusDokumen);
    }

    @Override
    public SeminarHasilModel saveNilaiAndStatus(Long idSeminarHasil, Long nilai, String statusSemhas) {
        SeminarHasilModel seminarHasil = seminarHasilDb.findByIdSeminarHasil(idSeminarHasil)
                .orElse(null);
        if (seminarHasil != null) {
            seminarHasil.setNilai(nilai);
            seminarHasil.setStatusSemhas(statusSemhas);
            return seminarHasilDb.save(seminarHasil);
        }
        return null;
    }

    @Override
    public SeminarHasilModel findSemhasBySempro(SeminarProposalModel sempro) {
        Optional<SeminarHasilModel> seminarHasil = seminarHasilDb.findBySeminarProposal(sempro);
        if (seminarHasil.isPresent()) {
            return seminarHasil.get();
        } else
            return null;
    }
}
