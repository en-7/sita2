package protensi.sita.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import protensi.sita.model.JadwalSidangModel;
import protensi.sita.model.SeminarHasilModel;
import protensi.sita.model.SeminarProposalModel;
import protensi.sita.model.TugasAkhirModel;
import protensi.sita.repository.SeminarHasilDb;
import protensi.sita.repository.SeminarProposalDb;
import protensi.sita.repository.TugasAkhirDb;
import protensi.sita.repository.JadwalSeminarSidangDb;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JadwalSidangSeminarServiceImpl implements JadwalSidangSeminarService {

    @Autowired
    JadwalSeminarSidangDb jadwalSeminarSidangDb;

    @Autowired
    SeminarProposalDb seminarProposalDb;

    @Autowired
    SeminarHasilDb seminarHasilDb;

    @Autowired
    TugasAkhirDb tugasAkhirDb;

    @Override
    public List<JadwalSidangModel> getListJadwalSidang() {
        return jadwalSeminarSidangDb.findAll();
    }

    @Override
    public JadwalSidangModel getJadwalSidangById(long id) {
        Optional<JadwalSidangModel> jadwalSidangid = jadwalSeminarSidangDb.findById(id);
        if (jadwalSidangid.isPresent()) {
            return jadwalSidangid.get();
        }
        return null;
    }

    @Override
    public JadwalSidangModel setJadwalSidang(JadwalSidangModel jadwalSidangSeminar) {
        jadwalSeminarSidangDb.save(jadwalSidangSeminar);
        return jadwalSidangSeminar;
    }

    @Override
    public void deletesJadwalSidangSeminar(Long id) {
        jadwalSeminarSidangDb.deleteById(id);
    }

    @Override
    public void addJadwalSidangSeminar(JadwalSidangModel jadwalSidangSeminar) {
        // System.out.println(jadwalSidangSeminar.getTanggalSempro());
        // System.out.println(jadwalSidangSeminar.getSeminarProposal());
        SeminarProposalModel semprobyId = seminarProposalDb
                .findByIdSeminarProposal(jadwalSidangSeminar.getSeminarProposal().getIdSeminarProposal()).get();
        semprobyId.setJadwalSidang(jadwalSidangSeminar);
        seminarProposalDb.save(semprobyId);
        // System.out.println(jadwalSidangSeminar.getUgb());
        jadwalSeminarSidangDb.save(jadwalSidangSeminar);
    }

    @Override
    public void addJadwalSemhas(JadwalSidangModel jadwalSemhas) {
        SeminarHasilModel semhasbyId = seminarHasilDb.findById(jadwalSemhas.getSeminarHasil().getIdSeminarHasil())
                .get();
        semhasbyId.setJadwalSidang(jadwalSemhas);
        seminarHasilDb.save(semhasbyId);
        jadwalSeminarSidangDb.save(jadwalSemhas);
    }

    @Override
    public void addJadwalSidangTa(JadwalSidangModel jadwalSidangTa) {
        TugasAkhirModel taById = tugasAkhirDb.findById(jadwalSidangTa.getTugasAkhir().getIdTugasAkhir()).get();
        taById.setJadwalSidang(jadwalSidangTa);
        tugasAkhirDb.save(taById);
        jadwalSeminarSidangDb.save(jadwalSidangTa);
    }

    @Override
    public void saverid(JadwalSidangModel jadwalSidangModel) {

    }
}
