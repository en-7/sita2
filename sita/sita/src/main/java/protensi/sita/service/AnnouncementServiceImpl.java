package protensi.sita.service;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.bytebuddy.asm.Advice.OffsetMapping.Sort;
import protensi.sita.model.AnnouncementModel;
import protensi.sita.repository.AnnouncementDb;

@Service
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    AnnouncementDb announcementDb;

    @Override
    public void addAnnouncement(AnnouncementModel announcement) {
        announcementDb.save(announcement);
    }

    @Override
    public List<AnnouncementModel> getListAnnounce() {
        return announcementDb.findAll();
    }

    @Override
    public AnnouncementModel getAnnounceById(Long id) {
        Optional<AnnouncementModel> announce = announcementDb.findById(id);
        if(announce.isPresent()){
            return announce.get();
        } 
        else return null;
    }

    @Override
    public AnnouncementModel updateAnnouncement(AnnouncementModel announcement) {
        announcementDb.save(announcement);
        return announcement;
        
    }
    


}
