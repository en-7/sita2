package protensi.sita.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import protensi.sita.model.EnumRole;
import protensi.sita.model.MahasiswaModel;
import protensi.sita.model.UserModel;
import protensi.sita.repository.UserDb;
import protensi.sita.security.UserDetailsServiceImpl;
import protensi.sita.service.BaseService;
import protensi.sita.service.MahasiswaServiceImpl;
import protensi.sita.service.ManageUserServiceImpl;

@Controller
public class ManajemenUserController {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    UserDb userDb;

    @Autowired
    private ManageUserServiceImpl manageUserService;

    @Autowired
    private MahasiswaServiceImpl mahasiswaService;

    @Autowired
    public BaseService baseService;

    @GetMapping("/users/viewall")
    public String viewAllUser(Model model, Authentication authentication){
        List<UserModel> listUser = manageUserService.findAllUser();

        model.addAttribute("listUser", listUser);
        return "user/viewall";

    }

    @GetMapping("/users/update/{idUser}")
    public String updateUserFormPage(@PathVariable Long idUser, Model model){
        UserModel pengguna = manageUserService.findUserById(idUser);
        model.addAttribute("user", pengguna);
        String [] listRole = {"DOSEN", "KOORDINATOR", "ADMIN"};
        if(pengguna.getRoles().contains(EnumRole.MAHASISWA)){
            MahasiswaModel mhs = mahasiswaService.findMahasiswaById(idUser);
            model.addAttribute("mahasiswa", mhs);
            model.addAttribute("userRole", "MAHASISWA");
        }else if (pengguna.getRoles().contains(EnumRole.PEMBIMBING) && pengguna.getRoles().contains(EnumRole.PENGUJI)){
            model.addAttribute("userRole", "DOSEN");
        }else if (pengguna.getRoles().contains(EnumRole.KOORDINATOR)){
            model.addAttribute("userRole", "KOORDINATOR");
        }else{
            model.addAttribute("userRole", "ADMIN");
        }
        model.addAttribute("listRole", listRole);
        return "user/update-form";
    }

    @PostMapping("/users/update")
    public String updateUserSubmitPage(@ModelAttribute UserModel pengguna,  Model model){
        System.out.println("---*******masuk---");

        UserModel usr = manageUserService.findUserById(pengguna.getIdUser());
        System.out.println("*******ID NYA: "+ pengguna.getIdUser());
        System.out.println("*******ROLE NYA: "+ pengguna.getRole_user());
        System.out.println("*******USERNAME NYA: "+ pengguna.getUsername());
        System.out.println("*******NAME NYA: "+ pengguna.getNama());


        usr.setNama(pengguna.getNama());
        usr.setUsername(pengguna.getUsername());
        Set<EnumRole> rolePengguna = new HashSet<EnumRole>();
        //  zlsp;;
         
         if (pengguna.getRole_user().equals("DOSEN")){
            rolePengguna.add(EnumRole.PENGUJI);
            rolePengguna.add(EnumRole.PEMBIMBING);
            usr.setRoles(rolePengguna);
        }else if (pengguna.getRole_user().equals("KOORDINATOR")){
            rolePengguna.add(EnumRole.KOORDINATOR);
            usr.setRoles(rolePengguna);
        }else{
            rolePengguna.add(EnumRole.ADMIN);
            usr.setRoles(rolePengguna);
        }
        userDb.save(usr);
        return "redirect:/";
    }


    @GetMapping("/users/filter")
    public String filterUser(@RequestParam("role") String role, Model model){
        System.out.println("MASUK ==");
        List<UserModel> listUser = manageUserService.findAllUser();

        if(role.equals("MAHASISWA")){
            List<UserModel> listMahasiswa = new ArrayList<UserModel>();
            for (UserModel user : listUser) {
                if (user.getRoles().contains(EnumRole.MAHASISWA)) {
                    listMahasiswa.add(user);
                }
            }
            model.addAttribute("listUser", listMahasiswa);
        }
        else if(role.equals("DOSEN")){
            List<UserModel> listDosen = new ArrayList<UserModel>();
            for (UserModel user : listUser) {
            if (user.getRoles().contains(EnumRole.PEMBIMBING) && user.getRoles().contains(EnumRole.PENGUJI)) {
                listDosen.add(user);
            }
            model.addAttribute("listUser", listDosen);
        }
        }else if(role.equals("KOORDINATOR")){
            List<UserModel> listKoordinator = new ArrayList<UserModel>();
            for (UserModel user : listUser) {
                if (user.getRoles().contains(EnumRole.KOORDINATOR)) {
                    listKoordinator.add(user);
                }
            }
            model.addAttribute("listUser", listKoordinator);
        }else{
            List<UserModel> listAdmin = new ArrayList<UserModel>();
            for (UserModel user : listUser) {
                if (user.getRoles().contains(EnumRole.ADMIN)) {
                    listAdmin.add(user);
                }
            }
            model.addAttribute("listUser", listAdmin);
        }
        return"user/viewall";
    }
}
