package cz.release_calendar.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.array.StringArrayType;

@Entity
@Table(name = "movies")
@TypeDef(name = "string-array", typeClass = StringArrayType.class)
public class Movie {

	@Id
	@GeneratedValue
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
	
	@Type(type = "string-array")
	@Column(name = "genres")
	private String [] genres;
	
	@Column(name = "csfd_link")
	private String csfdLink;
	
	@Column(name = "imdb_link")
	private String imdbLink;
	
	@Column(name = "director", length = 100)
	private String director;
	
	@Type(type = "string-array")
	@Column(name = "actors")
	private String [] actors;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "video_link")
	private String videoLink;
	
// Bezparametrový konstruktor //////////////////////////////////////////////////////////////
	
	public Movie() {
		
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

	public String [] getGenres() {
		return genres;
	}

	public void setGenres(String [] genres) {
		this.genres = genres;
	}

	public String getCsfdLink() {
		return csfdLink;
	}

	public void setCsfdLink(String csfdLink) {
		this.csfdLink = csfdLink;
	}

	public String getImdbLink() {
		return imdbLink;
	}

	public void setImdbLink(String imdbLink) {
		this.imdbLink = imdbLink;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String [] getActors() {
		return actors;
	}

	public void setActors(String [] actors) {
		this.actors = actors;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getVideoLink() {
		return videoLink;
	}

	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}
	
}
