package protensi.sita.repository;

import protensi.sita.model.EnumRole;
import protensi.sita.model.UserModel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDb extends JpaRepository<UserModel, Long> {
    UserModel findByNama(String name);

    UserModel findByIdUser(Long idUser);

    List<UserModel> findAllByRoles(EnumRole roles);

    UserModel findByUsername(String username);
}
