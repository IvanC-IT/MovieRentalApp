package it.course.exam.myfilmc4IVAN.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="film")
@Getter @Setter  @NoArgsConstructor
public class Film {
	
	

	@Id
	@Column(name="film_id",nullable=false, length=10)
	private String filmId;
	
	@Column(name="description", nullable=false,columnDefinition="TEXT")
	private String description;
	
	@Column(name="releaseYear", nullable=false, length=10,columnDefinition="INT(4)")
	private int releaseYear;
	
	@Column(name="title", nullable=false, length=128)
	private String title;
	
	@ManyToOne
	@JoinColumn(name="country_id", nullable=false)
	private Country countryId;
	
	@ManyToOne
	@JoinColumn(name="language_id", nullable=false)
	private Language languageId;
	
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="inventory", 
		joinColumns = {@JoinColumn(name="film_id", referencedColumnName="film_id")},
		inverseJoinColumns = {@JoinColumn(name="store_id", referencedColumnName="store_id")})
	Set<Store> stores;

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="film_actor", 
		joinColumns = {@JoinColumn(name="film_id", referencedColumnName="film_id")},
		inverseJoinColumns = {@JoinColumn(name="actor_id", referencedColumnName="actor_id")})
	Set<Actor> actors;
	
	public Film(String filmId, String description, int releaseYear, String title, Country countryId,
			Language languageId) {
		super();
		this.filmId = filmId;
		this.description = description;
		this.releaseYear = releaseYear;
		this.title = title;
		this.countryId = countryId;
		this.languageId = languageId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filmId == null) ? 0 : filmId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Film other = (Film) obj;
		if (filmId == null) {
			if (other.filmId != null)
				return false;
		} else if (!filmId.equals(other.filmId))
			return false;
		return true;
	}

	

	
	

	
	

	
	
	
	

	
	

}
