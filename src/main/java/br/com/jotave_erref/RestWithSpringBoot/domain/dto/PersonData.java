package br.com.jotave_erref.RestWithSpringBoot.domain.dto;

import br.com.jotave_erref.RestWithSpringBoot.domain.Person;
import org.springframework.hateoas.Links;

public record PersonData(Long id, String firstName, String lastName, String address, String gender, Links link) {

    PersonData(Person person){
        this(person.getId(), person.getFirstName(), person.getLastName(), person.getAddress(), person.getGender(), person.getLinks());
    }
}
