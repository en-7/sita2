package protensi.sita.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import protensi.sita.model.EnumRole;
import protensi.sita.model.UserModel;
import protensi.sita.repository.UserDb;

import java.util.List;

@Service
@Transactional
public class ManageUserServiceImpl implements ManageUserService {
    @Autowired
    UserDb userDb;

    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    @Override
    public UserModel addUser(UserModel user) {
        String password = encrypt(user.getPassword());
        user.setPassword(password);
        return userDb.save(user);
    }

    @Override
    public UserModel updateUser(UserModel user) {
        String password = encrypt(user.getPassword());
        user.setPassword(password);
        userDb.save(user);
        return user;
    }

    public List<UserModel> findAllUser() {
        return userDb.findAll();
    }

    public UserModel findUserById(Long idUser) {
        UserModel user = userDb.findByIdUser(idUser);
        if (user != null) {
            return user;
        } else
            return null;
    }

    public List<UserModel> findUserByRoles(EnumRole roles) {
        return userDb.findAllByRoles(roles);
    }

}
