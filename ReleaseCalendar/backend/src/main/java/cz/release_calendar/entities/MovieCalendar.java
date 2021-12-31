package cz.release_calendar.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "movies")
@NoArgsConstructor
@Getter
@Setter
public class MovieCalendar {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@Column(name = "name_cz", length = 100)
	private String nameCZ;
	
	@Column(name = "name_en", length = 100)
	private String nameEN;
	
	@Column(name = "release_date")
	private LocalDate releaseDate;
	
	@Transient
	private byte[] image;

}
