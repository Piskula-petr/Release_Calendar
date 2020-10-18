package cz.release_calendar.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "movies")
public class ListMovie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name_cz", length = 100)
	private String nameCZ;
	
	@Column(name = "name_en", length = 100)
	private String nameEN;
	
	@Column(name = "release_date")
	private Date releaseDate;
	
	@Column(name = "platform", length = 100)
	private String platform;
	
	@Column(name = "director", length = 100)
	private String director;
	
	@Type(type = "string-array")
	@Column(name = "genres")
	private String [] genres;
	
	@Type(type = "string-array")
	@Column(name = "actors")
	private String [] actors;
	
	@Transient
	private byte[] image;
	
// Bezparametrový konstruktor //////////////////////////////////////////////////////////////
	
	public ListMovie() {
		
	}

// Gettery + Settery ///////////////////////////////////////////////////////////////////////
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameCZ() {
		return nameCZ;
	}

	public void setNameCZ(String nameCZ) {
		this.nameCZ = nameCZ;
	}

	public String getNameEN() {
		return nameEN;
	}

	public void setNameEN(String nameEN) {
		this.nameEN = nameEN;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String[] getGenres() {
		return genres;
	}

	public void setGenres(String[] genres) {
		this.genres = genres;
	}

	public String[] getActors() {
		return actors;
	}

	public void setActors(String[] actors) {
		this.actors = actors;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	
}
