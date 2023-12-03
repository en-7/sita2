package protensi.sita.service;

import protensi.sita.model.SeminarHasilModel;
import protensi.sita.model.SeminarProposalModel;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

public interface SeminarHasilService {

    void addSeminarHasil(SeminarHasilModel seminarHasil);

    SeminarHasilModel updateSemhas(SeminarHasilModel seminarHasil);

    List<SeminarHasilModel> findAllSeminarHasil();


    SeminarHasilModel findSemhasById(Long idSeminarHasil);

    List<SeminarHasilModel> findAllByPembimbing(Long pembimbingId);

    List<SeminarHasilModel> findAllByPenguji(Long pengujiId);

    SeminarHasilModel findSemhasBySempro(SeminarProposalModel sempro);

    List<SeminarHasilModel> findSemhasByStatusDokumen(String statusDokumen);

    SeminarHasilModel saveNilaiAndStatus(Long idSeminarHasil, Long nilai, String statusSeminarHasil);

}
