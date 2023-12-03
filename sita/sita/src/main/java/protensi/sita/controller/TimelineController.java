package protensi.sita.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.commons.lang3.StringUtils;

import protensi.sita.model.TimelineModel;
import protensi.sita.repository.TimelineDb;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.LocalDateTime;

import java.time.LocalDate;
import java.util.*;



@Controller
public class TimelineController {

    @Autowired
    TimelineDb timelineDb;

    @GetMapping("/timeline")
    public String readTimelinePage(Model model){
        List<TimelineModel> listTl = timelineDb.findAll();
        Collections.sort(listTl , (a1, a2) -> a1.getPeriode().compareTo(a2.getPeriode()));

        int indexNow = listTl.size() - 1;
        LocalDate dateNow = LocalDate.now();
        String month = Integer.toString(dateNow.getMonthValue());
        String year = Integer.toString(dateNow.getYear());
        String periodeNow = year+"-"+month;
        System.out.println("nowPeriode: "+periodeNow);

        for(int i = 0; i < listTl.size(); i++){
            System.out.println("periode_in_list: "+listTl.get(i).getPeriode());
            if(listTl.get(i).getPeriode().equals(periodeNow)){
                indexNow = i;
            }
        }

        
        if(!listTl.isEmpty()){
            DateTimeFormatter ft = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.forLanguageTag("in-ID"));
            TimelineModel timeline = listTl.get(indexNow);

            LocalDate dateRegSidang = timeline.getRegSidang();
            LocalDate dateStartSidang = timeline.getStartSidang();
            LocalDate dateEndSidang = timeline.getEndSidang();
            LocalDateTime datePengSidang = timeline.getPengumumanSidang();
            LocalDate dateRegUgb = timeline.getRegUGB();
            LocalDateTime datePengUgb = timeline.getPengumumanUGB();
            LocalDate dateRegSempro = timeline.getRegSempro();
            LocalDate dateStartSempro = timeline.getStartSempro();
            LocalDate dateEndSempro = timeline.getEndSempro();
            LocalDateTime datePengSempro = timeline.getPengumumanSempro();
            LocalDate dateRegSemhas = timeline.getRegSemhas();
            LocalDate dateStartSemhas = timeline.getStartSemhas();
            LocalDate dateEndSemhas = timeline.getEndSemhas();
            LocalDateTime datePengSemhas = timeline.getPengumumanSemhas();
            LocalDate dateRegValTa = timeline.getRegValidasiTA();
            LocalDate datePengTa = timeline.getPengumpulanTA();

            String reg_sidang = "-";
            String pel_sidang = "-";
            String peng_sidang = "-";
            String reg_ugb = "-";
            String peng_ugb = "-";
            String reg_sempro = "-";
            String pel_sempro = "-";
            String peng_sempro = "-";
            String reg_semhas = "-";
            String pel_semhas = "-";
            String peng_semhas = "-";
            String reg_val_ta = "-";
            String peng_ta = "-";

            if(dateRegSidang != null){
                reg_sidang = dateRegSidang.format(ft);
            }
            if(dateStartSidang != null && dateEndSidang != null){
                pel_sidang = Integer.toString(dateStartSidang.getDayOfMonth()) + "-"
                            + Integer.toString(dateEndSidang.getDayOfMonth()) + " "  
                            + StringUtils.capitalize(dateStartSidang.getMonth().toString().toLowerCase()) + " "
                            + Integer.toString(dateStartSidang.getYear());
            }
            if(datePengSidang != null){
                peng_sidang = datePengSidang.format(ft) + " " 
                            + Integer.toString(datePengSidang.getHour()) + ":"
                            + Integer.toString(datePengSidang.getMinute());
                
                if(datePengSidang.getHour() > 11){
                    peng_sidang += " PM";
                }else{
                    peng_sidang += " AM";
                }
            }

            if(dateRegUgb != null){
                reg_ugb = dateRegUgb.format(ft);
            }
            if(datePengUgb != null){
                peng_ugb = datePengUgb.format(ft) + " " 
                            + Integer.toString(datePengUgb.getHour()) + ":"
                            + Integer.toString(datePengUgb.getMinute());
                
                if(datePengUgb.getHour() > 11){
                    peng_ugb += " PM";
                }else{
                    peng_ugb += " AM";
                }
            }

            if(dateRegSempro != null){
                reg_sempro = dateRegSempro.format(ft);
            }
            if(dateStartSempro != null && dateEndSempro != null){
                pel_sempro = Integer.toString(dateStartSempro.getDayOfMonth()) + "-"
                            + Integer.toString(dateEndSempro.getDayOfMonth()) + " "  
                            + StringUtils.capitalize(dateStartSempro.getMonth().toString().toLowerCase()) + " "
                            + Integer.toString(dateStartSempro.getYear());
            }
            if(datePengSempro != null){
                peng_sempro = datePengSempro.format(ft) + " " 
                            + Integer.toString(datePengSempro.getHour()) + ":"
                            + Integer.toString(datePengSempro.getMinute());
                
                if(datePengSempro.getHour() > 11){
                    peng_sempro += " PM";
                }else{
                    peng_sempro += " AM";
                }
            }

            if(dateRegSemhas != null){
                reg_semhas = dateRegSemhas.format(ft);
            }
            if(dateStartSemhas != null && dateEndSemhas != null){
                pel_semhas = Integer.toString(dateStartSemhas.getDayOfMonth()) + "-"
                            + Integer.toString(dateEndSemhas.getDayOfMonth()) + " "  
                            + StringUtils.capitalize(dateStartSemhas.getMonth().toString().toLowerCase()) + " "
                            + Integer.toString(dateStartSemhas.getYear());
            }
            if(datePengSemhas != null){
                peng_semhas = datePengSemhas.format(ft) + " " 
                            + Integer.toString(datePengSemhas.getHour()) + ":"
                            + Integer.toString(datePengSemhas.getMinute());
                
                if(datePengSemhas.getHour() > 11){
                    peng_semhas += " PM";
                }else{
                    peng_semhas += " AM";
                }
            }

            if(dateRegValTa != null){
                reg_val_ta = dateRegValTa.format(ft);
            }

            if(datePengTa != null){
                peng_ta = datePengTa.format(ft);
            }

            // System.out.println("reg_sidang: "+reg_sidang);
            // System.out.println("pel_sidang: "+pel_sidang);
            // System.out.println("peng_sidang: "+peng_sidang);

            model.addAttribute("timeline", timeline);
            model.addAttribute("listTimeline", listTl);
            model.addAttribute("reg_sidang", reg_sidang);
            model.addAttribute("pel_sidang", pel_sidang);
            model.addAttribute("peng_sidang", peng_sidang);
            model.addAttribute("reg_ugb", reg_ugb);
            model.addAttribute("peng_ugb", peng_ugb);
            model.addAttribute("reg_sempro", reg_sempro);
            model.addAttribute("pel_sempro", pel_sempro);
            model.addAttribute("peng_sempro", peng_sempro);
            model.addAttribute("reg_semhas", reg_semhas);
            model.addAttribute("pel_semhas", pel_semhas);
            model.addAttribute("peng_semhas", peng_semhas);
            model.addAttribute("reg_val_ta", reg_val_ta);
            model.addAttribute("peng_ta", peng_ta);
            return "timeline/read-tl";
        }else{
            return "timeline/read-tl";
        }
        
    }

    @GetMapping("/timeline/{periode}")
    public String readCertainTimelinePage(@PathVariable String periode, Model model){
        TimelineModel tlUpdate = timelineDb.findByPeriode(periode);
        List<TimelineModel> listTl = timelineDb.findAll();
        Collections.sort(listTl , (a1, a2) -> a1.getPeriode().compareTo(a2.getPeriode()));


        DateTimeFormatter ft = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.forLanguageTag("in-ID"));
        TimelineModel timeline = tlUpdate;

        LocalDate dateRegSidang = timeline.getRegSidang();
        LocalDate dateStartSidang = timeline.getStartSidang();
        LocalDate dateEndSidang = timeline.getEndSidang();
        LocalDateTime datePengSidang = timeline.getPengumumanSidang();
        LocalDate dateRegUgb = timeline.getRegUGB();
        LocalDateTime datePengUgb = timeline.getPengumumanUGB();
        LocalDate dateRegSempro = timeline.getRegSempro();
        LocalDate dateStartSempro = timeline.getStartSempro();
        LocalDate dateEndSempro = timeline.getEndSempro();
        LocalDateTime datePengSempro = timeline.getPengumumanSempro();
        LocalDate dateRegSemhas = timeline.getRegSemhas();
        LocalDate dateStartSemhas = timeline.getStartSemhas();
        LocalDate dateEndSemhas = timeline.getEndSemhas();
        LocalDateTime datePengSemhas = timeline.getPengumumanSemhas();
        LocalDate dateRegValTa = timeline.getRegValidasiTA();
        LocalDate datePengTa = timeline.getPengumpulanTA();

        String reg_sidang = "-";
        String pel_sidang = "-";
        String peng_sidang = "-";
        String reg_ugb = "-";
        String peng_ugb = "-";
        String reg_sempro = "-";
        String pel_sempro = "-";
        String peng_sempro = "-";
        String reg_semhas = "-";
        String pel_semhas = "-";
        String peng_semhas = "-";
        String reg_val_ta = "-";
        String peng_ta = "-";

        if(dateRegSidang != null){
            reg_sidang = dateRegSidang.format(ft);
        }
        if(dateStartSidang != null && dateEndSidang != null){
            pel_sidang = Integer.toString(dateStartSidang.getDayOfMonth()) + "-"
                        + Integer.toString(dateEndSidang.getDayOfMonth()) + " "  
                        + StringUtils.capitalize(dateStartSidang.getMonth().toString().toLowerCase()) + " "
                        + Integer.toString(dateStartSidang.getYear());
        }
        if(datePengSidang != null){
            peng_sidang = datePengSidang.format(ft) + " " 
                        + Integer.toString(datePengSidang.getHour()) + ":"
                        + Integer.toString(datePengSidang.getMinute());
            
            if(datePengSidang.getHour() > 11){
                peng_sidang += " PM";
            }else{
                peng_sidang += " AM";
            }
        }

        if(dateRegUgb != null){
            reg_ugb = dateRegUgb.format(ft);
        }
        if(datePengUgb != null){
            peng_ugb = datePengUgb.format(ft) + " " 
                        + Integer.toString(datePengUgb.getHour()) + ":"
                        + Integer.toString(datePengUgb.getMinute());
            
            if(datePengUgb.getHour() > 11){
                peng_ugb += " PM";
            }else{
                peng_ugb += " AM";
            }
        }

        if(dateRegSempro != null){
            reg_sempro = dateRegSempro.format(ft);
        }
        if(dateStartSempro != null && dateEndSempro != null){
            pel_sempro = Integer.toString(dateStartSempro.getDayOfMonth()) + "-"
                        + Integer.toString(dateEndSempro.getDayOfMonth()) + " "  
                        + StringUtils.capitalize(dateStartSempro.getMonth().toString().toLowerCase()) + " "
                        + Integer.toString(dateStartSempro.getYear());
        }
        if(datePengSempro != null){
            peng_sempro = datePengSempro.format(ft) + " " 
                        + Integer.toString(datePengSempro.getHour()) + ":"
                        + Integer.toString(datePengSempro.getMinute());
            
            if(datePengSempro.getHour() > 11){
                peng_sempro += " PM";
            }else{
                peng_sempro += " AM";
            }
        }

        if(dateRegSemhas != null){
            reg_semhas = dateRegSemhas.format(ft);
        }
        if(dateStartSemhas != null && dateEndSemhas != null){
            pel_semhas = Integer.toString(dateStartSemhas.getDayOfMonth()) + "-"
                        + Integer.toString(dateEndSemhas.getDayOfMonth()) + " "  
                        + StringUtils.capitalize(dateStartSemhas.getMonth().toString().toLowerCase()) + " "
                        + Integer.toString(dateStartSemhas.getYear());
        }
        if(datePengSemhas != null){
            peng_semhas = datePengSemhas.format(ft) + " " 
                        + Integer.toString(datePengSemhas.getHour()) + ":"
                        + Integer.toString(datePengSemhas.getMinute());
            
            if(datePengSemhas.getHour() > 11){
                peng_semhas += " PM";
            }else{
                peng_semhas += " AM";
            }
        }

        if(dateRegValTa != null){
            reg_val_ta = dateRegValTa.format(ft);
        }

        if(datePengTa != null){
            peng_ta = datePengTa.format(ft);
        }

        model.addAttribute("timeline", tlUpdate);
        model.addAttribute("listTimeline", listTl);
        model.addAttribute("reg_sidang", reg_sidang);
        model.addAttribute("pel_sidang", pel_sidang);
        model.addAttribute("peng_sidang", peng_sidang);
        model.addAttribute("reg_ugb", reg_ugb);
        model.addAttribute("peng_ugb", peng_ugb);
        model.addAttribute("reg_sempro", reg_sempro);
        model.addAttribute("pel_sempro", pel_sempro);
        model.addAttribute("peng_sempro", peng_sempro);
        model.addAttribute("reg_semhas", reg_semhas);
        model.addAttribute("pel_semhas", pel_semhas);
        model.addAttribute("peng_semhas", peng_semhas);
        model.addAttribute("reg_val_ta", reg_val_ta);
        model.addAttribute("peng_ta", peng_ta);
        return "timeline/read-tl";
    }

    
    @GetMapping("/timeline/add")
    public String addTimelineFormPage(Model model){
        TimelineModel tl = new TimelineModel();
        model.addAttribute("timeline", tl);
        return "timeline/add-tl-form";
    }
    

    @PostMapping("/timeline/add")
    public String addTimelineSubmitPage(@ModelAttribute TimelineModel tl){
        

        System.out.println("*** masuk ***");
        // System.out.println("*** periode: "+ periode);
        // System.out.println("*** regsidang: "+ regSidang);

        // tl.setPeriode(periode);
        // LocalDate dateRegSidang = LocalDate.parse(regSidang);
        // tl.setRegSidang(dateRegSidang);
        System.out.println("pengumuman :" + tl.getPengumumanSidang());
        System.out.println("pengumuman :" + tl.getPengumumanSempro());
        System.out.println("pengumuman :" + tl.getPengumumanSemhas());
        // TimelineModel existingTl = timelineDb.findByPeriode(tl.getPeriode());
        // if(existingTl != )
        timelineDb.save(tl);
        return "redirect:/";
    }

    @GetMapping("/timeline/update")
    public String updateTimelineFormPage(Model model){
        List<TimelineModel> listTl = timelineDb.findAll();
        Collections.sort(listTl , (a1, a2) -> a1.getPeriode().compareTo(a2.getPeriode()));

        int indexNow = listTl.size() - 1;
        LocalDate dateNow = LocalDate.now();
        String month = Integer.toString(dateNow.getMonthValue());
        String year = Integer.toString(dateNow.getYear());
        String periodeNow = year+"-"+month;
        System.out.println("nowPeriode: "+periodeNow);
        // TimelineModel time

        for(int i = 0; i < listTl.size(); i++){
            System.out.println("periode_in_list: "+listTl.get(i).getPeriode());
            if(listTl.get(i).getPeriode().equals(periodeNow)){
                indexNow = i;
            }
        }


        if(!listTl.isEmpty()){
            model.addAttribute("timeline", listTl.get(indexNow));
        }else{
            TimelineModel newTl = new TimelineModel();

            model.addAttribute("timeline", newTl);
        }


        model.addAttribute("listTimeline", listTl);

        return "timeline/update-tl-form";
    }

    @GetMapping("/timeline/update/{periode}")
    public String updateCertainTimelineFormPage(@PathVariable String periode, Model model){
        TimelineModel tlUpdate = timelineDb.findByPeriode(periode);
        List<TimelineModel> listTl = timelineDb.findAll();
        Collections.sort(listTl , (a1, a2) -> a1.getPeriode().compareTo(a2.getPeriode()));


        model.addAttribute("timeline", tlUpdate);
        model.addAttribute("listTimeline", listTl);

        return "timeline/update-tl-form";
    }
    
    @PostMapping("/timeline/update")
    public String updateTimelineSubmitPage(@ModelAttribute TimelineModel tl){
        

        System.out.println("*** masuk ***");
        TimelineModel existingTl = timelineDb.findByPeriode(tl.getPeriode());
        existingTl.setRegSidang(tl.getRegSidang());
        existingTl.setStartSidang(tl.getStartSidang());
        existingTl.setEndSidang(tl.getEndSidang());
        existingTl.setPengumumanSidang(tl.getPengumumanSidang());
        existingTl.setRegUGB(tl.getRegUGB());
        existingTl.setPengumumanUGB(tl.getPengumumanUGB());
        existingTl.setRegSempro(tl.getRegSempro());
        existingTl.setStartSempro(tl.getStartSempro());
        existingTl.setEndSempro(tl.getEndSempro());
        existingTl.setPengumumanSempro(tl.getPengumumanSempro());
        existingTl.setRegSemhas(tl.getRegSemhas());
        existingTl.setStartSemhas(tl.getStartSemhas());
        existingTl.setEndSemhas(tl.getEndSemhas());
        existingTl.setPengumumanSemhas(tl.getPengumumanSemhas());
        existingTl.setRegValidasiTA(tl.getRegValidasiTA());
        existingTl.setPengumpulanTA(tl.getPengumpulanTA());

        timelineDb.save(existingTl);
        return "redirect:/";
    }
}
