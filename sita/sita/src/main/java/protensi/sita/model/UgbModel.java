package protensi.sita.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ugb")
public class UgbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ugb", updatable = false, nullable = false)
    private Long idUgb;

    @NotNull
    @Length(max = 500)
    @Column(name = "judul_ugb", nullable = false)
    private String judulUgb;

    @Transient
    private Long idPembimbing1;

    @Transient
    private Long idPembimbing2;

    // @Transient
    // private Long idPenguji1;

    // @Transient
    // private Long idPenguji2;

    @Lob
    @Column(name = "bukti_kp", nullable = false)
    private byte[] buktiKp;

    @Column(name = "nama_file_kp", nullable = false)
    private String nameFileKp;

    @Lob
    @Column(name = "transkrip", nullable = false)
    private byte[] transkrip;

    @Column(name = "nama_file_transkrip", nullable = false)
    private String nameFileTranskrip;

    @Lob
    @Column(name = "file_khs", nullable = false)
    private byte[] fileKhs;

    @Column(name = "nama_file_khs", nullable = false)
    private String nameFileKhs;

    @Lob
    @Column(name = "file_ugb", nullable = false)
    private byte[] fileUgb;

    @Column(name = "nama_file_ugb", nullable = false)
    private String nameFileUgb;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Size(max = 100)
    @Column(name = "status_dokumen", nullable = true)
    private String statusDokumen;

    @Size(max = 100)
    @Column(name = "status_ugb", nullable = true)
    private String statusUgb;

    @Lob
    @Column(name = "catatan", nullable = true)
    private String catatan;

    @Column(name = "tanggal_lulus", nullable = true)
    private LocalDateTime tanggalLulus;

    @OneToOne
    @JoinColumn(name = "id_mahasiswa", nullable = false)
    private MahasiswaModel mahasiswa;

    @ManyToMany
    @JoinTable(name = "ugb_penguji", joinColumns = @JoinColumn(name = "id_ugb"), inverseJoinColumns = @JoinColumn(name = "id_penguji"))
    private Set<UserModel> penguji;

    @ManyToMany
    @JoinTable(name = "ugb_pembimbing", joinColumns = @JoinColumn(name = "id_ugb"), inverseJoinColumns = @JoinColumn(name = "id_pembimbing"))
    private Set<UserModel> pembimbing;
}
