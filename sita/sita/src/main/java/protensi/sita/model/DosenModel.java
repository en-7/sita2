package protensi.sita.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "id_user")
@Table(name = "Dosen")
public class DosenModel extends UserModel {
}