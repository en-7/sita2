package protensi.sita.controller;

import protensi.sita.model.KoordinatorModel;
import protensi.sita.model.DosenModel;
import protensi.sita.model.EnumRole;
import protensi.sita.service.KoordinatorServiceImpl;

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
public class KoordinatorController {
    @Autowired
    private KoordinatorServiceImpl koordinatorService;

    @GetMapping("/koordinator/viewall")
    public String viewAllKoordinator(Model model) {
        List<KoordinatorModel> listKoordinator = koordinatorService.findAllKoordinator();
        model.addAttribute("listKoordinator", listKoordinator);
        return "user/koordinator-viewall";
    }

    @GetMapping("/koordinator/add")
    public String addKoordinatorFormPage(Model model) {
        KoordinatorModel koordinator = new KoordinatorModel();
        model.addAttribute("koordinator", koordinator);
        return "user/koordinator-add-form";
    }

    @PostMapping("/koordinator/add")
    public String addKoordinatorSubmitPage(@ModelAttribute KoordinatorModel koordinator, Model model) {
        Set<EnumRole> roleKoordinator = new HashSet<EnumRole>();
        roleKoordinator.add(EnumRole.KOORDINATOR);
        koordinator.setRoles(roleKoordinator);
        koordinatorService.addKoordinator(koordinator);
        model.addAttribute("koordinator", koordinator);
        return "user/koordinator-add-form";
    }

    @GetMapping("/koordinator/update/{idUser}")
    public String updateKoordinatorFormPage(@PathVariable Long idUser, Model model) {
        KoordinatorModel koordinator = koordinatorService.findKoordinatorById(idUser);
        model.addAttribute("koordinator", koordinator);
        return "user/koordinator-update-form";
    }

    @PostMapping("/koordinator/update/{idUser}")
    public String updateKoordinatorSubmitPage(@ModelAttribute KoordinatorModel koordinator,
            @PathVariable Long idUser,
            @RequestParam("username") String username,
            @RequestParam("nama") String nama,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model, Authentication authentication) {
        try {
            koordinator.setUsername(username);
            koordinator.setNama(nama);
            koordinator.setEmail(email);
            koordinator.setPassword(password);

            koordinator = koordinatorService.findKoordinatorById(idUser);

            if (!username.isEmpty()) {
                koordinator.setUsername(username);
            }

            if (!nama.isEmpty()) {
                koordinator.setNama(nama);
            }

            if (!email.isEmpty()) {
                koordinator.setEmail(email);
            }

            if (!password.isEmpty()) {
                koordinator.setPassword(password);
            }

            Set<EnumRole> roleKoordinator = new HashSet<EnumRole>();
            roleKoordinator.add(EnumRole.KOORDINATOR);
            koordinator.setRoles(roleKoordinator);
            koordinatorService.updateKoordinator(koordinator);
            model.addAttribute("koordinator", koordinator);
            model.addAttribute("id", koordinator.getIdUser());
            return "user/koordinator-update-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }
    }
}
