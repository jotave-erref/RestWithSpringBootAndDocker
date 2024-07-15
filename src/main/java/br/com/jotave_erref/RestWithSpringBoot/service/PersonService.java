package br.com.jotave_erref.RestWithSpringBoot.service;

import br.com.jotave_erref.RestWithSpringBoot.Controller.PersonController;
import br.com.jotave_erref.RestWithSpringBoot.domain.Person;
import br.com.jotave_erref.RestWithSpringBoot.domain.dto.DetailPersonData;
import br.com.jotave_erref.RestWithSpringBoot.domain.dto.PersonData;
import br.com.jotave_erref.RestWithSpringBoot.domain.dto.UpdatePersonData;
import br.com.jotave_erref.RestWithSpringBoot.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;


    public DetailPersonData created(DetailPersonData data){
        Person person = new Person(data);
        var personData = repository.save(person);
        person.add(linkTo(methodOn(PersonController.class).search(personData.getId())).withSelfRel());
        return new DetailPersonData(personData);
    }

    public DetailPersonData searchPerson(Long id) {
        var person = repository.findById(id).get();
        person.add(linkTo(methodOn(PersonController.class).search(person.getId())).withSelfRel());
        return new DetailPersonData(person);
    }

    public List<PersonData> findAllPerson() {
        var persons = repository.findAll();

        persons.stream().forEach(l -> l.add(linkTo(methodOn(PersonController.class).search(l.getId())).withSelfRel()));

        return persons.stream().map(p -> new PersonData(
                p.getId(), p.getFirstName(), p.getLastName(), p.getAddress(), p.getGender(), p.getLinks()
        )).toList();
    }

    public DetailPersonData updatePerson(UpdatePersonData data) {
        var person = repository.getReferenceById(data.id());
        person.update(data);
        var updatePerson = repository.save(person);
        updatePerson.add(linkTo(methodOn(PersonController.class).search(updatePerson.getId())).withSelfRel());
        return new DetailPersonData(updatePerson);
    }

    public void deletePerson(Long id) {
        repository.deleteById(id);
    }
}
