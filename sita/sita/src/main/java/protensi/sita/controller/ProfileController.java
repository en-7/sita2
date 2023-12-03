package protensi.sita.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import protensi.sita.model.MahasiswaModel;
import protensi.sita.model.SeminarHasilModel;
import protensi.sita.model.SeminarProposalModel;
import protensi.sita.model.TugasAkhirModel;
import protensi.sita.model.UgbModel;
import protensi.sita.model.UserModel;
import protensi.sita.repository.UserDb;
import protensi.sita.repository.MahasiswaDb;
import protensi.sita.service.SeminarHasilServiceImpl;
import protensi.sita.service.SeminarProposalServiceImpl;
import protensi.sita.service.TugasAkhirServiceImpl;
import protensi.sita.service.UgbServiceImpl;
import protensi.sita.service.UserServiceImpl;

import java.time.LocalDate;
import java.util.*;

@Controller
public class ProfileController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UgbServiceImpl ugbService;

    @Autowired
    private SeminarProposalServiceImpl semproService;

    @Autowired
    private SeminarHasilServiceImpl semhasService;

    @Autowired
    private TugasAkhirServiceImpl taService;

    @Autowired
    UserDb userDb;

    @Autowired
    MahasiswaDb mahasiswaDb;

    @GetMapping("/profile/{parameter}")
    public String profileIdPage(@PathVariable Long parameter, Model model) {
        UserModel thisUser = userDb.findByIdUser(parameter);
        MahasiswaModel mahasiswa = mahasiswaDb.findByIdUser(thisUser.getIdUser());
        UgbModel thisUgb = ugbService.findByIdMahasiswa(mahasiswa);

        if (!mahasiswa.getTahap().equals("NEW")) {
            Set<UserModel> setPembimbing = thisUgb.getPembimbing();
            Iterator iterator = setPembimbing.iterator();
            UserModel pembimbing1 = (UserModel) iterator.next();
            UserModel pembimbing2 = (UserModel) iterator.next();
            model.addAttribute("pembimbing2", pembimbing2);
            model.addAttribute("pembimbing1", pembimbing1);

            if (mahasiswa.getTahap().equals("SEMPRO") || mahasiswa.getTahap().equals("SEMHAS")
                    || mahasiswa.getTahap().equals("SIDANG") || mahasiswa.getTahap().equals("EVAL_UGB")) {
                Set<UserModel> setPenguji = thisUgb.getPenguji();

                iterator = setPenguji.iterator();
                UserModel penguji1 = (UserModel) iterator.next();
                UserModel penguji2 = (UserModel) iterator.next();
                model.addAttribute("penguji2", penguji2);
                model.addAttribute("penguji1", penguji1);
            }

            if (mahasiswa.getTahap().equals("SEMPRO") || mahasiswa.getTahap().equals("SEMHAS")
                    || mahasiswa.getTahap().equals("SIDANG")) {
                SeminarProposalModel sempro = semproService.findSemproByUgb(thisUgb);
                model.addAttribute("sempro", sempro);

                if (mahasiswa.getTahap().equals("SEMHAS") || mahasiswa.getTahap().equals("SIDANG")) {
                    SeminarHasilModel semhas = semhasService.findSemhasBySempro(sempro);
                    model.addAttribute("semhas", semhas);

                }
            }

            if (mahasiswa.getTahap().equals("SIDANG")) {
                TugasAkhirModel ta = taService.findTAByUgb(thisUgb);
                model.addAttribute("tugas_akhir", ta);

            }

        }

        model.addAttribute("pengguna", thisUser);
        model.addAttribute("mahasiswa", mahasiswa);
        model.addAttribute("ugb", thisUgb);
        return "profile/profile-mahasiswa.html";
    }

    @GetMapping("/profile")
    public String profilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserModel thisUser = userDb.findByUsername(username);
        MahasiswaModel mahasiswa = mahasiswaDb.findByIdUser(thisUser.getIdUser());
        UgbModel thisUgb = ugbService.findByIdMahasiswa(mahasiswa);

        if (!mahasiswa.getTahap().equals("NEW")) {
            Set<UserModel> setPembimbing = thisUgb.getPembimbing();
            Iterator iterator = setPembimbing.iterator();
            UserModel pembimbing1 = (UserModel) iterator.next();
            UserModel pembimbing2 = (UserModel) iterator.next();
            model.addAttribute("pembimbing2", pembimbing2);
            model.addAttribute("pembimbing1", pembimbing1);

            if (mahasiswa.getTahap().equals("SEMPRO") || mahasiswa.getTahap().equals("SEMHAS")
                    || mahasiswa.getTahap().equals("SIDANG") || mahasiswa.getTahap().equals("EVAL_UGB")) {
                Set<UserModel> setPenguji = thisUgb.getPenguji();

                iterator = setPenguji.iterator();
                UserModel penguji1 = (UserModel) iterator.next();
                UserModel penguji2 = (UserModel) iterator.next();
                model.addAttribute("penguji2", penguji2);
                model.addAttribute("penguji1", penguji1);
            }

            if (mahasiswa.getTahap().equals("SEMPRO") || mahasiswa.getTahap().equals("SEMHAS")
                    || mahasiswa.getTahap().equals("SIDANG")) {
                SeminarProposalModel sempro = semproService.findSemproByUgb(thisUgb);
                model.addAttribute("sempro", sempro);

                if (mahasiswa.getTahap().equals("SEMHAS") || mahasiswa.getTahap().equals("SIDANG")) {
                    SeminarHasilModel semhas = semhasService.findSemhasBySempro(sempro);
                    model.addAttribute("semhas", semhas);

                }
            }

            if (mahasiswa.getTahap().equals("SIDANG")) {
                TugasAkhirModel ta = taService.findTAByUgb(thisUgb);
                model.addAttribute("tugas_akhir", ta);

            }

        }

        model.addAttribute("pengguna", thisUser);
        model.addAttribute("mahasiswa", mahasiswa);
        model.addAttribute("ugb", thisUgb);
        return "profile/profile-mahasiswa.html";
    }

    @GetMapping("/updatePass")
    public String updatePassFormPage(Model model) {
        boolean wrongPass = false;
        model.addAttribute("wrongPass", wrongPass);
        return "profile/update-password-form";
    }

    @PostMapping("/updatePass")
    public String updatePassSubmitPage(Model model,
            @RequestParam("psw") String newPass,
            @RequestParam("oldPassword") String oldPass) {

        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        // String username = authentication.getName();
        // UserModel thisUser = userDb.findByUsername(username);
        // System.out.println("this pass: "+ thisUser.getPassword());
        // System.out.println("input pass: "+ userService.encrypt(oldPass));
        boolean isMatched = userService.matcher(oldPass);
        if (isMatched) {
            userService.updatePass(newPass);

            return "redirect:/logout";
        } else {
            boolean wrongPass = true;
            model.addAttribute("wrongPass", wrongPass);
            return "profile/update-password-form";
        }
    }

}
