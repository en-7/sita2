package protensi.sita.service;

import java.util.List;

import protensi.sita.model.KoordinatorModel;

public interface KoordinatorService {
    KoordinatorModel findKoordinatorById(Long idUser);

    KoordinatorModel addKoordinator(KoordinatorModel koordinator);

    KoordinatorModel updateKoordinator(KoordinatorModel koordinator);

    List<KoordinatorModel> findAllKoordinator();

    KoordinatorModel findKoordinatorByUsername(String username);
}