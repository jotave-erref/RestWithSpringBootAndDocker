package br.com.jotave_erref.RestWithSpringBoot.service;

import br.com.jotave_erref.RestWithSpringBoot.controller.PersonController;
import br.com.jotave_erref.RestWithSpringBoot.domain.person.Person;
import br.com.jotave_erref.RestWithSpringBoot.domain.person.DetailPersonData;
import br.com.jotave_erref.RestWithSpringBoot.domain.person.PersonData;
import br.com.jotave_erref.RestWithSpringBoot.domain.person.UpdatePersonData;
import br.com.jotave_erref.RestWithSpringBoot.infra.exception.RequiredObjectIsNullException;
import br.com.jotave_erref.RestWithSpringBoot.infra.exception.ResourceNotFoundException;
import br.com.jotave_erref.RestWithSpringBoot.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;


    public DetailPersonData created(DetailPersonData data){
        if(data == null) throw new RequiredObjectIsNullException();
        Person person = new Person(data);
        var entity = repository.save(person);
        person.add(linkTo(methodOn(PersonController.class).search(entity.getId())).withSelfRel());
        return new DetailPersonData(entity);
    }

    public DetailPersonData searchPerson(Long id) {
        var person = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        person.add(linkTo(methodOn(PersonController.class).search(person.getId())).withSelfRel());
        return new DetailPersonData(person);
    }

    public Page<PersonData> findAllPerson(Pageable pageable) {
        var personPage = repository.findAll(pageable);
        personPage.map(p -> p.add(linkTo(methodOn(PersonController.class).search(p.getId())).withSelfRel()));
        var personData = personPage.map(PersonData::new);

        return personData;
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

    @Transactional
    public PersonData disablePerson(Long id){
        repository.disablePerson(id);
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        entity.add(linkTo(methodOn(PersonController.class).search(entity.getId())).withSelfRel());
        return new PersonData(entity);
    }

    public void deletePerson(Long id) {
        repository.deleteById(id);
    }
}
