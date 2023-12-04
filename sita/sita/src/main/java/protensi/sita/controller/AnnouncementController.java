package protensi.sita.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

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
import protensi.sita.model.SeminarProposalModel;
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
    public String createAnnouhncementSubmit(@ModelAttribute AnnouncementModel newAnnouncement,
                                            Model model, 
                                            Authentication auth,
                                            @RequestParam("ancFile") MultipartFile ancFile){

        try{
            byte[] fileAnnounce = ancFile.getBytes();
            String namaFile = StringUtils.cleanPath(ancFile.getOriginalFilename());

            newAnnouncement.setNamaFile(namaFile);
            newAnnouncement.setFile(fileAnnounce);
            Set<UserModel> setAuthor = new HashSet<>();
            String namaUser = auth.getName();
            UserModel getUser = userDetailsService.findByUsername(namaUser);

            setAuthor.add(getUser);
            newAnnouncement.setAuthor(setAuthor);
            announcementService.addAnnouncement(newAnnouncement);
            return "redirect:/";
        }
        catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
    
        }
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
    public String updateAnnouncementSubmit(@ModelAttribute AnnouncementModel announce, 
                                            Authentication auth,
                                            @RequestParam("ancFile") MultipartFile ancFile){
        
        try{
            byte[] updateAnnouncement = ancFile.getBytes();
            if(!ancFile.isEmpty()){
                String namaFileUpdate = StringUtils.cleanPath(ancFile.getOriginalFilename());
                announce.setNamaFile(namaFileUpdate);
                announce.setFile(updateAnnouncement);
            }

            Set<UserModel> setAuthor = new HashSet<>();
            String namaUser = auth.getName();
            UserModel getUser = userDetailsService.findByUsername(namaUser);

            setAuthor.add(getUser);
            announce.setAuthor(setAuthor);
            announcementService.updateAnnouncement(announce);
            System.out.println(announce.getIdAnnouncement());
            return "redirect:/";
        }
        catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
    
        }
    }
   
    @GetMapping("/downloadFile")
    public void downloadFile(@RequestParam("type") String type,
            @RequestParam("id") Long id,
            HttpServletResponse response) {
        try {
            AnnouncementModel retrievedAnnouncement = announcementService.getAnnounceById(id);
            response.setContentType("application/ocetet-stream");
            String headerKey = "Content-Disposition";

            if (type.equals("FILE")) {
                String headerValue = "attachment; filename=" + retrievedAnnouncement.getNamaFile();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedAnnouncement.getFile());
                outputStream.close();
            }
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }

    }
}