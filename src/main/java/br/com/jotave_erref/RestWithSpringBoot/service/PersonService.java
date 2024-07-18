package br.com.jotave_erref.RestWithSpringBoot.service;

import br.com.jotave_erref.RestWithSpringBoot.Controller.PersonController;
import br.com.jotave_erref.RestWithSpringBoot.domain.Person;
import br.com.jotave_erref.RestWithSpringBoot.domain.dto.DetailPersonData;
import br.com.jotave_erref.RestWithSpringBoot.domain.dto.PersonData;
import br.com.jotave_erref.RestWithSpringBoot.domain.dto.UpdatePersonData;
import br.com.jotave_erref.RestWithSpringBoot.infra.exception.RequiredObjectIsNullException;
import br.com.jotave_erref.RestWithSpringBoot.infra.exception.ResourceNotFoundException;
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
        if(data == null) throw new RequiredObjectIsNullException();
        Person person = new Person(data);
        var enity = repository.save(person);
        person.add(linkTo(methodOn(PersonController.class).search(enity.getId())).withSelfRel());
        return new DetailPersonData(enity);
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
        if(data == null) throw new RequiredObjectIsNullException();
        var person = repository.getReferenceById(data.id());
        if(person == null) throw new ResourceNotFoundException("No records found for this ID");
        person.update(data);
        var updatePerson = repository.save(person);
        updatePerson.add(linkTo(methodOn(PersonController.class).search(updatePerson.getId())).withSelfRel());
        return new DetailPersonData(updatePerson);
    }

    public void deletePerson(Long id) {
        repository.deleteById(id);
    }
}
