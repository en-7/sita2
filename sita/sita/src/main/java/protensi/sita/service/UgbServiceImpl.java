package protensi.sita.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import protensi.sita.model.EnumRole;
import protensi.sita.model.MahasiswaModel;
import protensi.sita.model.PembimbingModel;
import protensi.sita.model.SeminarProposalModel;
import protensi.sita.model.TugasAkhirModel;
import protensi.sita.model.UgbModel;
import protensi.sita.model.EvaluasiUgbModel;
import protensi.sita.model.UserModel;
import protensi.sita.repository.MahasiswaDb;
import protensi.sita.repository.UgbDb;
import protensi.sita.repository.EvaluasiUgbDb;
import protensi.sita.repository.UserDb;
import reactor.netty.http.server.HttpServerResponse;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class UgbServiceImpl {
    @Autowired
    UgbDb ugbDb;

    @Autowired
    EvaluasiUgbDb evaluasiUgbDb;

    @Autowired
    MahasiswaDb mahasiswaDb;

    @Autowired
    UserDb userDb;

    public List<UserModel> getListPembimbing() {
        List<UserModel> listUser = userDb.findAll();
        List<UserModel> listPembimbing = new ArrayList<>();

        for (UserModel u : listUser) {
            Set<EnumRole> roles = u.getRoles();
            if (roles.contains(EnumRole.PEMBIMBING) == true) {
                listPembimbing.add(u);
            }
        }
        return listPembimbing;
    }

    public List<UserModel> getListPenguji() {
        List<UserModel> listPenguji = new ArrayList<>();
        List<UserModel> listUser = userDb.findAll();

        for (UserModel i : listUser) {
            Set<EnumRole> roles = i.getRoles();
            if (roles.contains(EnumRole.PENGUJI) == true) {
                listPenguji.add(i);
            }
        }
        return listPenguji;
    }

    public UserModel getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserModel thisUser = userDb.findByUsername(username);
        return thisUser;
    }

    public UgbModel getAddFormObjects() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        MahasiswaModel thisMahasiswa = mahasiswaDb.findByUsername(username);
        UgbModel retrievedUgb = findByIdMahasiswa(thisMahasiswa);

        if (retrievedUgb != null) {
            return retrievedUgb;

        } else {
            UgbModel ugbModel = new UgbModel();
            return ugbModel;
        }
    }

    public void updateUgbKoordinator(Long idUgb, Long idP1, Long idP2) {
        UgbModel ugb = getUgbById(idUgb);
        Set<UserModel> set_pembimbing = new HashSet<>();
        UserModel pembimbing1 = userDb.findByIdUser(idP1);
        UserModel pembimbing2 = userDb.findByIdUser(idP2);

        set_pembimbing.add(pembimbing1);
        set_pembimbing.add(pembimbing2);
        System.out.println("nama pemb_1 = " + pembimbing1.getNama());
        System.out.println("nama pemb_2 = " + pembimbing2.getNama());

        ugb.setPembimbing(set_pembimbing);
        ugbDb.save(ugb);
    }

    public void updateUGBKoordinatorforPenguji(Long idUgb, Long idPJ1, Long idPJ2) {
        UgbModel ugb = getUgbById(idUgb);
        Set<UserModel> set_penguji = new HashSet<>();
        UserModel penguji1 = userDb.findByIdUser(idPJ1);
        UserModel penguji2 = userDb.findByIdUser(idPJ2);

        set_penguji.add(penguji1);
        set_penguji.add(penguji2);

        ugb.setPenguji(set_penguji);
        ugb.setStatusUgb("ALLOCATED");
        ugbDb.save(ugb);
    }

    public void evaluasiUgb(UgbModel ugb) {
        ugb.setStatusDokumen("DIEVALUASI");
        ugb.setStatusUgb("DIEVALUASI");
        ugbDb.save(ugb);
    }

    public String addCatatanUgb(EvaluasiUgbModel idUgb, String catatanJudul, Long nilaiJudul, String latarBelakang,
            Long nilaiLatarBelakang, String tujuanManfaat, Long nilaiTujuanManfaat,
            String ruangLingkup, Long nilaiRuangLingkup, String keterbaruan, Long nilaiKeterbaruan, String metodologi,
            Long nilaiMetodologi, UgbModel ugb) {

        evaluasiUgbDb.save(idUgb);
        return "success";
    }

    public void updateUgbMahasiswa(Long idUgb, String judul, MultipartFile bukti_kp, MultipartFile transcript,
            MultipartFile file_khs, MultipartFile file_ugb) {

        UgbModel ugb = getUgbById(idUgb);
        try {
            // System.out.println("kp: "+bukti_kp);
            // System.out.println("transkrip: "+transcript);
            // System.out.println("is transkrip empty?: "+transcript.isEmpty());

            if (!bukti_kp.isEmpty()) {
                String namaFileKp = StringUtils.cleanPath(bukti_kp.getOriginalFilename());
                ugb.setNameFileKp(namaFileKp);
                ugb.setBuktiKp(bukti_kp.getBytes());
            }
            if (!transcript.isEmpty()) {
                String namaFileTranskrip = StringUtils.cleanPath(transcript.getOriginalFilename());
                ugb.setNameFileTranskrip(namaFileTranskrip);
                ugb.setTranskrip(transcript.getBytes());
            }
            if (!file_khs.isEmpty()) {
                String namaFileKhs = StringUtils.cleanPath(file_khs.getOriginalFilename());
                ugb.setNameFileKhs(namaFileKhs);
                ugb.setFileKhs(file_khs.getBytes());
            }
            if (!file_ugb.isEmpty()) {
                String namaFileUgb = StringUtils.cleanPath(file_ugb.getOriginalFilename());
                ugb.setNameFileUgb(namaFileUgb);
                ugb.setFileUgb(file_ugb.getBytes());
            }
            if (judul != null) {
                ugb.setJudulUgb(judul);
            }
            ugb.setStatusDokumen("SUBMITTED");
            ugb.setStatusUgb("SUBMITTED");
            ugb.setCatatan(null);
            ugbDb.save(ugb);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Document");
        }
    }

    public String addUgb(UgbModel ugb, MultipartFile bukti_kp, MultipartFile transcript, MultipartFile file_khs,
            MultipartFile file_ugb) {
        try {
            String namaFileKp = StringUtils.cleanPath(bukti_kp.getOriginalFilename());
            String namaFileTranskrip = StringUtils.cleanPath(transcript.getOriginalFilename());
            String namaFileKhs = StringUtils.cleanPath(file_khs.getOriginalFilename());
            String namaFileUgb = StringUtils.cleanPath(file_ugb.getOriginalFilename());

            ugb.setNameFileKp(namaFileKp);
            ugb.setNameFileTranskrip(namaFileTranskrip);
            ugb.setNameFileKhs(namaFileKhs);
            ugb.setNameFileUgb(namaFileUgb);
            ugb.setBuktiKp(bukti_kp.getBytes());
            ugb.setTranskrip(transcript.getBytes());
            ugb.setFileKhs(file_khs.getBytes());
            ugb.setFileUgb(file_ugb.getBytes());
            ugb.setStatusUgb("SUBMITTED");
            ugb.setStatusDokumen("SUBMITTED");
            UserModel user = getCurrentUser();
            // System.out.println("### id mahassiswa noww: "+ user.getIdUser());

            MahasiswaModel mahasiswa = mahasiswaDb.findByIdUser(user.getIdUser());
            // System.out.println("### mahasiswa noww: "+mahasiswa);
            ugb.setMahasiswa(mahasiswa);

            Set<UserModel> set_pembimbing = new HashSet<>();
            mahasiswa.setTahap("UGB");
            set_pembimbing.add(userDb.findByIdUser(ugb.getIdPembimbing1()));
            set_pembimbing.add(userDb.findByIdUser(ugb.getIdPembimbing2()));
            ugb.setPembimbing(set_pembimbing);

            ugbDb.save(ugb);
            return "success";
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Document");
        }

    }

    public List<UgbModel> viewAllUgb() {
        UserModel thisUser = getCurrentUser();
        Set<EnumRole> roles = thisUser.getRoles();
        System.out.println("*** roles user == " + roles.toString());

        if (roles.contains(EnumRole.PEMBIMBING) == true) {
            // *** the default filter would be 'BIMBINGAN'
            // *** returns the default list for dosen with role [PEMBIMBING] or [PEMBIMBING,
            // PENGUJI]
            // *** list contains ugb mahasiswa bimbingan
            // List<UgbModel> submitted_ugb = ugbDb.findAll();
            // System.out.println("@@@@ initial list ugb pembimbing = "+submitted_ugb);
            // for(UgbModel u : submitted_ugb){
            // System.out.println("@@@@ UGB_now = "+u.getJudulUgb());
            // if(u.getPembimbing().contains(thisUser) == true){
            // retrievedUgb.add(u);
            // System.out.println("@@@@ ugb dgn judul -"+u.getJudulUgb()+"- ditambah ke
            // list");
            // }
            // }
            // return retrievedUgb;
            return filterUgb("SUBMITTED");
        } else if (roles.contains(EnumRole.PENGUJI) == true) {
            // *** the default filter would be 'EVALUATE'
            // *** returns the default list for dosen with role [PENGUJI]
            // *** list contains ugb with status 'EVALUATE'
            // List<UgbModel> submitted_ugb = ugbDb.getUgbBasedOnStatus("EVALUATE");
            // System.out.println("@@@@ initial list ugb penguji = "+submitted_ugb);
            // for(UgbModel u : submitted_ugb){
            // System.out.println("@@@@ UGB_now = "+u.getJudulUgb());
            // if(u.getPembimbing().contains(thisUser) == true){
            // retrievedUgb.add(u);
            // System.out.println("@@@@ ugb dgn judul -"+u.getJudulUgb()+"- ditambah ke
            // list");
            // }

            // }
            // return retrievedUgb;
            return filterUgb("EVALUATE");
        } else {
            // *** the default filter would be 'VERIFY'
            // returns the default list for dosen with role [KOORDINATOR]
            // *** list contains ugb with status 'SUBMITTED'
            // List<UgbModel> submitted_ugb = ugbDb.getUgbBasedOnStatus("APPROVE");
            // System.out.println("@@@@ initial list ugb koordinator = "+submitted_ugb);
            // return retrievedUgb;
            return filterUgb("SUBMITTED");
        }
    }

    public List<UgbModel> filterUgb(String status) {
        UserModel thisUser = getCurrentUser();
        Set<EnumRole> roles = thisUser.getRoles();
        System.out.println("*** STATUS == " + status);
        System.out.println("*** ROLES == " + roles.toString());

        if (roles.contains(EnumRole.KOORDINATOR)) {
            List<UgbModel> submitted_ugb = ugbDb.getUgbBasedOnStatus(status);
            return submitted_ugb;
        } else if (roles.contains(EnumRole.PENGUJI) && status.equals("EVALUATE") || status.equals("EVALUATED")) {
            // as penguji
            System.out.println("masuk else if penguji");
            List<UgbModel> submitted_ugb = ugbDb.getUgbBasedOnStatus(status);
            List<UgbModel> retrievedUgb = new ArrayList<>();
            for (UgbModel u : submitted_ugb) {
                System.out.println("@@@@ UGB_now = " + u.getJudulUgb());
                if (u.getPenguji().contains(thisUser) == true) {
                    retrievedUgb.add(u);
                    System.out.println("@@@@ ugb dgn judul -" + u.getJudulUgb() + "- ditambah ke list");
                }
            }
            return retrievedUgb;
        } else {
            List<UgbModel> submitted_ugb = ugbDb.findAll();
            List<UgbModel> retrievedUgb = new ArrayList<>();

            for (UgbModel u : submitted_ugb) {
                System.out.println("@@@@ UGB_now pembimbing = " + u.getJudulUgb());
                if (u.getPembimbing().contains(thisUser) == true) {
                    retrievedUgb.add(u);
                    System.out.println("@@@@ ugb dgn judul -" + u.getJudulUgb() + "- ditambah ke list");
                }
            }
            return retrievedUgb;
        }

    }

    public UgbModel getUgbById(Long idUgb) {
        return ugbDb.findByIdUgb(idUgb);
    }

    public EvaluasiUgbModel getEvaluasiUgbById(Long idEvaluasiUgb) {
        return evaluasiUgbDb.findByIdEvaluasiUgb(idEvaluasiUgb);
    }

    public void approveUgb(UgbModel ugb) {
        ugb.setStatusDokumen("APPROVED");
        ugb.setStatusUgb("ALLOCATE");
        ugbDb.save(ugb);
    }

    public void denyUgb(UgbModel ugb, String ctt) {
        ugb.setStatusDokumen("DENIED");
        ugb.setStatusUgb("DENIED");
        ugb.setCatatan(ctt);
        ugbDb.save(ugb);
    }

    public UgbModel findByIdMahasiswa(MahasiswaModel mahasiswa) {
        return ugbDb.findByMahasiswa(mahasiswa);
    }

    public void downloadUgbFiles(String type, Long id, HttpServletResponse response) {
        try {
            UgbModel retrievedUgb = getUgbById(id);
            response.setContentType("application/ocetet-stream");
            String headerKey = "Content-Disposition";

            if (type.equals("FILE UGB")) {
                String headerValue = "attachment; filename=" + retrievedUgb.getNameFileUgb();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedUgb.getFileUgb());
                outputStream.close();
            } else if (type.equals("FILE KHS")) {
                String headerValue = "attachment; filename=" + retrievedUgb.getNameFileKhs();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedUgb.getFileKhs());
                outputStream.close();
            } else if (type.equals("FILE KP")) {
                String headerValue = "attachment; filename=" + retrievedUgb.getNameFileKp();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedUgb.getBuktiKp());
                outputStream.close();
            } else {
                String headerValue = "attachment; filename=" + retrievedUgb.getNameFileTranskrip();
                response.setHeader(headerKey, headerValue);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(retrievedUgb.getTranskrip());
                outputStream.close();
            }

        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error while saving the file.");
        }
    }
}
