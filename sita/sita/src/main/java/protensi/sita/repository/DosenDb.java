package protensi.sita.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import protensi.sita.model.DosenModel;

public interface DosenDb extends JpaRepository<DosenModel, Long> {
    DosenModel findByIdUser(Long idUser);

    DosenModel findByUsername(String username);

}
