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
public class MoviePreview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "name_cz", length = 100)
	private String nameCZ;
	
	@Column(name = "release_date")
	private Date releaseDate;
	
	@Column(name = "platform", length = 100)
	private String platform;
	
	@Type(type = "string-array")
	@Column(name = "genres")
	private String [] genres;
	
	@Transient
	private byte[] image;
	
// Bezparametrový konstruktor ////////////////////////////////////////////////////////////
	
	public MoviePreview() {
		
	}

// Gettery + Settery /////////////////////////////////////////////////////////////////////	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNameCZ() {
		return nameCZ;
	}

	public void setNameCZ(String nameCZ) {
		this.nameCZ = nameCZ;
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

	public String[] getGenres() {
		return genres;
	}

	public void setGenres(String[] genres) {
		this.genres = genres;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	
}
