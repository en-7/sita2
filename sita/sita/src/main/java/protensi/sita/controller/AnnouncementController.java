package protensi.sita.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.util.StringUtils;
import protensi.sita.model.AnnouncementModel;
import protensi.sita.model.UserModel;
import protensi.sita.security.UserDetailsServiceImpl;
import protensi.sita.service.AnnouncementService;

@Controller
public class AnnouncementController {


    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/announce/create")
    public String createAnnouncement(Model model){
        AnnouncementModel newAnncouncement = new AnnouncementModel();
        model.addAttribute("newAnnouncement", newAnncouncement);
        return "Announcement/form-add-announcement";
        
    }
    @PostMapping("/announce/create")
    public String createAnnouhncementSubmit(@ModelAttribute AnnouncementModel announce,
                                            Model model, 
                                            Authentication auth){


        Set<UserModel> setAuthor = new HashSet<>();
        String namaUser = auth.getName();
        UserModel getUser = userDetailsService.findByUsername(namaUser);

        setAuthor.add(getUser);
        announce.setAuthor(setAuthor);
        
        announcementService.addAnnouncement(announce);

        System.out.println(announce.getAuthor());
        return "redirect:/";
   
    }

    @GetMapping("/announce/view")
    public String viewDetailAnnouncement (@RequestParam(value = "id") Long id, Model model ){
        AnnouncementModel announce = announcementService.getAnnounceById(id);
        model.addAttribute("announce", announce);
        model.addAttribute("author", announce.getAuthor());
        return "Announcement/view-announcement";

    }

    @GetMapping("/announce/update/{id}")
    public String updateAnnouncement(@PathVariable("id") Long id, Model model){
        AnnouncementModel announce = announcementService.getAnnounceById(id);
        model.addAttribute("updateAnnounce", announce);
        return "Announcement/update-announcement";
    }

    @PostMapping("/announce/update")
    public String updateAnnouncementSubmit(@ModelAttribute AnnouncementModel announce, Authentication auth){

        Set<UserModel> setAuthor = new HashSet<>();
        String namaUser = auth.getName();
        UserModel getUser = userDetailsService.findByUsername(namaUser);

        setAuthor.add(getUser);
        announce.setAuthor(setAuthor);
        
        announcementService.addAnnouncement(announce);


        announcementService.updateAnnouncement(announce);
        System.out.println(announce.getIdAnnouncement());
        return "redirect:/";
    }




    // @PostMapping("/announce/create")
    // public String createAnnouhncementSubmit(@ModelAttribute AnnouncementModel announce,
    //                                         Model model, 
    //                                         Authentication auth,
    //                                         @RequestParam("file") MultipartFile file){

    //     try{
    //         // byte[] fileAnnounce = file.getBytes();
    //         String namaFile = StringUtils.cleanPath(file.getOriginalFilename());

    //         announce.setNamaFile(namaFile);
    //         announce.setFile(file.getBytes());
            
    //         announcementService.addAnnouncement(announce);
    //         return "redirect:/";
    //     }
    //     catch (IOException e) {
    //         throw new ResponseStatusException(
    //                 HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
    
    //     }
    // }
}