package protensi.sita.controller;

import protensi.sita.model.EnumRole;
import protensi.sita.model.MahasiswaModel;
import protensi.sita.model.SeminarProposalModel;
import protensi.sita.model.TimelineModel;
import protensi.sita.model.UgbModel;
import protensi.sita.model.UserModel;
import protensi.sita.security.UserDetailsServiceImpl;
import protensi.sita.service.BaseService;
import protensi.sita.service.MahasiswaServiceImpl;
import protensi.sita.service.SeminarProposalServiceImpl;
import protensi.sita.service.TimelineServiceImpl;
import protensi.sita.service.UgbServiceImpl;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;

@Resource
@Controller
@RequestMapping("/seminar-proposal")
public class SeminarProposalController {
    @Qualifier("seminarProposalServiceImpl")
    @Autowired
    private SeminarProposalServiceImpl seminarProposalService;

    @Autowired
    private TimelineServiceImpl tlService;

    @Qualifier("ugbServiceImpl")
    @Autowired
    private UgbServiceImpl ugbService;

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Qualifier("mahasiswaServiceImpl")
    @Autowired
    private MahasiswaServiceImpl mahasiswaService;

    @Autowired
    public BaseService baseService;

    @GetMapping("/add")
    public String addSemproFormPage(Model model, Authentication authentication) {
        String namaUser = authentication.getName();
        UserModel user = userDetailsService.findByUsername(namaUser);
        if (user.getRoles().contains(EnumRole.MAHASISWA)) {
            MahasiswaModel mahasiswa = mahasiswaService.findMahasiswaByUsername(user.getUsername());
            UgbModel ugb = ugbService.findByIdMahasiswa(mahasiswa);
            SeminarProposalModel seminarProposal = seminarProposalService.findSemproByUgb(ugb);
            if (ugb != null) {
                if (ugb.getStatusDokumen().equals("DIEVALUASI")) {
                    if (seminarProposal != null) {
                        model.addAttribute("seminarProposal", seminarProposal);
                        return "sempro/detail-sempro-mahasiswa";
                    } else {
                        TimelineModel tl = tlService.checkDate();
                        LocalDate nowDate = LocalDate.now();

                        if (tl.getRegSempro() != null && tl.getRegSempro().equals(nowDate)) {
                            seminarProposal = new SeminarProposalModel();
                            model.addAttribute("seminarProposal", seminarProposal);
                            return "sempro/add-sempro-form";
                        } else {
                            return "sempro/no-access-sempro";
                        }
                    }
                } else {
                    model.addAttribute("pesan",
                            "Tidak dapat mendaftar seminar proposal, karena UGB Anda belum dievaluasi");
                    return "sempro/error-sempro";
                }
            } else {
                model.addAttribute("pesan", "Tidak dapat mendaftar seminar proposal, karena Anda belum mendaftar UGB");
                return "sempro/error-sempro";
            }
        } else {
            model.addAttribute("pesan", "Tidak dapat mendaftar seminar proposal, karena Anda bukan Mahasiswa");
            return "sempro/error-sempro";
        }

    }

    @PostMapping("/add")
    public String addSemproSubmitPage(@ModelAttribute SeminarProposalModel seminarProposal,
            @RequestParam("draftProposalTaFile") MultipartFile draftProposalTaFile,
            @RequestParam("buktiKrsFile") MultipartFile buktiKrsFile,
            @RequestParam("persetujuanPembimbingFile") MultipartFile persetujuanPembimbingFile,
            Model model, Authentication authentication) {
        try {
            byte[] draftProposalTaBytes = draftProposalTaFile.getBytes();
            byte[] buktiKrsBytes = buktiKrsFile.getBytes();
            byte[] persetujuanPembimbingBytes = persetujuanPembimbingFile.getBytes();
            String namaFiledraftProposalTa = StringUtils.cleanPath(draftProposalTaFile.getOriginalFilename());
            String namaFileBuktiKrs = StringUtils.cleanPath(buktiKrsFile.getOriginalFilename());
            String namaFilePersetujuanPembimbing = StringUtils
                    .cleanPath(persetujuanPembimbingFile.getOriginalFilename());

            seminarProposal.setNameFileBuktiKrs(namaFileBuktiKrs);
            seminarProposal.setNameFilePersetujuanPembimbing(namaFilePersetujuanPembimbing);
            seminarProposal.setNameFileDraftProposalTa(namaFiledraftProposalTa);
            seminarProposal.setDraftProposalTa(draftProposalTaBytes);
            seminarProposal.setBuktiKrs(buktiKrsBytes);
            seminarProposal.setPersetujuanPembimbing(persetujuanPembimbingBytes);
            // Mengatur idUgb dalam entitas SeminarProposalModel
            String namaUser = authentication.getName();
            UserModel user = userDetailsService.findByUsername(namaUser);
            MahasiswaModel mahasiswa = mahasiswaService.findMahasiswaById(user.getIdUser());
            UgbModel ugb = ugbService.findByIdMahasiswa(mahasiswa);
            seminarProposal.setUgb(ugb);
            // Mengatur tahap mahasiswa menjadi "SEMPRO", dan statusDokumen menjadI
            // "TERDAFTAR"
            seminarProposal.getUgb().getMahasiswa().setTahap("SEMPRO");
            seminarProposal.setStatusDokumen("TERDAFTAR");

            seminarProposalService.addSempro(seminarProposal);
            model.addAttribute("seminarProposal", seminarProposal);
            return "sempro/detail-sempro-mahasiswa";
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }
    }

