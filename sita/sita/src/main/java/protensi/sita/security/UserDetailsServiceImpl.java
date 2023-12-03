package protensi.sita.security;

import protensi.sita.model.EnumRole;
import protensi.sita.model.MahasiswaModel;
import protensi.sita.model.AdminModel;
import protensi.sita.model.UserModel;
import protensi.sita.repository.AdminDb;
import protensi.sita.repository.MahasiswaDb;
import protensi.sita.repository.UserDb;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserDb userDb;

    @Autowired
    private MahasiswaDb mahasiswaDb;

    @Autowired
    private AdminDb adminDb;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(UserDb userDatabase) {
        super();
        this.userDb = userDatabase;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userDb.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
        System.out.println("###########" + user.getRoles());
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRoles()));
        return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }

    public void addDummy() {
        // Set<EnumRole> roles_koordinator = new HashSet<EnumRole>();
        // roles_koordinator.add(EnumRole.KOORDINATOR);
        // UserModel koordinator = new UserModel("koordinator", roles_koordinator,
        // "koordinator",
        // passwordEncoder.encode("koordinator"), "koordinator@gmail.com");
        // userDb.save(koordinator);

        // Set<EnumRole> roles_admin = new HashSet<EnumRole>();
        // roles_admin.add(EnumRole.ADMIN);
        // AdminModel admin = new AdminModel("admin", roles_admin, "admin",
        // passwordEncoder.encode("admin"),
        // "admin@gmail.com");
        // adminDb.save(admin);

        // Set<EnumRole> roles_pembimbing = new HashSet<EnumRole>();
        // roles_pembimbing.add(EnumRole.PEMBIMBING);
        // UserModel pembimbing = new UserModel("pembimbing", roles_pembimbing,
        // "pembimbing",
        // passwordEncoder.encode("pembimbing"), "pembimbing@gmail.com");
        // userDb.save(pembimbing);

        // Set<EnumRole> roles_penguji = new HashSet<EnumRole>();
        // roles_penguji.add(EnumRole.PENGUJI);
        // UserModel penguji = new UserModel("penguji", roles_penguji, "penguji",
        // passwordEncoder.encode("penguji"),
        // "penguji@gmail.com");
        // userDb.save(penguji);

        // Set<EnumRole> roles_mahasiswa = new HashSet<EnumRole>();
        // roles_mahasiswa.add(EnumRole.MAHASISWA);
        // UserModel mahasiswa = new UserModel("mahasiswa", roles_mahasiswa,
        // "mahasiswa",
        // passwordEncoder.encode("mahasiswa"), "mahasiswa@gmail.com");
        // userDb.save(mahasiswa);
        // MahasiswaModel m = new MahasiswaModel(2123456789, "NONE");
        // mahasiswaDb.save(m);

    }

    public UserModel findByUsername(String username) {
        return userDb.findByUsername(username);
    }

}
