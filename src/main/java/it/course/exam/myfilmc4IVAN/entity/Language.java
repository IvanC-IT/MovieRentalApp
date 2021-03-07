package it.course.exam.myfilmc4IVAN.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "language")
@Getter
@Setter
@NoArgsConstructor
public class Language {

	@Id
	@Column(name = "language_id", nullable = false, length = 2)
	private String languageId;

	@Column(name = "language_name", nullable = false, unique = true, length = 40) 
	private String languageName;

	public Language(String languageId, String languageName) {
		super();
		this.languageId = languageId;
		this.languageName = languageName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((languageId == null) ? 0 : languageId.hashCode());
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
		Language other = (Language) obj;
		if (languageId == null) {
			if (other.languageId != null)
				return false;
		} else if (!languageId.equals(other.languageId))
			return false;
		return true;
	}

}
