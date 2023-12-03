package protensi.sita.service;
import protensi.sita.repository.TimelineDb;
import protensi.sita.model.TimelineModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.LocalDate;
import java.util.*;


@Service
public class TimelineServiceImpl {
    @Autowired
    TimelineDb tlDb;


    public TimelineModel checkDate(){
        LocalDate nowDate = LocalDate.now();
        System.out.println("nowdate: "+ nowDate);
        Month month = nowDate.getMonth();
        List<TimelineModel> listTl = tlDb.findAll();
        TimelineModel theTl = new TimelineModel();
        for(TimelineModel tl : listTl){
            if(tl.getRegSidang().getMonth().equals(month)){
                System.out.println("the date: "+ tl.getRegUGB());
                System.out.println("the id: "+ tl.getIdTimeline());


                return tl;
            }
        }
        return theTl;
    }
}





