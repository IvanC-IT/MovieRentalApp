package it.course.exam.myfilmc4IVAN.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.course.exam.myfilmc4IVAN.entity.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, String> {

	

	List<Actor> findAllByLastNameIn(List<String> actorsLastname);

}
