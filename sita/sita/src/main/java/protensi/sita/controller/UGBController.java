package protensi.sita.controller;

import protensi.sita.model.EnumRole;
import protensi.sita.model.MahasiswaModel;
import protensi.sita.model.SeminarProposalModel;
import protensi.sita.model.TimelineModel;
import protensi.sita.model.TugasAkhirModel;
import protensi.sita.model.UgbModel;
import protensi.sita.model.UserModel;
import protensi.sita.repository.MahasiswaDb;
import protensi.sita.repository.PembimbingDb;
import protensi.sita.repository.UserDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import protensi.sita.service.UgbServiceImpl;
import protensi.sita.service.BaseService;
import protensi.sita.service.TimelineServiceImpl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UGBController {

    @Autowired
    private UgbServiceImpl ugbService;

    @Autowired
    private TimelineServiceImpl tlService;

    @Autowired
    PembimbingDb pembimbingDb;

    @Autowired
    UserDb userDb;

    @Autowired
    MahasiswaDb mahasiswaDb;
    

    @Autowired
    public BaseService baseService;

    @GetMapping("/ugb/add")
    public String addUgbFormPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        MahasiswaModel thisMahasiswa = mahasiswaDb.findByUsername(username);
        UgbModel retrievedUgb = ugbService.findByIdMahasiswa(thisMahasiswa);

        TimelineModel tl = tlService.checkDate();
        LocalDate nowDate = LocalDate.now();
        Instant now = Instant.now();
        ZonedDateTime nowJakarta = now.atZone(ZoneId.of("Asia/Jakarta")); 
  
        LocalDate nowTimezonedDate = nowJakarta.toLocalDate();

        System.out.println("nowTimeZonedDate: "+ nowTimezonedDate);

        if (retrievedUgb != null) {
            String idUgb = retrievedUgb.getIdUgb().toString();
            return "redirect:/ugb/detail/" + idUgb;
        } else {
            System.out.println("regugb: "+ tl.getRegUGB());
            System.out.println("now: "+ nowDate);

            if(tl.getRegUGB() != null && tl.getRegUGB().equals(nowDate)){
                UgbModel ugbModel = new UgbModel();
                model.addAttribute("ugb", ugbModel);
                model.addAttribute("listPembimbing", ugbService.getListPembimbing());
                return "ugb/add-ugb-form";
            }else{
                return "ugb/no-access-ugb";
            }
        }
    }

    @PostMapping("/ugb/add")
    public String addUgbSubmitPage(@ModelAttribute UgbModel ugb,
            @RequestParam("bukti_kp") MultipartFile bukti_kp,
            @RequestParam("transcript") MultipartFile transcript,
            @RequestParam("file_khs") MultipartFile file_khs,
            @RequestParam("file_ugb") MultipartFile file_ugb) {

        System.out.println("*** pembimbing_1 : " + ugb.getIdPembimbing1());
        System.out.println("*** pembimbing_2 : " + ugb.getIdPembimbing2());

        String result = ugbService.addUgb(ugb, bukti_kp, transcript, file_khs, file_ugb);
        String idUgb = ugb.getIdUgb().toString();

        return "redirect:/ugb/detail/" + idUgb;
    }

    @GetMapping("/ugb/addcatatan/{idUgb}")
    public String addCatatanUgbFormPage(Model model, String catatanJudulUgb, String latarBelakang, String tujuanManfaat,
            String ruangLingkup, String keterbaruan, String metodologi) {
        return "ugb/add-ugb-catatan";
    }

    @PostMapping("/ugb/addcatatan/{idUgb}")
    public String addCatatanUgbSubmitPage(@ModelAttribute UgbModel ugb, String catatanJudulUgb, String latarBelakang,
            String tujuanManfaat, String ruangLingkup, String keterbaruan, String metodologi) {

        String result = ugbService.addCatatanUgb(ugb, catatanJudulUgb, latarBelakang, tujuanManfaat, ruangLingkup,
                keterbaruan, metodologi);
        String idUgb = ugb.getIdUgb().toString();
        return "redirect:/ugb/catatan/" + idUgb;
    }

    @GetMapping("/ugb/catatan/{idUgb}")
    public String viewCatatanUgb(@PathVariable Long idUgb, Model model) {
        UgbModel retrievedUgb = ugbService.getUgbById(idUgb);
        model.addAttribute("ugb", retrievedUgb);
        model.addAttribute("roleUser", baseService.getCurrentRole());
        return "ugb/detail-catatan-ugb";
    }

    @PostMapping("/ugb/catatan/{idUgb}")
    public String viewSubbmittedCatatanUgb(@ModelAttribute UgbModel ugb, String catatanJudulUgb, String latarBelakang,
            String tujuanManfaat, String ruangLingkup, String keterbaruan, String metodologi) {

        String result = ugbService.addCatatanUgb(ugb, catatanJudulUgb, latarBelakang, tujuanManfaat, ruangLingkup,
                keterbaruan, metodologi);
        String idUgb = ugb.getIdUgb().toString();
        return "ugb/detail-catatan-ugb" + idUgb;
    }

    @GetMapping("/ugb/update/{idUgb}")
    public String updateUgbFormPage(@PathVariable Long idUgb, Model model) {
        UgbModel retrievedUgb = ugbService.getUgbById(idUgb);
        Set<UserModel> set = retrievedUgb.getPembimbing();
        Iterator iterator = set.iterator();
        List<UserModel> listPembimbing = ugbService.getListPembimbing();
        UserModel pembimbing1 = (UserModel) iterator.next();
        UserModel pembimbing2 = (UserModel) iterator.next();

        System.out.println("### PEMBIMBING 1: " + pembimbing1.getNama());
        System.out.println("### PEMBIMBING 2: " + pembimbing2.getNama());

        model.addAttribute("ugb", retrievedUgb);
        model.addAttribute("pembimbing2", pembimbing2);
        model.addAttribute("pembimbing1", pembimbing1);
        model.addAttribute("listPembimbing", listPembimbing);
        model.addAttribute("roleUser", baseService.getCurrentRole());
        return "ugb/update-ugb";
    }

    @PostMapping("/ugb/updateK/{idUgb}")
    public String updateUgbSubmitKoordinator(
            @PathVariable Long idUgb,
            @RequestParam("id_p1") Long idP1,
            @RequestParam("id_p2") Long idP2) {
        System.out.println("pemb_1 = " + idP1.toString());
        System.out.println("pemb_2 = " + idP2.toString());

        ugbService.updateUgbKoordinator(idUgb, idP1, idP2);
        return "redirect:/ugb/detail/" + idUgb.toString();
    }

    @PostMapping("/ugb/updateM/{idUgb}")
    public String updateUgbSubmitKoordinator(
            @PathVariable Long idUgb,
            @RequestParam("judul_ugb") String judul,
            @RequestParam("bukti_kp") MultipartFile bukti_kp,
            @RequestParam("transcript") MultipartFile transcript,
            @RequestParam("file_khs") MultipartFile file_khs,
            @RequestParam("file_ugb") MultipartFile file_ugb) {
        ugbService.updateUgbMahasiswa(idUgb, judul, bukti_kp, transcript, file_khs, file_ugb);
        return "redirect:/ugb/detail/" + idUgb;
    }

    @GetMapping("/ugb/viewall")
    public String listUgb(Model model) {
        System.out.println("*** test ***");

        List<UgbModel> result = ugbService.viewAllUgb();

        System.out.println("*** list ugb retrieved: " + result);

        model.addAttribute("listUgb", result);
        model.addAttribute("roleUser", baseService.getCurrentRole());
        return "ugb/viewall-ugb";
    }

    @GetMapping("/ugb/filter")
    public String filterUgb(@RequestParam("status") String status, Model model) {
        System.out.println("MASUK ==");
        List<UgbModel> filteredUGBList = ugbService.filterUgb(status);
        model.addAttribute("listUgb", filteredUGBList);
        model.addAttribute("roleUser", baseService.getCurrentRole());
        return "ugb/viewall-ugb";
    }

    @GetMapping("/ugb/detail/{idUgb}")
    public String viewDetailUgb(@PathVariable Long idUgb, Model model) {
        UgbModel retrievedUgb = ugbService.getUgbById(idUgb);
        model.addAttribute("ugb", retrievedUgb);
        model.addAttribute("roleUser", baseService.getCurrentRole());
        return "ugb/detail-ugb";
    }

    @GetMapping("/ugb/approve/{idUgb}")
    public String approveUgb(@PathVariable Long idUgb, Model model) {
        UgbModel retrievedUgb = ugbService.getUgbById(idUgb);
        // System.out.println(retrievedUgb.getJudulUgb());
        // System.out.println("status before: "+retrievedUgb.getStatusUgb());

        ugbService.approveUgb(retrievedUgb);

        // System.out.println("status after: "+retrievedUgb.getStatusUgb());

        model.addAttribute("ugb", retrievedUgb);
        model.addAttribute("roleUser", baseService.getCurrentRole());
        return "ugb/detail-ugb";
    }

    @PostMapping("/ugb/deny/{idUgb}")
    public String denyUgb(@PathVariable Long idUgb, @RequestParam("catatan") String catatan, Model model) {
        UgbModel retrievedUgb = ugbService.getUgbById(idUgb);
        ugbService.denyUgb(retrievedUgb, catatan);
        model.addAttribute("ugb", retrievedUgb);
        model.addAttribute("roleUser", baseService.getCurrentRole());
        return "ugb/detail-ugb";

    }

    @GetMapping("/ugb/downloadFile")
    public void downloadFile(@RequestParam("type") String type,
            @RequestParam("id") Long id,
            HttpServletResponse response) {
        ugbService.downloadUgbFiles(type, id, response);
    }

    @GetMapping("/ugb/allocate/{idUgb}")
    public String allocateUgb(@PathVariable Long idUgb, Model model){
        UgbModel getUgbUser = ugbService.getUgbById(idUgb);
        model.addAttribute("ugbUser", getUgbUser);
        model.addAttribute("listPenguji", ugbService.getListPenguji());
        return "ugb/allocate-ugb";
    }

    @PostMapping("/ugb/allocate/{idUgb}")
    public String allocateUgbSubmitPage(@PathVariable Long idUgb, 
                                        @RequestParam("id_pj1") Long idPJ1, 
                                        @RequestParam("id_pj2") Long idPJ2,
                                        Model model ) {
        ugbService.updateUGBKoordinatorforPenguji(idUgb, idPJ1, idPJ2);
        model.addAttribute("roleUser", baseService.getCurrentRole());
        // String idUgb = ugb.getIdUgb().toString();
        return "ugb/viewall-ugb";
    }
}

