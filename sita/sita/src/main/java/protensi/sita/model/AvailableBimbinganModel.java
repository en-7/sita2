package protensi.sita.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "available_bimbingan")
public class AvailableBimbinganModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_available_bimbingan", updatable = false, nullable = false)
    private Long idAvailableBimbingan;

    @Size(max = 100)
    @Column(name = "booking_status")
    private String bookingStatus;

    @Column(name = "start_bimbingan_time", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startBimbinganTime;

    @Column(name = "end_bimbingan_time", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endBimbinganTime;

    @ManyToOne
    @JoinColumn(name = "id_pembimbing", nullable = false)
    private PembimbingModel pembimbing;

    @ManyToOne
    @JoinColumn(name = "id_mahasiswa")
    private MahasiswaModel mahasiswa;
}