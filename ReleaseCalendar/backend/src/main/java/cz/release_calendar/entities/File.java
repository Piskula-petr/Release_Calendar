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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "files")
@NoArgsConstructor
@Getter
@Setter
public class File {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "movie_id")
	private long movie_id;
	
	@Column(name = "data")
	private byte[] data;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "category", length = 100)
	private FileCategory category;

}