    @GetMapping("/update/{idSeminarProposal}")
    public String updateSemproFormPage(@PathVariable Long idSeminarProposal, Model model) {
        SeminarProposalModel seminarProposal = seminarProposalService.findSemproById(idSeminarProposal);
        model.addAttribute("seminarProposal", seminarProposal);
        return "sempro/update-sempro-form";
    }

    @PostMapping("/update/{idSeminarProposal}")
    public String updateSemproSubmitPage(
            @PathVariable Long idSeminarProposal,
            @RequestParam("draftProposalTaFile") MultipartFile draftProposalTaFile,
            @RequestParam("buktiKrsFile") MultipartFile buktiKrsFile,
            @RequestParam("persetujuanPembimbingFile") MultipartFile persetujuanPembimbingFile,
            Model model, Authentication authentication) {
        try {
            byte[] draftProposalTaBytes = draftProposalTaFile.getBytes();
            byte[] buktiKrsBytes = buktiKrsFile.getBytes();
            byte[] persetujuanPembimbingBytes = persetujuanPembimbingFile.getBytes();

            SeminarProposalModel seminarProposal = seminarProposalService.findSemproById(idSeminarProposal);
            if (!draftProposalTaFile.isEmpty()) {
                String namaFiledraftProposalTa = StringUtils.cleanPath(draftProposalTaFile.getOriginalFilename());
                seminarProposal.setNameFileDraftProposalTa(namaFiledraftProposalTa);
                seminarProposal.setDraftProposalTa(draftProposalTaBytes);
            }
            if (!buktiKrsFile.isEmpty()) {
                String namaFileBuktiKrs = StringUtils.cleanPath(buktiKrsFile.getOriginalFilename());
                seminarProposal.setNameFileBuktiKrs(namaFileBuktiKrs);
                seminarProposal.setBuktiKrs(buktiKrsBytes);
            }
            if (!persetujuanPembimbingFile.isEmpty()) {
                String namaFilePersetujuanPembimbing = StringUtils
                        .cleanPath(persetujuanPembimbingFile.getOriginalFilename());
                seminarProposal.setNameFilePersetujuanPembimbing(namaFilePersetujuanPembimbing);
                seminarProposal.setPersetujuanPembimbing(persetujuanPembimbingBytes);
            }

            seminarProposal.setCatatan(null);
            seminarProposal.setStatusDokumen("TERDAFTAR");
            seminarProposalService.updateSempro(seminarProposal);

            model.addAttribute("seminarProposal", seminarProposal);
            return "sempro/detail-sempro-mahasiswa";
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }
    }

    @GetMapping("/viewall")
    public String listSempro(Model model, Authentication authentication) {
        String namaUser = authentication.getName();
        UserModel user = userDetailsService.findByUsername(namaUser);
        if (user.getRoles().contains(EnumRole.KOORDINATOR)) {
            List<SeminarProposalModel> listSempro = seminarProposalService.findAllSempro();
            model.addAttribute("listSempro", listSempro);
            return "sempro/viewall-sempro";
        } else if (user.getRoles().contains(EnumRole.PEMBIMBING) && user.getRoles().contains(EnumRole.PENGUJI)) {
            List<SeminarProposalModel> listSemproPembimbing = seminarProposalService
                    .findAllByPembimbing(user.getIdUser());
            List<SeminarProposalModel> listSemproPenguji = seminarProposalService.findAllByPenguji(user.getIdUser());
            List<SeminarProposalModel> listSempro = new ArrayList<SeminarProposalModel>();
            listSempro.addAll(listSemproPembimbing);
            listSempro.addAll(listSemproPenguji);
            model.addAttribute("listSempro", listSempro);
            return "sempro/viewall-sempro-dosen";
        }
        model.addAttribute("pesan",
                "Tidak dapat melihat daftar peserta seminar proposal karena Anda bukan Koordinator atau Dosen");
        return "sempro/error-sempro";
    }

