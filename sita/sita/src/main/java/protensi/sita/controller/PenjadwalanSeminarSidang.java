package protensi.sita.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import protensi.sita.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import protensi.sita.security.UserDetailsServiceImpl;
import protensi.sita.service.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PenjadwalanSeminarSidang {

    @Autowired
    private JadwalSidangSeminarService jadwalSidangSeminarService;

    @Autowired
    private SeminarProposalService seminarProposalService;

    @Autowired
    private SeminarHasilServiceImpl seminarHasilService;

    @Autowired
    private TugasAkhirService tugasAkhirService;

    @Autowired
    public BaseService baseService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /////////////////////////// SEMINAR PROPOSAL///////////////////////////

    // method add jadwalsempro
    @GetMapping("/jadwalSidangProposal/create/{id}")
    public String addJadwalSidangProposalForm(@PathVariable("id") Long id, Model model) {
        JadwalSidangModel jadwalSidang = new JadwalSidangModel();
        SeminarProposalModel getSeminarProposalById = seminarProposalService.findSemproById(id);
        jadwalSidang.setSeminarProposal(getSeminarProposalById);

        model.addAttribute("addJadwalSempro", jadwalSidang);
        model.addAttribute("getPenguji", jadwalSidang.getSeminarProposal().getUgb().getPenguji());
        return "PenjadwalanSeminarSidang/seminarProposal/form-add-jadwalSempro";

    }

    @PostMapping("/jadwalSidangProposal/create")
    public String addJadwalSidangProposalSubmitPage(@ModelAttribute JadwalSidangModel jadwalSidangSeminar,
            Model model) {
        jadwalSidangSeminarService.addJadwalSidangSeminar(jadwalSidangSeminar);
        return "redirect:/jadwalSidangProposal";
    }

    // method viewall JadwalSempro
    @GetMapping("/jadwalSidangProposal")
    public String jadwalSidangProposalForm(Model model) {
        List<JadwalSidangModel> listJadwalSempro = jadwalSidangSeminarService.getListJadwalSidang();
        List<JadwalSidangModel> getListSidangProposal = new ArrayList<>();
        for (JadwalSidangModel i : listJadwalSempro) {
            if (i.getSeminarProposal() != null) {
                getListSidangProposal.add(i);
            }
        }
        model.addAttribute("listjadwalSidangSeminar", getListSidangProposal);
        model.addAttribute("roleUser", baseService.getCurrentRole());
        return "PenjadwalanSeminarSidang/seminarProposal/jadwalSidangProposal";
    }

    @GetMapping("/jadwalSidangProposal-pendaftar")
    public String jadwalSidangProposalPendaftar(Model model, Authentication auth) {
        String namaUser = auth.getName();
        UserModel user = userDetailsService.findByUsername(namaUser);
        if (user.getRoles().contains(EnumRole.KOORDINATOR)) {
            List<SeminarProposalModel> listPendaftarSempro = seminarProposalService.findAllSempro();
            List<SeminarProposalModel> newListPendaftarSempro = new ArrayList<SeminarProposalModel>();
            for (SeminarProposalModel i : listPendaftarSempro) {
                if (i.getJadwalSidang() == null && i.getStatusDokumen().equalsIgnoreCase("APPROVED")) {
                    newListPendaftarSempro.add(i);
                }
            }
            model.addAttribute("listdaftarSempro", newListPendaftarSempro);

            return "PenjadwalanSeminarSidang/seminarProposal/jadwalSidangProposal-pendaftar";
        } else {
            return "sempro/error-sempro";
        }

    }

    // controller delete sempro
    @GetMapping(value = "/jadwalSidangProposal/delete/{id}")
    public String deleteSempro(@PathVariable Long id, Model model) {
        jadwalSidangSeminarService.deletesJadwalSidangSeminar(id);
        return "PenjadwalanSeminarSidang/seminarProposal/deleteSempro";
    }

    // controller update sempro
    @GetMapping("/jadwalSidangProposal/setJadwal/{id}")
    public String SetJadwalSidangProposalFormPage(@PathVariable("id") Long id, Model model) {
        JadwalSidangModel setJadwalSempro = jadwalSidangSeminarService.getJadwalSidangById(id);
        model.addAttribute("setJadwalSempro", setJadwalSempro);
        model.addAttribute("getPengujiSet", setJadwalSempro.getSeminarProposal().getUgb().getPenguji());
        return "PenjadwalanSeminarSidang/seminarProposal/form-set-jadwalSempro";
    }

    @PostMapping("/jadwalSidangProposal/setJadwal")
    public String SetJadwalSidangProposalSubmitPage(@ModelAttribute JadwalSidangModel jadwalSempro, Model model) {
        jadwalSidangSeminarService.setJadwalSidang(jadwalSempro);
        System.out.println(jadwalSempro.getTanggalSempro().getClass());
        return "redirect:/jadwalSidangProposal";

    }

    /////////////////////////// SEMINAR HASIL///////////////////////////

    // method add jadwalSemhas
    @GetMapping("/jadwalSidangHasil/create/{id}")
    public String addJadwalSeminarHasilForm(@PathVariable("id") Long id, Model model) {
        JadwalSidangModel jadwalSidangSemhas = new JadwalSidangModel();
        SeminarHasilModel getSeminarHasilById = seminarHasilService.findSemhasById(id);
        jadwalSidangSemhas.setSeminarHasil(getSeminarHasilById);

        model.addAttribute("addJadwalSemhas", jadwalSidangSemhas);
        model.addAttribute("getPenguji", jadwalSidangSemhas.getSeminarHasil().getUgb().getPenguji());

        return "PenjadwalanSeminarSidang/seminarHasil/form-add-jadwalSemhas";

    }

    @PostMapping("/jadwalSidangHasil/create")
    public String addJadwalSeminarHasilSubmitPage(@ModelAttribute JadwalSidangModel jadwalSemhas, Model model) {
        jadwalSidangSeminarService.addJadwalSemhas(jadwalSemhas);
        return "redirect:/jadwalSidangHasil";
    }

    // controller viewall jadwal semhas
    @GetMapping("/jadwalSidangHasil")
    public String jadwalSidangHasilForm(Model model) {
        List<JadwalSidangModel> listJadwalSidangSeminarHasil = jadwalSidangSeminarService.getListJadwalSidang();
        List<JadwalSidangModel> getListSidangHasil = new ArrayList<>();
        for (JadwalSidangModel i : listJadwalSidangSeminarHasil) {
            if (i.getSeminarHasil() != null) {
                getListSidangHasil.add(i);
            }
        }
        model.addAttribute("listjadwalSidangSeminarHasil", getListSidangHasil);
        model.addAttribute("roleUser", baseService.getCurrentRole());
        return "PenjadwalanSeminarSidang/seminarHasil/jadwalSidangHasil";
    }

    @GetMapping("/jadwalSidangHasil-pendaftar")
    public String jadwalSidangHasilPendaftar(Model model, Authentication auth) {
        String namaUser = auth.getName();
        UserModel user = userDetailsService.findByUsername(namaUser);
        if (user.getRoles().contains(EnumRole.KOORDINATOR)) {
            List<SeminarHasilModel> listPedaftarSemhas = seminarHasilService.findAllSeminarHasil();
            List<SeminarHasilModel> newLisPendaftarSemhas = new ArrayList<>();
            for (SeminarHasilModel j : listPedaftarSemhas) {
                if (j.getJadwalSidang() == null && j.getStatusDokumen().equalsIgnoreCase("APPROVED")) {
                    newLisPendaftarSemhas.add(j);
                }
            }
            model.addAttribute("listdaftarSemhas", newLisPendaftarSemhas);
            return "PenjadwalanSeminarSidang/seminarHasil/jadwalSidangHasil-pendaftar";
        } else {
            return "sempro/error-sempro";
        }
    }

    // controller delete jadwal semhas
    @GetMapping(value = "/jadwalSidangHasil/delete/{id}")
    public String deleteSemhas(@PathVariable Long id, Model model) {
        jadwalSidangSeminarService.deletesJadwalSidangSeminar(id);
        return "PenjadwalanSeminarSidang/seminarHasil/deleteSemhas";
    }

    // controller update semhas
    @GetMapping("/jadwalSidangHasil/setJadwal/{id}")
    public String SetJadwalSidangHasilFormPage(@PathVariable("id") Long id, Model model) {
        JadwalSidangModel setJadwalSemhas = jadwalSidangSeminarService.getJadwalSidangById(id);
        model.addAttribute("setJadwalSemhas", setJadwalSemhas);
        model.addAttribute("getPengujiSemhas", setJadwalSemhas.getSeminarHasil().getUgb().getPenguji());
        return "PenjadwalanSeminarSidang/seminarHasil/form-set-jadwalSemhas";
    }

    @PostMapping("/jadwalSidangHasil/setJadwal")
    public String SetJadwalSidangHasilSubmitPage(@ModelAttribute JadwalSidangModel jadwalSemhas, Model model) {
        jadwalSidangSeminarService.setJadwalSidang(jadwalSemhas);
        System.out.println(jadwalSemhas.getTanggalSempro());
        return "redirect:/jadwalSidangHasil";

    }

    // SIDANG TUGAS AKHIR///

    // method add jadwalSidangTA
    @GetMapping("/jadwalSidangTugasAkhir/create/{id}")
    public String addJadwalSidangTAForm(@PathVariable("id") Long id, Model model) {
        JadwalSidangModel jadwalSidangTa = new JadwalSidangModel();
        TugasAkhirModel getSidangTaById = tugasAkhirService.findTugasAkhirById(id);
        jadwalSidangTa.setTugasAkhir(getSidangTaById);

        model.addAttribute("addJadwalSidangTa", jadwalSidangTa);
        model.addAttribute("getPengujiTa", jadwalSidangTa.getTugasAkhir().getUgb().getPenguji());
        return "PenjadwalanSeminarSidang/sidangTA/form-add-jadwalSidangTA";
    }

    @PostMapping("/jadwalSidangTugasAkhir/create")
    public String addJadwalSidangTASubmit(@ModelAttribute JadwalSidangModel tugasAkhir) {
        jadwalSidangSeminarService.addJadwalSidangTa(tugasAkhir);
        return "redirect:/jadwalSidangTugasAkhir";
    }

    // viewall jadwal Sidang Tugas Akhir
    @GetMapping("/jadwalSidangTugasAkhir")
    public String jadwalSidangTugasAkhir(Model model) {
        List<JadwalSidangModel> listJadwalSidangSeminar = jadwalSidangSeminarService.getListJadwalSidang();
        List<JadwalSidangModel> getListJadwalSidangTA = new ArrayList<>();
        for (JadwalSidangModel i : listJadwalSidangSeminar) {
            if (i.getTugasAkhir() != null) {
                getListJadwalSidangTA.add(i);
            }
        }
        model.addAttribute("listjadwalSidangTA", getListJadwalSidangTA);
        model.addAttribute("roleUser", baseService.getCurrentRole());
        return "PenjadwalanSeminarSidang/sidangTA/jadwalSidangTA";
    }

    @GetMapping("/jadwalSidangTugasAkhir-pendaftar")
    public String jadwalSidangTugasAkhirPendaftar(Model model, Authentication auth) {
        String namaUser = auth.getName();
        UserModel user = userDetailsService.findByUsername(namaUser);
        if (user.getRoles().contains(EnumRole.KOORDINATOR)) {
            List<TugasAkhirModel> listPendaftarTugasAkhir = tugasAkhirService.findAllTugasAkhir();
            List<TugasAkhirModel> newListPendaftarTugasAkhir = new ArrayList<>();
            for (TugasAkhirModel k : listPendaftarTugasAkhir) {
                if (k.getJadwalSidang() == null && k.getStatusDokumen().equalsIgnoreCase("APPROVED")) {
                    newListPendaftarTugasAkhir.add(k);
                }
            }
            model.addAttribute("listdaftarSidangTA", newListPendaftarTugasAkhir);
            return "PenjadwalanSeminarSidang/sidangTA/jadwalSidangTA-pendaftar";
        } else {
            return "sempro/error-sempro";
        }
    }

    // controller delete sidangTA
    @GetMapping(value = "/jadwalSidangTugasAkhir/delete/{id}")
    public String deleteSidangTA(@PathVariable Long id, Model model) {
        jadwalSidangSeminarService.deletesJadwalSidangSeminar(id);
        return "PenjadwalanSeminarSidang/sidangTA/deleteTA";
    }

    // controller update sidang TA
    @GetMapping("/jadwalSidangTugasAkhir/setJadwal/{id}")
    public String SetJadwalSidangTAFormPage(@PathVariable("id") Long id, Model model) {
        JadwalSidangModel setJadwalSidangTA = jadwalSidangSeminarService.getJadwalSidangById(id);
        model.addAttribute("setJadwalSidangTA", setJadwalSidangTA);
        model.addAttribute("getPengujiTa", setJadwalSidangTA.getTugasAkhir().getUgb().getPenguji());
        return "PenjadwalanSeminarSidang/sidangTA/form-set-jadwalSidangTA";
    }

    @PostMapping("/jadwalSidangTugasAkhir/setJadwal")
    public String SetJadwalSidangTASubmitPage(@ModelAttribute JadwalSidangModel jadwalSidangTA, Model model) {
        System.out.println(jadwalSidangTA.getTanggalSempro());
        jadwalSidangSeminarService.setJadwalSidang(jadwalSidangTA);
        System.out.println(jadwalSidangTA.getTanggalSempro());
        return "redirect:/jadwalSidangTugasAkhir";

    }

}
