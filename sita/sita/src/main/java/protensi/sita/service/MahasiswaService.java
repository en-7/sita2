package protensi.sita.service;

import java.util.List;

import protensi.sita.model.MahasiswaModel;

public interface MahasiswaService {
    MahasiswaModel findMahasiswaById(Long idUser);

    MahasiswaModel addMahasiswa(MahasiswaModel mahasiswa);

    MahasiswaModel updateMahasiswa(MahasiswaModel mahasiswa);

    List<MahasiswaModel> findAllMahasiswa();

    MahasiswaModel findMahasiswaByUsername(String username);
}