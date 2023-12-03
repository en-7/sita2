package protensi.sita.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "timeline")
public class TimelineModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_timeline", updatable = false, nullable = false)
    private Long idTimeline;

    @NotNull
    @Column(name = "periode", nullable = false)
    private String periode;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "reg_sidang")
    private LocalDate regSidang;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "mulai_sidang")
    private LocalDate startSidang;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "akhir_sidang")
    private LocalDate endSidang;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "pengumuman_sidang")
    private LocalDateTime pengumumanSidang;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "reg_ugb")
    private LocalDate regUGB;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "pengumuman_ugb")
    private LocalDateTime pengumumanUGB;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "reg_sempro")
    private LocalDate regSempro;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "mulai_sempro")
    private LocalDate startSempro;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "akhir_sempro")
    private LocalDate endSempro;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "pengumuman_sempro")
    private LocalDateTime pengumumanSempro;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "reg_semhas")
    private LocalDate regSemhas;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "mulai_semhas")
    private LocalDate startSemhas;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "akhir_semhas")
    private LocalDate endSemhas;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "pengumuman_semhas")
    private LocalDateTime pengumumanSemhas;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "reg_validasi_TA")
    private LocalDate regValidasiTA;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "pengumpulan_TA")
    private LocalDate pengumpulanTA;

}