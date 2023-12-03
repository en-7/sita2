package protensi.sita.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import protensi.sita.model.KoordinatorModel;

public interface KoordinatorDb extends JpaRepository<KoordinatorModel, Long> {
    KoordinatorModel findByIdUser(Long idUser);

    KoordinatorModel findByUsername(String username);

}
