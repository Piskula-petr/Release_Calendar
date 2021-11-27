package cz.release_calendar.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "movies")
@NoArgsConstructor
@Getter
@Setter
public class MovieList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "name_cz", length = 100)
	private String nameCZ;
	
	@Column(name = "name_EN", length = 100)
	private String nameEN;
	
	@Column(name = "release_date")
	private LocalDate releaseDate;
	
	@Column(name = "platform", length = 100)
	private String platform;
	
	@Type(type = "string-array")
	@Column(name = "genres")
	private String [] genres;
	
	@Transient
	private byte[] image;

}
