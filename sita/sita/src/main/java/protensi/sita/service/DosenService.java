package protensi.sita.service;

import java.util.List;

import protensi.sita.model.DosenModel;

public interface DosenService {
    DosenModel findDosenById(Long idUser);

    DosenModel addDosen(DosenModel dosen);

    DosenModel updateDosen(DosenModel dosen);

    List<DosenModel> findAllDosen();

    DosenModel findDosenByUsername(String username);
}