    @GetMapping("/filter")
    public String filterSeminarProposals(@RequestParam String status, Model model) {
        List<SeminarProposalModel> filteredProposals = seminarProposalService.findSemproByStatusDokumen(status);
        model.addAttribute("listSempro", filteredProposals);
        return "sempro/viewall-sempro";
    }

    @PostMapping("/input-nilai/{idSeminarProposal}")
    public String inputNilai(@PathVariable Long idSeminarProposal, @RequestBody Map<String, Object> data, Model model) {
        SeminarProposalModel seminarProposal = seminarProposalService.findSemproById(idSeminarProposal);
        Long nilai = ((Integer) data.get("nilai")).longValue();
        if (nilai != null) {
            if (nilai < 40) {
                seminarProposal.setNilai(nilai);
                seminarProposal.setNilaiHuruf("E");
                seminarProposal.setStatusSeminarProposal("TIDAK LULUS");
            } else if (nilai < 50) {
                seminarProposal.setNilai(nilai);
                seminarProposal.setNilaiHuruf("D");
                seminarProposal.setStatusSeminarProposal("TIDAK LULUS");
            } else if (nilai < 55) {
                seminarProposal.setNilai(nilai);
                seminarProposal.setNilaiHuruf("C");
                seminarProposal.setStatusSeminarProposal("LULUS");
            } else if (nilai < 60) {
                seminarProposal.setNilai(nilai);
                seminarProposal.setNilaiHuruf("C+");
                seminarProposal.setStatusSeminarProposal("LULUS");
            } else if (nilai < 65) {
                seminarProposal.setNilai(nilai);
                seminarProposal.setNilaiHuruf("B-");
                seminarProposal.setStatusSeminarProposal("LULUS");
            } else if (nilai < 70) {
                seminarProposal.setNilai(nilai);
                seminarProposal.setNilaiHuruf("B");
                seminarProposal.setStatusSeminarProposal("LULUS");
            } else if (nilai < 75) {
                seminarProposal.setNilai(nilai);
                seminarProposal.setNilaiHuruf("B+");
                seminarProposal.setStatusSeminarProposal("LULUS");
            } else if (nilai < 80) {
                seminarProposal.setNilai(nilai);
                seminarProposal.setNilaiHuruf("A-");
                seminarProposal.setStatusSeminarProposal("LULUS");
            } else if (nilai <= 100) {
                seminarProposal.setNilai(nilai);
                seminarProposal.setNilaiHuruf("A");
                seminarProposal.setStatusSeminarProposal("LULUS");
            } else {
                throw new IllegalArgumentException("Invalid nilai: " + nilai);
            }
            seminarProposal.setTanggalLulus(LocalDateTime.now());
            seminarProposalService.updateSempro(seminarProposal);
            model.addAttribute("seminarProposal", seminarProposal);
            return "sempro/detail-sempro-koordinator";
        } else {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }

    }

