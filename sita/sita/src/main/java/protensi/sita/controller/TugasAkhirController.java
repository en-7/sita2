package protensi.sita.controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.util.StringUtils;

import protensi.sita.model.EnumRole;
import protensi.sita.model.MahasiswaModel;
import protensi.sita.model.SeminarHasilModel;
import protensi.sita.model.SeminarProposalModel;
import protensi.sita.model.TimelineModel;
import protensi.sita.model.TugasAkhirModel;
import protensi.sita.model.UgbModel;
import protensi.sita.model.UserModel;
import protensi.sita.security.UserDetailsServiceImpl;
import protensi.sita.service.BaseService;
import protensi.sita.service.MahasiswaServiceImpl;
import protensi.sita.service.SeminarProposalServiceImpl;
import protensi.sita.service.TimelineServiceImpl;
import protensi.sita.service.SeminarHasilServiceImpl;
import protensi.sita.service.TugasAkhirServiceImpl;
import protensi.sita.service.UgbServiceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class TugasAkhirController {

    @Qualifier("tugasAkhirServiceImpl")
    @Autowired
    private TugasAkhirServiceImpl tugasAkhirService;

    @Qualifier("ugbServiceImpl")
    @Autowired
    private UgbServiceImpl ugbService;

    @Qualifier("seminarProposalServiceImpl")
    @Autowired
    private SeminarProposalServiceImpl seminarProposalService;

    @Qualifier("seminarHasilServiceImpl")
    @Autowired
    private SeminarHasilServiceImpl seminarHasilService;

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Qualifier("mahasiswaServiceImpl")
    @Autowired
    private MahasiswaServiceImpl mahasiswaService;

    @Autowired
    private TimelineServiceImpl tlService;

    @Autowired
    public BaseService baseService;

    @GetMapping("/tugas-akhir/add")
    public String addSidangTAFormPage(Model model, Authentication authentication) {
        String namaUser = authentication.getName();
        UserModel user = userDetailsService.findByUsername(namaUser);
        if (user.getRoles().contains(EnumRole.MAHASISWA)) {
            MahasiswaModel mahasiswa = mahasiswaService.findMahasiswaByUsername(user.getUsername());
            UgbModel ugb = ugbService.findByIdMahasiswa(mahasiswa);
            TugasAkhirModel tugasAkhir = tugasAkhirService.findTAByUgb(ugb);
            if (ugb != null) {
                if (ugb.getStatusDokumen().equals("DIEVALUASI")) {
                    if (tugasAkhir != null) {
                        model.addAttribute("roleUser", baseService.getCurrentRole());
                        model.addAttribute("tugasAkhir", tugasAkhir);
                        return "tugasakhir/detail-ta-mahasiswa";
                    } else {
                        TimelineModel tl = tlService.checkDate();
                        LocalDate nowDate = LocalDate.now();
                        if(tl.getRegSidang() != null && tl.getRegSidang().equals(nowDate)){
                            tugasAkhir = new TugasAkhirModel();
                            model.addAttribute("roleUser", baseService.getCurrentRole());
                            model.addAttribute("tugasAkhir", tugasAkhir);
                            return "tugasakhir/add-ta-form";
                        }else{
                            return "tugasakhir/no-access-ta";
                        }

                    }
                } else {
                    model.addAttribute("roleUser", baseService.getCurrentRole());
                    return "tugasakhir/error-ta";
                }
            } else {
                model.addAttribute("roleUser", baseService.getCurrentRole());
                return "tugasakhir/error-ta";
            }

        } else {
            model.addAttribute("roleUser", baseService.getCurrentRole());
            return "tugasakhir/error-ta";
        }

    }

    @PostMapping("/tugas-akhir/add")
    public String addSidangTASubmitPage(@ModelAttribute TugasAkhirModel tugasAkhir,
            @RequestParam("risalahSemhasFile") MultipartFile risalahSemhasFile,
            @RequestParam("krsPengambilanTaFile") MultipartFile krsPengambilanTaFile,
            @RequestParam("suratPersetujuanSidangFile") MultipartFile suratPersetujuanSidangFile,
            @RequestParam("buktiNilaiKpFile") MultipartFile buktiNilaiKpFile,
            @RequestParam("buktiLembarAsistensiFile") MultipartFile buktiLembarAsistensiFile,
            @RequestParam("lembarKonversiNilaiFile") MultipartFile lembarKonversiNilaiFile,
            @RequestParam("perbaikanLaporanTaFile") MultipartFile perbaikanLaporanTaFile,
            @RequestParam("suratBebasLabFile") MultipartFile suratBebasLabFile,
            @RequestParam("kartuMengikutiSeminarFile") MultipartFile kartuMengikutiSeminarFile,
            @RequestParam("draftLaporanTAFile") MultipartFile draftLaporanTAFile,
            @RequestParam("buktiToeflFile") MultipartFile buktiToeflFile,
            @RequestParam("transkripNilaiTerbaruFile") MultipartFile transkripNilaiTerbaruFile,
            Model model, Authentication authentication) {
        try {
            // Mengambil data file yang diunggah
            byte[] risalahSemhasBytes = risalahSemhasFile.getBytes();
            byte[] krsPengambilanTaBytes = krsPengambilanTaFile.getBytes();
            byte[] suratPersetujuanSidangBytes = suratPersetujuanSidangFile.getBytes();
            byte[] buktiNilaiKpBytes = buktiNilaiKpFile.getBytes();
            byte[] buktiLembarAsistensiBytes = buktiLembarAsistensiFile.getBytes();
            byte[] lembarKonversiNilaiBytes = lembarKonversiNilaiFile.getBytes();
            byte[] perbaikanLaporanTaBytes = perbaikanLaporanTaFile.getBytes();
            byte[] suratBebasLabBytes = suratBebasLabFile.getBytes();
            byte[] kartuMengikutiSeminarBytes = kartuMengikutiSeminarFile.getBytes();
            byte[] draftLaporanTABytes = draftLaporanTAFile.getBytes();
            byte[] buktiToeflBytes = buktiToeflFile.getBytes();
            byte[] transkripNilaiTerbaruBytes = transkripNilaiTerbaruFile.getBytes();

            String namaFileRisalahSemhas = StringUtils.cleanPath(risalahSemhasFile.getOriginalFilename());
            String namaFileKrsPengambilanTa = StringUtils.cleanPath(krsPengambilanTaFile.getOriginalFilename());
            String namaFileSuratPersetujuanSidang = StringUtils
                    .cleanPath(suratPersetujuanSidangFile.getOriginalFilename());
            String namaFileBuktiNilaiKp = StringUtils.cleanPath(buktiNilaiKpFile.getOriginalFilename());
            String namaFileBuktiLembarAsistensi = StringUtils.cleanPath(buktiLembarAsistensiFile.getOriginalFilename());
            String namaFileLembarKonversiNilai = StringUtils.cleanPath(lembarKonversiNilaiFile.getOriginalFilename());
            String namaFilePerbaikanLaporanTa = StringUtils.cleanPath(perbaikanLaporanTaFile.getOriginalFilename());
            String namaFileSuratBebasLab = StringUtils.cleanPath(suratBebasLabFile.getOriginalFilename());
            String namaFileKartuMengikutiSeminar = StringUtils
                    .cleanPath(kartuMengikutiSeminarFile.getOriginalFilename());
            String namaFileDraftLaporanTA = StringUtils.cleanPath(draftLaporanTAFile.getOriginalFilename());
            String namaFileBuktiToefl = StringUtils.cleanPath(buktiToeflFile.getOriginalFilename());
            String namaFileTranskripNilaiTerbaru = StringUtils
                    .cleanPath(transkripNilaiTerbaruFile.getOriginalFilename());

            // Mengatur data file dalam entitas TugasAkhirModel
            tugasAkhir.setRisalahSemhas(risalahSemhasBytes);
            tugasAkhir.setKrsPengambilanTa(krsPengambilanTaBytes);
            tugasAkhir.setSuratPersetujuanSidang(suratPersetujuanSidangBytes);
            tugasAkhir.setBuktiNilaiKp(buktiNilaiKpBytes);
            tugasAkhir.setBuktiLembarAsistensi(buktiLembarAsistensiBytes);
            tugasAkhir.setLembarKonversiNilai(lembarKonversiNilaiBytes);
            tugasAkhir.setPerbaikanLaporanTa(perbaikanLaporanTaBytes);
            tugasAkhir.setSuratBebasLab(suratBebasLabBytes);
            tugasAkhir.setKartuMengikutiSeminar(kartuMengikutiSeminarBytes);
            tugasAkhir.setDraftLaporanTA(draftLaporanTABytes);
            tugasAkhir.setBuktiToefl(buktiToeflBytes);
            tugasAkhir.setTranskripNilaiTerbaru(transkripNilaiTerbaruBytes);

            tugasAkhir.setNameFileRisalahSemhas(namaFileRisalahSemhas);
            tugasAkhir.setNameFileKrsPengambilanTa(namaFileKrsPengambilanTa);
            tugasAkhir.setNameFileSuratPersetujuanSidang(namaFileSuratPersetujuanSidang);
            tugasAkhir.setNameFileBuktiNilaiKp(namaFileBuktiNilaiKp);
            tugasAkhir.setNameFileBuktiLembarAsistensi(namaFileBuktiLembarAsistensi);
            tugasAkhir.setNameFileLembarKonversiNilai(namaFileLembarKonversiNilai);
            tugasAkhir.setNameFilePerbaikanLaporanTa(namaFilePerbaikanLaporanTa);
            tugasAkhir.setNameFileSuratBebasLab(namaFileSuratBebasLab);
            tugasAkhir.setNameFileKartuMengikutiSeminar(namaFileKartuMengikutiSeminar);
            tugasAkhir.setNameFileDraftLaporanTA(namaFileDraftLaporanTA);
            tugasAkhir.setNameFileBuktiToefl(namaFileBuktiToefl);
            tugasAkhir.setNameFileTranskripNilaiTerbaru(namaFileTranskripNilaiTerbaru);

            String namaUser = authentication.getName();
            UserModel user = userDetailsService.findByUsername(namaUser);
            MahasiswaModel mahasiswa = mahasiswaService.findMahasiswaById(user.getIdUser());
            UgbModel ugb = ugbService.findByIdMahasiswa(mahasiswa);
            SeminarProposalModel sempro = seminarProposalService.findSemproById(user.getIdUser());
            SeminarHasilModel semhas = seminarHasilService.findSemhasById(user.getIdUser());

            tugasAkhir.setUgb(ugb);
            tugasAkhir.setSeminarProposal(sempro);
            tugasAkhir.setSeminarHasil(semhas);

            tugasAkhir.getUgb().getMahasiswa().setTahap("TUGASAKHIR");
            tugasAkhir.setStatusDokumen("TERDAFTAR");

            tugasAkhirService.addSidangTA(tugasAkhir);
            model.addAttribute("roleUser", baseService.getCurrentRole());
            model.addAttribute("tugasAkhir", tugasAkhir);
            return "tugasakhir/detail-ta-mahasiswa";
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }
    }

    @GetMapping("/tugas-akhir/viewall")
    public String listTugasAkhir(Model model, Authentication authentication) {
        String namaUser = authentication.getName();
        UserModel user = userDetailsService.findByUsername(namaUser);
        if (user.getRoles().contains(EnumRole.KOORDINATOR)) {
            List<TugasAkhirModel> listTugasAkhir = tugasAkhirService.findAllTugasAkhir();
            model.addAttribute("roleUser", baseService.getCurrentRole());
            model.addAttribute("listTugasAkhir", listTugasAkhir);
            return "tugasakhir/viewall-ta";
        } else if (user.getRoles().contains(EnumRole.PEMBIMBING) && user.getRoles().contains(EnumRole.PENGUJI)) {
            List<TugasAkhirModel> listTugasAkhirPembimbing = tugasAkhirService
                    .findAllByPembimbing(user.getIdUser());
            List<TugasAkhirModel> listTugasAkhirPenguji = tugasAkhirService.findAllByPenguji(user.getIdUser());
            List<TugasAkhirModel> listTugasAkhir = new ArrayList<TugasAkhirModel>();
            listTugasAkhir.addAll(listTugasAkhirPembimbing);
            listTugasAkhir.addAll(listTugasAkhirPenguji);
            model.addAttribute("roleUser", baseService.getCurrentRole());
            model.addAttribute("listTugasAkhir", listTugasAkhir);
            return "tugasakhir/viewall-ta-dosen";
        }
        model.addAttribute("roleUser", baseService.getCurrentRole());
        return "tugasakhir/error-ta";
    }

    @GetMapping("/tugas-akhir/filter")
    public String filterTugasAkhir(@RequestParam String status, Model model) {
        List<TugasAkhirModel> filteredTA = tugasAkhirService.findTAByStatusDokumen(status);
        model.addAttribute("listTugasAkhir", filteredTA);
        model.addAttribute("roleUser", baseService.getCurrentRole());
        return "tugasakhir/viewall-ta";
    }

    @PostMapping("/tugas-akhir/input-nilai/{idTugasAkhir}")
    public String inputNilai(@PathVariable Long idTugasAkhir, @RequestBody Map<String, Object> data, Model model) {
        TugasAkhirModel tugasAkhir = tugasAkhirService.findTugasAkhirById(idTugasAkhir);
        Long nilai = ((Integer) data.get("nilai")).longValue();
        if (nilai != null) {
            if (nilai < 40) {
                tugasAkhir.setNilai(nilai);
                tugasAkhir.setNilaiHuruf("E");
                tugasAkhir.setStatusTugasAkhir("TIDAK LULUS");
            } else if (nilai < 50) {
                tugasAkhir.setNilai(nilai);
                tugasAkhir.setNilaiHuruf("D");
                tugasAkhir.setStatusTugasAkhir("TIDAK LULUS");
            } else if (nilai < 55) {
                tugasAkhir.setNilai(nilai);
                tugasAkhir.setNilaiHuruf("C");
                tugasAkhir.setStatusTugasAkhir("TIDAK LULUS");
            } else if (nilai < 60) {
                tugasAkhir.setNilai(nilai);
                tugasAkhir.setNilaiHuruf("C+");
                tugasAkhir.setStatusTugasAkhir("TIDAK LULUS");
            } else if (nilai < 65) {
                tugasAkhir.setNilai(nilai);
                tugasAkhir.setNilaiHuruf("B-");
                tugasAkhir.setStatusTugasAkhir("TIDAK LULUS");
            } else if (nilai < 70) {
                tugasAkhir.setNilai(nilai);
                tugasAkhir.setNilaiHuruf("B");
                tugasAkhir.setStatusTugasAkhir("LULUS");
            } else if (nilai < 75) {
                tugasAkhir.setNilai(nilai);
                tugasAkhir.setNilaiHuruf("B+");
                tugasAkhir.setStatusTugasAkhir("LULUS");
            } else if (nilai < 80) {
                tugasAkhir.setNilai(nilai);
                tugasAkhir.setNilaiHuruf("A-");
                tugasAkhir.setStatusTugasAkhir("LULUS");
            } else if (nilai <= 100) {
                tugasAkhir.setNilai(nilai);
                tugasAkhir.setNilaiHuruf("A");
                tugasAkhir.setStatusTugasAkhir("LULUS");
            } else {
                throw new IllegalArgumentException("Invalid nilai: " + nilai);
            }

            tugasAkhir.setTanggalLulus(LocalDateTime.now());
            tugasAkhirService.updateTugasAkhir(tugasAkhir);
            model.addAttribute("tugasAkhir", tugasAkhir);
            return "tugasakhir/detail-ta-koordinator";
        } else {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }

    }

    @PostMapping("/tugas-akhir/update-nilai/{idTugasAkhir}")
    public String updateNilai(@PathVariable Long idTugasAkhir, @RequestBody Map<String, Object> data,
            Model model) {
        TugasAkhirModel tugasAkhir = tugasAkhirService.findTugasAkhirById(idTugasAkhir);
        Long nilai = ((Integer) data.get("nilai")).longValue();

        if (nilai != null) {
            if (nilai < 40) {
                tugasAkhir.setNilai(nilai);
                tugasAkhir.setNilaiHuruf("E");
                tugasAkhir.setStatusTugasAkhir("TIDAK LULUS");
            } else if (nilai < 50) {
                tugasAkhir.setNilai(nilai);
                tugasAkhir.setNilaiHuruf("D");
                tugasAkhir.setStatusTugasAkhir("TIDAK LULUS");
            } else if (nilai < 55) {
                tugasAkhir.setNilai(nilai);
                tugasAkhir.setNilaiHuruf("C");
                tugasAkhir.setStatusTugasAkhir("TIDAK LULUS");
            } else if (nilai < 60) {
                tugasAkhir.setNilai(nilai);
                tugasAkhir.setNilaiHuruf("C+");
                tugasAkhir.setStatusTugasAkhir("TIDAK LULUS");
            } else if (nilai < 65) {
                tugasAkhir.setNilai(nilai);
                tugasAkhir.setNilaiHuruf("B-");
                tugasAkhir.setStatusTugasAkhir("TIDAK LULUS");
            } else if (nilai < 70) {
                tugasAkhir.setNilai(nilai);
                tugasAkhir.setNilaiHuruf("B");
                tugasAkhir.setStatusTugasAkhir("LULUS");
            } else if (nilai < 75) {
                tugasAkhir.setNilai(nilai);
                tugasAkhir.setNilaiHuruf("B+");
                tugasAkhir.setStatusTugasAkhir("LULUS");
            } else if (nilai < 80) {
                tugasAkhir.setNilai(nilai);
                tugasAkhir.setNilaiHuruf("A-");
                tugasAkhir.setStatusTugasAkhir("LULUS");
            } else if (nilai <= 100) {
                tugasAkhir.setNilai(nilai);
                tugasAkhir.setNilaiHuruf("A");
                tugasAkhir.setStatusTugasAkhir("LULUS");
            } else {
                throw new IllegalArgumentException("Invalid nilai: " + nilai);
            }

            tugasAkhir.setTanggalLulus(LocalDateTime.now());
            tugasAkhirService.updateTugasAkhir(tugasAkhir);
            model.addAttribute("tugasAkhir", tugasAkhir);
            return "tugasakhir/detail-ta-koordinator";
        } else {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }
    }

    @GetMapping("/tugas-akhir/detail/{idTugasAkhir}")
    public String viewDetailTugasAkhirPage(@PathVariable Long idTugasAkhir, Model model,
            Authentication authentication) {
        String namaUser = authentication.getName();
        UserModel user = userDetailsService.findByUsername(namaUser);
        TugasAkhirModel tugasAkhir = tugasAkhirService.findTugasAkhirById(idTugasAkhir);
        model.addAttribute("tugasAkhir", tugasAkhir);
        model.addAttribute("roleUser", baseService.getCurrentRole());
        if (user.getRoles().contains(EnumRole.KOORDINATOR)) {
            return "tugasakhir/detail-ta-koordinator";
        } else if (user.getRoles().contains(EnumRole.PEMBIMBING) && user.getRoles().contains(EnumRole.PENGUJI)) {
            return "tugasakhir/detail-ta-dosen";
        } else {
            return "tugasakhir/detail-ta-mahasiswa";
        }
    }

    @GetMapping("/tugas-akhir/approve/{idTugasAkhir}")
    public String approveTugasAkhir(@PathVariable Long idTugasAkhir, Model model) {
        try {
            TugasAkhirModel tugasAkhir = tugasAkhirService.findTugasAkhirById(idTugasAkhir);
            tugasAkhir.setStatusDokumen("DISETUJUI");
            tugasAkhirService.updateTugasAkhir(tugasAkhir);
            model.addAttribute("roleUser", baseService.getCurrentRole());
            model.addAttribute("tugasAkhir", tugasAkhir);
            return "tugasakhir/detail-ta-koordinator";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }
    }

    @PostMapping("/tugas-akhir/deny/{idTugasAkhir}")
    public String denyTugasAkhir(@PathVariable Long idTugasAkhir, @RequestParam("catatan") String catatan,
            Model model) {
        try {
            TugasAkhirModel tugasAkhir = tugasAkhirService.findTugasAkhirById(idTugasAkhir);
            tugasAkhir.setStatusDokumen("DITOLAK");
            tugasAkhir.setCatatan(catatan);
            tugasAkhirService.updateTugasAkhir(tugasAkhir);
            model.addAttribute("roleUser", baseService.getCurrentRole());
            model.addAttribute("tugasAkhir", tugasAkhir);
            return "tugasakhir/detail-ta-koordinator";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }
    }

    @GetMapping("/tugas-akhir/update/{idTugasAkhir}")
    public String updateTAFormPage(@PathVariable Long idTugasAkhir, Model model) {
        TugasAkhirModel tugasAkhir = tugasAkhirService.findTugasAkhirById(idTugasAkhir);
        model.addAttribute("roleUser", baseService.getCurrentRole());
        model.addAttribute("tugasAkhir", tugasAkhir);
        return "tugasakhir/update-ta-form";
    }

    @PostMapping("/tugas-akhir/update/{idTugasAkhir}")
    public String updateSidangTASubmitPage(
            @PathVariable Long idTugasAkhir,
            @RequestParam("risalahSemhasFile") MultipartFile risalahSemhasFile,
            @RequestParam("krsPengambilanTaFile") MultipartFile krsPengambilanTaFile,
            @RequestParam("suratPersetujuanSidangFile") MultipartFile suratPersetujuanSidangFile,
            @RequestParam("buktiNilaiKpFile") MultipartFile buktiNilaiKpFile,
            @RequestParam("buktiLembarAsistensiFile") MultipartFile buktiLembarAsistensiFile,
            @RequestParam("lembarKonversiNilaiFile") MultipartFile lembarKonversiNilaiFile,
            @RequestParam("perbaikanLaporanTaFile") MultipartFile perbaikanLaporanTaFile,
            @RequestParam("suratBebasLabFile") MultipartFile suratBebasLabFile,
            @RequestParam("kartuMengikutiSeminarFile") MultipartFile kartuMengikutiSeminarFile,
            @RequestParam("draftLaporanTAFile") MultipartFile draftLaporanTAFile,
            @RequestParam("buktiToeflFile") MultipartFile buktiToeflFile,
            @RequestParam("transkripNilaiTerbaruFile") MultipartFile transkripNilaiTerbaruFile,
            Model model, Authentication authentication) {
        try {
            // Mengambil data file yang diunggah
            byte[] risalahSemhasBytes = risalahSemhasFile.getBytes();
            byte[] krsPengambilanTaBytes = krsPengambilanTaFile.getBytes();
            byte[] suratPersetujuanSidangBytes = suratPersetujuanSidangFile.getBytes();
            byte[] buktiNilaiKpBytes = buktiNilaiKpFile.getBytes();
            byte[] buktiLembarAsistensiBytes = buktiLembarAsistensiFile.getBytes();
            byte[] lembarKonversiNilaiBytes = lembarKonversiNilaiFile.getBytes();
            byte[] perbaikanLaporanTaBytes = perbaikanLaporanTaFile.getBytes();
            byte[] suratBebasLabBytes = suratBebasLabFile.getBytes();
            byte[] kartuMengikutiSeminarBytes = kartuMengikutiSeminarFile.getBytes();
            byte[] draftLaporanTABytes = draftLaporanTAFile.getBytes();
            byte[] buktiToeflBytes = buktiToeflFile.getBytes();
            byte[] transkripNilaiTerbaruBytes = transkripNilaiTerbaruFile.getBytes();

            TugasAkhirModel tugasAkhir = tugasAkhirService.findTugasAkhirById(idTugasAkhir);
            if (!risalahSemhasFile.isEmpty()) {
                String namaFileRisalahSemhas = StringUtils.cleanPath(risalahSemhasFile.getOriginalFilename());
                tugasAkhir.setNameFileRisalahSemhas(namaFileRisalahSemhas);
                tugasAkhir.setRisalahSemhas(risalahSemhasBytes);
            }
            if (!krsPengambilanTaFile.isEmpty()) {
                String namaFileKrsPengambilanTa = StringUtils.cleanPath(krsPengambilanTaFile.getOriginalFilename());
                tugasAkhir.setNameFileKrsPengambilanTa(namaFileKrsPengambilanTa);
                tugasAkhir.setKrsPengambilanTa(krsPengambilanTaBytes);
            }
            if (!suratPersetujuanSidangFile.isEmpty()) {
                String namaFileSuratPersetujuanSidang = StringUtils
                        .cleanPath(suratPersetujuanSidangFile.getOriginalFilename());
                tugasAkhir.setNameFileSuratPersetujuanSidang(namaFileSuratPersetujuanSidang);
                tugasAkhir.setSuratPersetujuanSidang(suratPersetujuanSidangBytes);
            }
            if (!buktiNilaiKpFile.isEmpty()) {
                String namaFileBuktiNilaiKp = StringUtils.cleanPath(buktiNilaiKpFile.getOriginalFilename());
                tugasAkhir.setNameFileBuktiNilaiKp(namaFileBuktiNilaiKp);
                tugasAkhir.setBuktiNilaiKp(buktiNilaiKpBytes);
            }
            if (!buktiLembarAsistensiFile.isEmpty()) {
                String namaFileBuktiLembarAsistensi = StringUtils
                        .cleanPath(buktiLembarAsistensiFile.getOriginalFilename());
                tugasAkhir.setNameFileBuktiLembarAsistensi(namaFileBuktiLembarAsistensi);
                tugasAkhir.setBuktiLembarAsistensi(buktiLembarAsistensiBytes);
            }
            if (!lembarKonversiNilaiFile.isEmpty()) {
                String namaFileLembarKonversiNilai = StringUtils
                        .cleanPath(lembarKonversiNilaiFile.getOriginalFilename());
                tugasAkhir.setNameFileLembarKonversiNilai(namaFileLembarKonversiNilai);
                tugasAkhir.setLembarKonversiNilai(lembarKonversiNilaiBytes);
            }
            if (!perbaikanLaporanTaFile.isEmpty()) {
                String namaFilePerbaikanLaporanTa = StringUtils.cleanPath(perbaikanLaporanTaFile.getOriginalFilename());
                tugasAkhir.setNameFilePerbaikanLaporanTa(namaFilePerbaikanLaporanTa);
                tugasAkhir.setPerbaikanLaporanTa(perbaikanLaporanTaBytes);
            }
            if (!suratBebasLabFile.isEmpty()) {
                String namaFileSuratBebasLab = StringUtils.cleanPath(suratBebasLabFile.getOriginalFilename());
                tugasAkhir.setNameFileSuratBebasLab(namaFileSuratBebasLab);
                tugasAkhir.setSuratBebasLab(suratBebasLabBytes);
            }
            if (!kartuMengikutiSeminarFile.isEmpty()) {
                String namaFileKartuMengikutiSeminar = StringUtils
                        .cleanPath(kartuMengikutiSeminarFile.getOriginalFilename());
                tugasAkhir.setNameFileKartuMengikutiSeminar(namaFileKartuMengikutiSeminar);
                tugasAkhir.setKartuMengikutiSeminar(kartuMengikutiSeminarBytes);
            }
            if (!draftLaporanTAFile.isEmpty()) {
                String namaFileDraftLaporanTA = StringUtils.cleanPath(draftLaporanTAFile.getOriginalFilename());
                tugasAkhir.setNameFileDraftLaporanTA(namaFileDraftLaporanTA);
                tugasAkhir.setDraftLaporanTA(draftLaporanTABytes);
            }
            if (!buktiToeflFile.isEmpty()) {
                String namaFileBuktiToefl = StringUtils.cleanPath(buktiToeflFile.getOriginalFilename());
                tugasAkhir.setNameFileBuktiToefl(namaFileBuktiToefl);
                tugasAkhir.setBuktiToefl(buktiToeflBytes);
            }
            if (!transkripNilaiTerbaruFile.isEmpty()) {
                String namaFileTranskripNilaiTerbaru = StringUtils
                        .cleanPath(transkripNilaiTerbaruFile.getOriginalFilename());
                tugasAkhir.setNameFileTranskripNilaiTerbaru(namaFileTranskripNilaiTerbaru);
                tugasAkhir.setTranskripNilaiTerbaru(transkripNilaiTerbaruBytes);
            }

            tugasAkhir.setCatatan(null);
            tugasAkhir.setStatusDokumen("TERDAFTAR");
            tugasAkhirService.updateTugasAkhir(tugasAkhir);

            model.addAttribute("roleUser", baseService.getCurrentRole());
            model.addAttribute("tugasAkhir", tugasAkhir);
            return "tugasakhir/detail-ta-mahasiswa";
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }
    }

    @GetMapping("/tugas-akhir/downloadFile")
    public void downloadFile(@RequestParam("type") String type,
            @RequestParam("id") Long id,
            HttpServletResponse response) {
        try {
            TugasAkhirModel retrievedTugasAkhir = tugasAkhirService.findTugasAkhirById(id);
            response.setContentType("application/ocetet-stream");
            String headerKey = "Content-Disposition";

            if (type.equals("RISALAH SEMINAR HASIL")) {
                String headerValue = "attachment; filename=" + retrievedTugasAkhir.getNameFileRisalahSemhas();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedTugasAkhir.getRisalahSemhas());
                outputStream.close();
            } else if (type.equals("KRS PENGAMBILAN TA")) {
                String headerValue = "attachment; filename=" + retrievedTugasAkhir.getNameFileKrsPengambilanTa();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedTugasAkhir.getKrsPengambilanTa());
                outputStream.close();
            } else if (type.equals("SURAT PERSETUJUAN SIDANG")) {
                String headerValue = "attachment; filename=" + retrievedTugasAkhir.getNameFileSuratPersetujuanSidang();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedTugasAkhir.getSuratPersetujuanSidang());
                outputStream.close();
            } else if (type.equals("BUKTI NILAI KERJA PRAKTIK")) {
                String headerValue = "attachment; filename=" + retrievedTugasAkhir.getNameFileBuktiNilaiKp();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedTugasAkhir.getBuktiNilaiKp());
                outputStream.close();
            } else if (type.equals("BUKTI LEMBAR ASISTENSI")) {
                String headerValue = "attachment; filename=" + retrievedTugasAkhir.getNameFileBuktiLembarAsistensi();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedTugasAkhir.getBuktiLembarAsistensi());
                outputStream.close();
            } else if (type.equals("LEMBAR KONVERSI NILAI")) {
                String headerValue = "attachment; filename=" + retrievedTugasAkhir.getNameFileLembarKonversiNilai();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedTugasAkhir.getLembarKonversiNilai());
                outputStream.close();
            } else if (type.equals("PERBAIKAN LAPORAN TA")) {
                String headerValue = "attachment; filename=" + retrievedTugasAkhir.getNameFilePerbaikanLaporanTa();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedTugasAkhir.getPerbaikanLaporanTa());
                outputStream.close();
            } else if (type.equals("SURAT BEBAS LABORATORIUM")) {
                String headerValue = "attachment; filename=" + retrievedTugasAkhir.getNameFileSuratBebasLab();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedTugasAkhir.getSuratBebasLab());
                outputStream.close();
            } else if (type.equals("KARTU MENGIKUTI SEMINAR")) {
                String headerValue = "attachment; filename=" + retrievedTugasAkhir.getNameFileKartuMengikutiSeminar();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedTugasAkhir.getKartuMengikutiSeminar());
                outputStream.close();
            } else if (type.equals("DRAFT LAPORAN TA")) {
                String headerValue = "attachment; filename=" + retrievedTugasAkhir.getNameFileDraftLaporanTA();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedTugasAkhir.getDraftLaporanTA());
                outputStream.close();
            } else if (type.equals("BUKTI TOEFL")) {
                String headerValue = "attachment; filename=" + retrievedTugasAkhir.getNameFileBuktiToefl();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedTugasAkhir.getBuktiToefl());
                outputStream.close();
            } else if (type.equals("MERGE TRANSKRIP NILAI TERBARU")) {
                String headerValue = "attachment; filename=" + retrievedTugasAkhir.getNameFileTranskripNilaiTerbaru();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedTugasAkhir.getTranskripNilaiTerbaru());
                outputStream.close();
            }

        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }

    }

}