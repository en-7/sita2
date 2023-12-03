package protensi.sita.model;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "seminar_hasil")
public class SeminarHasilModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seminar_hasil", updatable = false, nullable = false)
    private Long idSeminarHasil;

    @Lob
    @Column(name = "persetujuan_pembimbing", nullable = false)
    private byte[] persetujuanPembimbing;

    @Column(name = "nama_file_persetujuan_pembimbing", nullable = false)
    private String nameFilePersetujuanPembimbing;

    @Lob
    @Column(name = "laporan_kp", nullable = false)
    private byte[] laporanKp;

    @Column(name = "nama_file_laporan_kp", nullable = false)
    private String nameFileLaporanKp;

    @Lob
    @Column(name = "risalah_sempro", nullable = false)
    private byte[] risalahSempro;

    @Column(name = "nama_file_risalah_sempro", nullable = false)
    private String nameFileRisalahSempro;

    @Lob
    @Column(name = "catatan_sempro", nullable = false)
    private byte[] catatanSempro;

    @Column(name = "nama_file_catatan_sempro", nullable = false)
    private String nameFileCatatanSempro;

    @Lob
    @Column(name = "saps", nullable = false)
    private byte[] saps;

    @Column(name = "nama_file_saps", nullable = false)
    private String nameFileSaps;

    @Lob
    @Column(name = "draft_laporan_ta", nullable = false)
    private byte[] draftLaporanTa;

    @Column(name = "nama_file_draft_laporan_ta", nullable = false)
    private String nameFileDraftLaporanTa;

    @Size(max = 100)
    @Column(name = "status_dokumen")
    private String statusDokumen;

    @Size(max = 100)
    @Column(name = "status_semhas")
    private String statusSemhas;

    @Lob
    @Column(name = "catatan")
    private String catatan;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "nilai")
    private Long nilai;

    @Column(name = "nilai_huruf")
    private String nilaiHuruf;

    @Column(name = "tanggal_lulus")
    private LocalDateTime tanggalLulus;

    @OneToOne
    @JoinColumn(name = "id_ugb")
    private UgbModel ugb;

    @OneToOne
    @JoinColumn(name = "id_seminar_proposal")
    private SeminarProposalModel seminarProposal;

    @OneToOne(mappedBy = "seminarHasil")
    private JadwalSidangModel jadwalSidang;

}
