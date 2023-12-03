package protensi.sita.service;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import protensi.sita.model.KoordinatorModel;
import protensi.sita.repository.KoordinatorDb;

import java.util.List;

@Service
@Transactional
public class KoordinatorServiceImpl implements KoordinatorService {
    @Autowired
    KoordinatorDb koordinatorDb;

    @Override
    public KoordinatorModel findKoordinatorById(Long idUser) {
        return koordinatorDb.findByIdUser(idUser);
    }

    public String encrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    @Override
    public KoordinatorModel addKoordinator(KoordinatorModel koordinator) {
        String password = encrypt(koordinator.getPassword());
        koordinator.setPassword(password);
        return koordinatorDb.save(koordinator);
    }

    @Override
    public KoordinatorModel updateKoordinator(KoordinatorModel koordinator) {
        String password = encrypt(koordinator.getPassword());
        koordinator.setPassword(password);
        koordinatorDb.save(koordinator);
        return koordinator;
    }

    @Override
    public List<KoordinatorModel> findAllKoordinator() {
        return koordinatorDb.findAll();
    }

    @Override
    public KoordinatorModel findKoordinatorByUsername(String username) {
        return koordinatorDb.findByUsername(username);
    }
}
