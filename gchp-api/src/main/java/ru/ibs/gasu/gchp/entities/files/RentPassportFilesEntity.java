package ru.ibs.gasu.gchp.entities.files;

import org.hibernate.envers.Audited;
import ru.ibs.gasu.gchp.entities.files.ProjectFile;

import javax.persistence.Entity;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "rent_passport_files")
public class RentPassportFilesEntity extends ProjectFile {
}
