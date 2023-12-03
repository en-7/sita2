package protensi.sita.service;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import protensi.sita.model.MahasiswaModel;
import protensi.sita.repository.MahasiswaDb;

import java.util.List;

@Service
@Transactional
public class MahasiswaServiceImpl implements MahasiswaService {
    @Autowired
    MahasiswaDb mahasiswaDb;

    @Override
    public MahasiswaModel findMahasiswaById(Long idUser) {
        return mahasiswaDb.findByIdUser(idUser);
    }

    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    @Override
    public MahasiswaModel addMahasiswa(MahasiswaModel mahasiswa) {
        String password = encrypt(mahasiswa.getPassword());
        mahasiswa.setPassword(password);
        return mahasiswaDb.save(mahasiswa);
    }

    @Override
    public MahasiswaModel updateMahasiswa(MahasiswaModel mahasiswa) {
        String password = encrypt(mahasiswa.getPassword());
        mahasiswa.setPassword(password);
        mahasiswaDb.save(mahasiswa);
        return mahasiswa;
    }

    @Override
    public List<MahasiswaModel> findAllMahasiswa() {
        return mahasiswaDb.findAll();
    }

    @Override
    public MahasiswaModel findMahasiswaByUsername(String username) {
        return mahasiswaDb.findByUsername(username);
    }
}
