package protensi.sita.controller;

import protensi.sita.model.DosenModel;
import protensi.sita.model.EnumRole;
import protensi.sita.model.SeminarHasilModel;
import protensi.sita.model.UserModel;
import protensi.sita.service.BaseService;
import protensi.sita.security.UserDetailsServiceImpl;
import protensi.sita.service.DosenServiceImpl;
import protensi.sita.service.ManageUserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class DosenController {
    @Autowired
    private DosenServiceImpl dosenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ManageUserServiceImpl manageUserService;

    @Autowired
    public BaseService baseService;

    // @GetMapping("/dosen/viewall")
    // public String viewAllDosen(Model model) {
    // List<DosenModel> listDosen = dosenService.findAllDosen();
    // model.addAttribute("listDosen", listDosen);
    // return "user/dosen-viewall";
    // }

    @GetMapping("/dosen/viewall")
    public String viewAllDosen(Model model, Authentication authentication) {
        List<UserModel> listUser = manageUserService.findAllUser();
        List<UserModel> listDosen = new ArrayList<UserModel>();

        for (UserModel user : listUser) {
            if (user.getRoles().contains(EnumRole.PEMBIMBING) && user.getRoles().contains(EnumRole.PENGUJI)) {
                listDosen.add(user);
            }
        }
        model.addAttribute("roleUser", baseService.getCurrentRole());
        model.addAttribute("listDosen", listDosen);
        return "user/dosen-viewall";
    }

    @GetMapping("/dosen/add")
    public String addDosenFormPage(Model model) {
        UserModel dosen = new UserModel();
        model.addAttribute("dosen", dosen);
        return "user/dosen-add-form";
    }

    @PostMapping("/dosen/add")
    public String addDosenSubmitPage(@ModelAttribute UserModel dosen, Model model) {
        Set<EnumRole> roleDosen = new HashSet<EnumRole>();
        roleDosen.add(EnumRole.PEMBIMBING);
        roleDosen.add(EnumRole.PENGUJI);
        dosen.setRoles(roleDosen);
        manageUserService.addUser(dosen);
        model.addAttribute("dosen", dosen);
        return "user/dosen-add-form";
    }

    @GetMapping("/dosen/update/{idUser}")
    public String updateDosenFormPage(@PathVariable Long idUser, Model model) {
        UserModel dosen = manageUserService.findUserById(idUser);
        model.addAttribute("dosen", dosen);
        return "user/dosen-update-form";
    }

    @PostMapping("/dosen/update/{idUser}")
    public String updateDosenSubmitPage(@ModelAttribute UserModel dosen,
            @PathVariable Long idUser,
            @RequestParam("username") String username,
            @RequestParam("nama") String nama,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model, Authentication authentication) {
        try {
            dosen.setUsername(username);
            dosen.setNama(nama);
            dosen.setEmail(email);
            dosen.setPassword(password);

            dosen = manageUserService.findUserById(idUser);

            if (!username.isEmpty()) {
                dosen.setUsername(username);
            }

            if (!nama.isEmpty()) {
                dosen.setNama(nama);
            }

            if (!email.isEmpty()) {
                dosen.setEmail(email);
            }

            if (!password.isEmpty()) {
                dosen.setPassword(password);
            }

            Set<EnumRole> roleDosen = new HashSet<EnumRole>();
            roleDosen.add(EnumRole.PEMBIMBING);
            roleDosen.add(EnumRole.PENGUJI);
            dosen.setRoles(roleDosen);
            manageUserService.updateUser(dosen);
            model.addAttribute("dosen", dosen);
            model.addAttribute("id", dosen.getIdUser());
            return "user/dosen-update-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }
    }
}
