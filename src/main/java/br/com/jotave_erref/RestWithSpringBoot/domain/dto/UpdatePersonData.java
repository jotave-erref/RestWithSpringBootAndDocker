package br.com.jotave_erref.RestWithSpringBoot.domain.dto;

import br.com.jotave_erref.RestWithSpringBoot.domain.Person;

public record UpdatePersonData(Long id, String address, String gender) {
    public UpdatePersonData(Person person) {
        this(person.getId(), person.getAddress(), person.getGender());
    }
}
