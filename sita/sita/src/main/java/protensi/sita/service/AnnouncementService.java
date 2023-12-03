package protensi.sita.service;

import java.util.*;
import protensi.sita.model.AnnouncementModel;

public interface AnnouncementService {
      void addAnnouncement(AnnouncementModel announcement);
      
      List<AnnouncementModel> getListAnnounce();

      AnnouncementModel getAnnounceById(Long id);

      AnnouncementModel updateAnnouncement(AnnouncementModel announcement);

}