    @PostMapping("/update-nilai/{idSeminarProposal}")
    public String updateNilai(@PathVariable Long idSeminarProposal, @RequestBody Map<String, Object> data,
            Model model) {
        SeminarProposalModel seminarProposal = seminarProposalService.findSemproById(idSeminarProposal);
        Long nilai = ((Integer) data.get("nilai")).longValue();

        if (nilai != null) {
            if (nilai < 40) {
                seminarProposal.setNilai(nilai);
                seminarProposal.setNilaiHuruf("E");
                seminarProposal.setStatusSeminarProposal("TIDAK LULUS");
            } else if (nilai < 50) {
                seminarProposal.setNilai(nilai);
                seminarProposal.setNilaiHuruf("D");
                seminarProposal.setStatusSeminarProposal("TIDAK LULUS");
            } else if (nilai < 55) {
                seminarProposal.setNilai(nilai);
                seminarProposal.setNilaiHuruf("C");
                seminarProposal.setStatusSeminarProposal("LULUS");
            } else if (nilai < 60) {
                seminarProposal.setNilai(nilai);
                seminarProposal.setNilaiHuruf("C+");
                seminarProposal.setStatusSeminarProposal("LULUS");
            } else if (nilai < 65) {
                seminarProposal.setNilai(nilai);
                seminarProposal.setNilaiHuruf("B-");
                seminarProposal.setStatusSeminarProposal("LULUS");
            } else if (nilai < 70) {
                seminarProposal.setNilai(nilai);
                seminarProposal.setNilaiHuruf("B");
                seminarProposal.setStatusSeminarProposal("LULUS");
            } else if (nilai < 75) {
                seminarProposal.setNilai(nilai);
                seminarProposal.setNilaiHuruf("B+");
                seminarProposal.setStatusSeminarProposal("LULUS");
            } else if (nilai < 80) {
                seminarProposal.setNilai(nilai);
                seminarProposal.setNilaiHuruf("A-");
                seminarProposal.setStatusSeminarProposal("LULUS");
            } else if (nilai <= 100) {
                seminarProposal.setNilai(nilai);
                seminarProposal.setNilaiHuruf("A");
                seminarProposal.setStatusSeminarProposal("LULUS");
            } else {
                throw new IllegalArgumentException("Invalid nilai: " + nilai);
            }
            seminarProposal.setTanggalLulus(LocalDateTime.now());
            seminarProposalService.updateSempro(seminarProposal);
            model.addAttribute("seminarProposal", seminarProposal);
            return "sempro/detail-sempro-koordinator";
        } else {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }
    }

    @GetMapping("/detail/{idSeminarProposal}")
    public String viewDetailSemproPage(@PathVariable Long idSeminarProposal, Model model,
            Authentication authentication) {
        String namaUser = authentication.getName();
        UserModel user = userDetailsService.findByUsername(namaUser);
        SeminarProposalModel seminarProposal = seminarProposalService.findSemproById(idSeminarProposal);
        model.addAttribute("seminarProposal", seminarProposal);
        if (user.getRoles().contains(EnumRole.KOORDINATOR)) {
            return "sempro/detail-sempro-koordinator";
        } else if (user.getRoles().contains(EnumRole.PEMBIMBING) && user.getRoles().contains(EnumRole.PENGUJI)) {
            return "sempro/detail-sempro-dosen";
        } else {
            return "sempro/detail-sempro-mahasiswa";
        }
    }

    @GetMapping("/approve/{idSeminarProposal}")
    public String approveSeminarProposal(@PathVariable Long idSeminarProposal, Model model) {
        try {
            SeminarProposalModel seminarProposal = seminarProposalService.findSemproById(idSeminarProposal);
            seminarProposal.setStatusDokumen("DISETUJUI");
            seminarProposalService.updateSempro(seminarProposal);
            model.addAttribute("seminarProposal", seminarProposal);
            return "sempro/detail-sempro-koordinator";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }
    }

    @PostMapping("/deny/{idSeminarProposal}")
    public String denySeminarProposal(@PathVariable Long idSeminarProposal, @RequestParam("catatan") String catatan,
            Model model) {
        try {
            SeminarProposalModel seminarProposal = seminarProposalService.findSemproById(idSeminarProposal);
            seminarProposal.setStatusDokumen("DITOLAK");
            seminarProposal.setCatatan(catatan);
            seminarProposalService.updateSempro(seminarProposal);
            model.addAttribute("seminarProposal", seminarProposal);
            return "sempro/detail-sempro-koordinator";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }
    }

    @GetMapping("/downloadFile")
    public void downloadFile(@RequestParam("type") String type,
            @RequestParam("id") Long id,
            HttpServletResponse response) {
        try {
            SeminarProposalModel retrievedSempro = seminarProposalService.findSemproById(id);
            response.setContentType("application/ocetet-stream");
            String headerKey = "Content-Disposition";

            if (type.equals("FILE BUKTI KRS")) {
                String headerValue = "attachment; filename=" + retrievedSempro.getNameFileBuktiKrs();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedSempro.getBuktiKrs());
                outputStream.close();
            } else if (type.equals("FILE DRAFT PROPOSAL TA")) {
                String headerValue = "attachment; filename=" + retrievedSempro.getNameFileDraftProposalTa();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedSempro.getDraftProposalTa());
                outputStream.close();
            } else if (type.equals("FILE PERSETUJUAN PEMBIMBING")) {
                String headerValue = "attachment; filename=" + retrievedSempro.getNameFilePersetujuanPembimbing();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedSempro.getPersetujuanPembimbing());
                outputStream.close();
            }

        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }

    }

}
