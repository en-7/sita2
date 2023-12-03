package protensi.sita.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jadwal_bimbingan")
public class JadwalBimbinganModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_jadwal_bimbingan", updatable = false, nullable = false)
    private Long idJadwalBimbingan;

    @Column(name = "catatan_bimbingan")
    private String catatanBimbingan;

    @ManyToOne
    @JoinColumn(name = "id_mahasiswa", nullable = false)
    private MahasiswaModel mahasiswa;

    @OneToOne
    @JoinColumn(name = "id_available_bimbingan", nullable = false, unique = true)
    private AvailableBimbinganModel availableBimbingan;
}
