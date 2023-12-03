package protensi.sita.service;

import protensi.sita.model.JadwalSidangModel;

import java.util.List;

public interface JadwalSidangSeminarService {

    // Method get List Mahasiswa mendaftar Sidang/Seminar;
    List<JadwalSidangModel> getListJadwalSidang();

    // Method getUserbyId
    JadwalSidangModel getJadwalSidangById(long id);

    // Method set Jadwal Sempro
    JadwalSidangModel setJadwalSidang(JadwalSidangModel jadwalSidangSeminar);

    // Method delete penjadwalan Sempro
    void deletesJadwalSidangSeminar(Long id);

    // add-sempro
    void addJadwalSidangSeminar(JadwalSidangModel jadwalSidangSeminar);

    void saverid(JadwalSidangModel jadwalSidangModel);

    // add-semhas
    void addJadwalSemhas(JadwalSidangModel jadwalSidangSemhas);

    // add-sidang
    void addJadwalSidangTa(JadwalSidangModel jadwalSidangTa);

}
