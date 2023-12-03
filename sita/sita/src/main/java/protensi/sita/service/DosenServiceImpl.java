package protensi.sita.service;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import protensi.sita.model.DosenModel;
import protensi.sita.repository.DosenDb;

import java.util.List;

@Service
@Transactional
public class DosenServiceImpl implements DosenService {
    @Autowired
    DosenDb dosenDb;

    @Override
    public DosenModel findDosenById(Long idUser) {
        return dosenDb.findByIdUser(idUser);
    }

    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    @Override
    public DosenModel addDosen(DosenModel dosen) {
        String password = encrypt(dosen.getPassword());
        dosen.setPassword(password);
        return dosenDb.save(dosen);
    }

    @Override
    public DosenModel updateDosen(DosenModel dosen) {
        String password = encrypt(dosen.getPassword());
        dosen.setPassword(password);
        dosenDb.save(dosen);
        return dosen;
    }

    @Override
    public List<DosenModel> findAllDosen() {
        return dosenDb.findAll();
    }

    @Override
    public DosenModel findDosenByUsername(String username) {
        return dosenDb.findByUsername(username);
    }
}
