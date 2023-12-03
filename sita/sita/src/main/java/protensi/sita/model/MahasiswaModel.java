package protensi.sita.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "id_user")
@Table(name = "Mahasiswa")
public class MahasiswaModel extends UserModel {
    @NotNull
    @Column(name = "nim", nullable = false)
    private Integer nim;

    @Column(name = "tahap")
    private String tahap;

    // public MahasiswaModel(String nama, Set<EnumRole> roles, String username,
    // String password, String email) {
    // super(nama, roles, username, password, email);
    // }
}