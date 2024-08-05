package br.com.jotave_erref.RestWithSpringBoot.domain.person;

import br.com.jotave_erref.RestWithSpringBoot.domain.person.Person;

public record UpdatePersonData(Long id, String address, String gender) {
    public UpdatePersonData(Person person) {
        this(person.getId(), person.getAddress(), person.getGender());
    }
}
