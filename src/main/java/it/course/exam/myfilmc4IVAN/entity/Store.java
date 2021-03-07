package it.course.exam.myfilmc4IVAN.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "store")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Store {

	@Id
	@Column(name = "store_id", nullable = false, length = 10)
	private String storeId;

	@Column(name = "store_name", nullable = false, unique = true, length = 50)
	private String storeName;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((storeId == null) ? 0 : storeId.hashCode());
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
		Store other = (Store) obj;
		if (storeId == null) {
			if (other.storeId != null)
				return false;
		} else if (!storeId.equals(other.storeId))
			return false;
		return true;
	}

}
