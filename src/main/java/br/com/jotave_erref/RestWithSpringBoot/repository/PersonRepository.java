package br.com.jotave_erref.RestWithSpringBoot.repository;

import br.com.jotave_erref.RestWithSpringBoot.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
