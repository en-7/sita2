package protensi.sita.model;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "evaluasi_ugb")
public class EvaluasiUgbModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluasi_ugb", updatable = false, nullable = false)
    private Long idEvaluasiUgb;

    @Lob
    @Column(name = "catatan_judul", nullable = false)
    private String catatanJudul;

    @Column(name = "nilai_judul")
    private Long nilaiJudul;

    @Lob
    @Column(name = "latar_belakang", nullable = false)
    private String latarBelakang;

    @Column(name = "nilai_latar_belakang")
    private Long nilaiLatarBelakang;

    @Lob
    @Column(name = "tujuan_manfaat", nullable = false)
    private String tujuanManfaat;

    @Column(name = "nilai_tujuan_manfaat")
    private Long nilaiTujuanManfaat;

    @Lob
    @Column(name = "ruang_lingkup", nullable = false)
    private String ruangLingkup;

    @Column(name = "nilai_ruang_lingkup")
    private Long nilaiRuangLingkup;

    @Lob
    @Column(name = "keterbaruan", nullable = false)
    private String keterbaruan;

    @Column(name = "nilai_keterbaruan")
    private Long nilaiKeterbaruan;

    @Lob
    @Column(name = "metodologi", nullable = false)
    private String metodologi;

    @Column(name = "nilai_metodologi")
    private Long nilaiMetodologi;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @OneToOne
    @JoinColumn(name = "id_ugb")
    private UgbModel ugb;
}