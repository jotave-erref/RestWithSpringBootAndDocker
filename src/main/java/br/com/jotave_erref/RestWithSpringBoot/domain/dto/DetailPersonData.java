package br.com.jotave_erref.RestWithSpringBoot.domain.dto;

import br.com.jotave_erref.RestWithSpringBoot.domain.Person;
import org.springframework.hateoas.Links;

public record DetailPersonData(String firstName, String lastName, String address, String gender, Links link) {
    public DetailPersonData(Person personData) {
        this(personData.getFirstName(), personData.getLastName(), personData.getAddress(), personData.getGender(), personData.getLinks());
    }
}
