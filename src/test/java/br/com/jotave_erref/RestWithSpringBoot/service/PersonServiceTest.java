package br.com.jotave_erref.RestWithSpringBoot.service;

import br.com.jotave_erref.RestWithSpringBoot.Controller.PersonController;
import br.com.jotave_erref.RestWithSpringBoot.domain.Person;
import br.com.jotave_erref.RestWithSpringBoot.domain.dto.DetailPersonData;
import br.com.jotave_erref.RestWithSpringBoot.domain.dto.UpdatePersonData;
import br.com.jotave_erref.RestWithSpringBoot.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    @InjectMocks
    private PersonService service;
    @Mock
    private PersonRepository repository;
    private Person person;
    private DetailPersonData personData;
    private UpdatePersonData updatePersonData;


    @BeforeEach
    void setUp() {
        this.person = new Person("Jean", "Victor", "Rua xxx", "male");
        person.setId(1L);
    }

    @Test
    void testSearchPerson(){

        when(repository.findById(1L)).thenReturn(Optional.of(person));

        var result = service.searchPerson(1L);

        verify(repository).findById(1L);

        var link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class)
                .search(person.getId())).withSelfRel();
        assertNotNull(result);
        assertNotNull(result.firstName());
        assertNotNull(result.lastName());
        assertNotNull(result.address());
        assertNotNull(result.gender());
        assertNotNull(result.link());
        assertTrue(result.link().contains(link));
        System.out.println(result.toString());
        assertTrue(result.toString().contains("link=</person/1>;rel=\"self\""));
    }

    @Test
    void testCreated(){
        this.personData = new DetailPersonData(person);
        Person entity = new Person(personData);
        entity.setId(1L);

        person.add(Link.of("/person/" + person.getId()));

        when(repository.save(entity)).thenReturn(person);

        var result = service.created(personData);

        verify(repository).save(entity);

        assertNotNull(result);
        assertNotNull(result.link());
        assertEquals("Jean", result.firstName());
        assertEquals("Victor", result.lastName());
        assertEquals("Rua xxx", result.address());

        assertTrue(result.toString().contains("link=</person/1>;rel=\"self\""));

    }
    @Test
    void testUpdatePerson(){
        //prepare UpdatePersonData
        this.updatePersonData = new UpdatePersonData(1L, "Rua yyy", "female");

        //Mock the repository
        when(repository.getReferenceById(updatePersonData.id())).thenReturn(person);
        when(repository.save(person)).thenAnswer(invocation -> invocation.getArgument(0));

        //Call the service method
        var result = service.updatePerson(updatePersonData);

        //Verify the repository methods were called
        verify(repository).getReferenceById(updatePersonData.id());
        verify(repository).save(person);

        person.add(Link.of("/person/" + person.getId()));

        //Verify that the person details were updated
        assertEquals("Rua yyy", person.getAddress());
        assertEquals("female", person.getGender());

        // Verify the returned data
        assertNotNull(result);
        assertNotNull(result.link());
        assertEquals(result.firstName(), person.getFirstName());
        assertEquals(result.lastName(), person.getLastName());
        assertEquals(result.address(), person.getAddress());
        assertEquals(result.gender(), person.getGender());

        assertTrue(result.toString().contains("link=</person/1>;rel=\"self\""));

    }






}