package protensi.sita.service;

import protensi.sita.security.xml.Attributes;
import protensi.sita.security.xml.ServiceResponse;
import protensi.sita.setting.Setting;
import protensi.sita.model.AdminModel;
import protensi.sita.model.MahasiswaModel;
import protensi.sita.model.EnumRole;
import protensi.sita.model.UserModel;
import protensi.sita.repository.AdminDb;
import protensi.sita.repository.MahasiswaDb;
import protensi.sita.repository.UserDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class UserServiceImpl {
    @Autowired
    MahasiswaDb mahasiswaDb;
    @Autowired
    AdminDb adminDb;
    @Autowired
    UserDb userDb;

    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    public boolean matcher(String inputPassword){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserModel thisUser = userDb.findByUsername(username);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();  
        boolean isMatched = encoder.matches(inputPassword, thisUser.getPassword()); 
        return isMatched;
    }

    public UserModel findByUsername(String username) {
        return userDb.findByUsername(username);
    }

    public void updatePass(String newPass){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String encryptedPass = encrypt(newPass);
        UserModel thisUser = userDb.findByUsername(username);
        thisUser.setPassword(encryptedPass);
        userDb.save(thisUser);
        System.out.println("pass changed");
    }


    // public void addDummy() {
    // AdminModel admin = new AdminModel();
    // Set<EnumRole> roleAdmin = new HashSet<EnumRole>();
    // admin.setNama("legend");
    // admin.setUsername("legend");
    // admin.setPassword("$2a$12$bvLzytqbMQTZ.tQTIutpEO.3jNt3MY7f7oYklI5Hk3BJ82wIlusRW");
    // roleAdmin.add(EnumRole.ADMIN);
    // admin.setRoles(roleAdmin);
    // admin.setEmail("legend@email.com");
    // adminDb.save(admin);

    // MahasiswaModel mahasiswa = new MahasiswaModel();
    // Set<EnumRole> roleMahasiswa = new HashSet<EnumRole>();
    // mahasiswa.setNama("hoho");
    // mahasiswa.setUsername("hoho");
    // mahasiswa.setPassword("$2a$12$VNAYAOyvyGmBkH9bfxyiheb8asfOp02unr.GX2rVcCI0eC3jkupK2");
    // roleMahasiswa.add(EnumRole.ADMIN);
    // mahasiswa.setRoles(roleMahasiswa);
    // mahasiswa.setEmail("hoho@email.com");
    // mahasiswa.setNim(001);
    // mahasiswa.setTahap("UGB");
    // mahasiswaDb.save(mahasiswa);
    // }
}
