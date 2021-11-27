package cz.release_calendar.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.vladmihalcea.hibernate.type.array.StringArrayType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "movies")
@TypeDef(name = "string-array", typeClass = StringArrayType.class)
@NoArgsConstructor
@Getter
@Setter
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "name_cz", length = 100)
	private String nameCZ;
	
	@Column(name = "name_en", length = 100)
	private String nameEN;
	
	@JsonDeserialize(using = LocalDateDeserializer.class)  
	@JsonSerialize(using = LocalDateSerializer.class) 
	@Column(name = "release_date")
	private LocalDate releaseDate;
	
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
	
	@Transient
	private byte[] image;
	
	@Transient
	private List<byte[]> images;
	
}
