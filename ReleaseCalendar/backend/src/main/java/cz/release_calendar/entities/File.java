package cz.release_calendar.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cz.release_calendar.enums.FileCategory;

@Entity
@Table(name = "files")
public class File {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "movie_id")
	private Long movie_id;
	
	@Column(name = "data")
	private byte[] data;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "category", length = 100)
	private FileCategory category;

// Bezparametrový konstruktor //////////////////////////////////////////////////////////////
	
	public File() {

	}

// Gettery + Settery ///////////////////////////////////////////////////////////////////////
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMovie_id() {
		return movie_id;
	}

	public void setMovie_id(Long movie_id) {
		this.movie_id = movie_id;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public FileCategory getCategory() {
		return category;
	}

	public void setCategory(FileCategory category) {
		this.category = category;
	}
	
}
