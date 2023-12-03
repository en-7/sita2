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
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "announcement")
public class AnnouncementModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_announcement", updatable = false, nullable = false)
    private Long idAnnouncement;

    @NotNull
    @Size(max = 100)
    @Column(name = "judul", nullable = false)
    private String judul;

    @Lob
    @Column(name = "deskripsi", nullable = false)
    private String deskripsi;

    @Lob
    @Column(name = "file")
    private byte[] file;

    @Column(name = "nama_file")
    private String namaFile;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @ManyToMany
    @JoinTable(name = "announcement_author", joinColumns = @JoinColumn(name = "id_announcement"), inverseJoinColumns = @JoinColumn(name = "id_user"))
    private Set<UserModel> author;
}