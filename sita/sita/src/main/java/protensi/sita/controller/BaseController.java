package protensi.sita.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import protensi.sita.model.AnnouncementModel;
import protensi.sita.model.EnumRole;
import protensi.sita.security.UserDetailsServiceImpl;
import protensi.sita.service.AnnouncementService;
import protensi.sita.service.AnnouncementServiceImpl;
import protensi.sita.service.BaseService;
import protensi.sita.service.TimelineServiceImpl;

import java.util.*;

@Controller
public class BaseController {
    @Autowired
    public UserDetailsServiceImpl userDetailsService;

    @Autowired
    public BaseService baseService;

    @Autowired
    public AnnouncementService announcementService;
    
    private TimelineServiceImpl tlService;


    @GetMapping("/")
    private String home(Model model) {
        List<AnnouncementModel> listAnnounce = announcementService.getListAnnounce();
        model.addAttribute("roleUser", baseService.getCurrentRole());
        model.addAttribute("listAnnounce", listAnnounce);
        model.addAttribute("roleUser", baseService.getCurrentRole());
        return "home";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/";
    }

    @GetMapping(value = "/create-dummy")
    public String addDummy() {
        userDetailsService.addDummy();
        return "login";
    }

    @GetMapping("/error")
    private String Error(Model model) {
        model.addAttribute("roleUser", baseService.getCurrentRole());
        return "error";
    }

}
