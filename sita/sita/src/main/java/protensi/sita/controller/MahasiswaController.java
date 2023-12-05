package protensi.sita.controller;

import protensi.sita.model.MahasiswaModel;
import protensi.sita.model.EnumRole;
import protensi.sita.service.MahasiswaServiceImpl;

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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class MahasiswaController {
    @Autowired
    private MahasiswaServiceImpl mahasiswaService;

    @GetMapping("/mahasiswa/viewall")
    public String viewAllMahasiswa(Model model) {
        List<MahasiswaModel> listMahasiswa = mahasiswaService.findAllMahasiswa();
        model.addAttribute("listMahasiswa", listMahasiswa);
        return "user/mahasiswa-viewall";
    }

    @GetMapping("/mahasiswa/add")
    public String addMahasiswaFormPage(Model model) {
        MahasiswaModel mahasiswa = new MahasiswaModel();
        model.addAttribute("mahasiswa", mahasiswa);
        return "user/mahasiswa-add-form";
    }

    @PostMapping("/mahasiswa/add")
    public String addMahasiswaSubmitPage(@ModelAttribute MahasiswaModel mahasiswa, Model model) {
        Set<EnumRole> roleMahasiswa = new HashSet<EnumRole>();
        roleMahasiswa.add(EnumRole.MAHASISWA);
        mahasiswa.setRoles(roleMahasiswa);
        mahasiswa.setTahap("NEW");
        mahasiswaService.addMahasiswa(mahasiswa);
        model.addAttribute("mahasiswa", mahasiswa);
        model.addAttribute("nama", mahasiswa.getNama());
        return "redirect:/mahasiswa/viewall";
    }

    @GetMapping("/mahasiswa/update/{idUser}")
    public String updateMahasiswaFormPage(@PathVariable Long idUser, Model model) {
        MahasiswaModel mahasiswa = mahasiswaService.findMahasiswaById(idUser);
        model.addAttribute("mahasiswa", mahasiswa);
        return "user/mahasiswa-update-form";
    }

    @PostMapping("/mahasiswa/update/{idUser}")
    public String updateMahasiswaSubmitPage(@ModelAttribute MahasiswaModel mahasiswa,
            @PathVariable Long idUser,
            @RequestParam("username") String username,
            @RequestParam("nama") String nama,
            @RequestParam("email") String email,
            @RequestParam("nim") Integer nim,
            Model model, Authentication authentication) {
        try {
            mahasiswa.setUsername(username);
            mahasiswa.setNama(nama);
            mahasiswa.setEmail(email);
            mahasiswa.setNim(nim);

            mahasiswa = mahasiswaService.findMahasiswaById(idUser);

            if (!username.isEmpty()) {
                mahasiswa.setUsername(username);
            }

            if (!nama.isEmpty()) {
                mahasiswa.setNama(nama);
            }

            if (!email.isEmpty()) {
                mahasiswa.setEmail(email);
            }

            if (nim != null) {
                mahasiswa.setNim(nim);
            }

            Set<EnumRole> roleMahasiswa = new HashSet<EnumRole>();
            roleMahasiswa.add(EnumRole.MAHASISWA);
            mahasiswa.setRoles(roleMahasiswa);
            mahasiswaService.updateMahasiswa(mahasiswa);
            model.addAttribute("mahasiswa", mahasiswa);
            model.addAttribute("id", mahasiswa.getIdUser());
            return "redirect:/mahasiswa/viewall";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }
    }
}
