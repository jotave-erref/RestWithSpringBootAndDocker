package br.com.jotave_erref.RestWithSpringBoot.service;

import br.com.jotave_erref.RestWithSpringBoot.controller.PersonController;
import br.com.jotave_erref.RestWithSpringBoot.domain.person.Person;
import br.com.jotave_erref.RestWithSpringBoot.domain.person.DetailPersonData;
import br.com.jotave_erref.RestWithSpringBoot.domain.person.PersonData;
import br.com.jotave_erref.RestWithSpringBoot.domain.person.UpdatePersonData;
import br.com.jotave_erref.RestWithSpringBoot.infra.exception.RequiredObjectIsNullException;
import br.com.jotave_erref.RestWithSpringBoot.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    @InjectMocks
    private PersonService service;
    @Mock
    private PersonRepository repository;
//    @Mock
    private Person person;
//    @Mock
    private DetailPersonData personData;
    private UpdatePersonData updatePersonData;


    @BeforeEach
    void setUp() {
        this.person = new Person("Jean", "Victor", "Rua xxx", "male", true);
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
        assertTrue(result.toString().contains("link=</api/person/1>;rel=\"self\""));
    }

    @Test
    void testCreated(){
        this.personData = new DetailPersonData(person);

        person.add(Link.of("/api/person/" + person.getId()));

        when(repository.save(any(Person.class))).thenReturn(person);

        var result = service.created(personData);

        verify(repository).save(any(Person.class));

        assertNotNull(result);
        assertNotNull(result.link());
        assertEquals("Jean", result.firstName());
        assertEquals("Victor", result.lastName());
        assertEquals("Rua xxx", result.address());

        assertTrue(result.toString().contains("link=</api/person/1>;rel=\"self\""));

    }

    @Test
    @DisplayName("Should throw RequiredObjectIsNullException message if the object is null")
    void testCreatedIsNull(){
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () ->{
            service.created(null);
        });

        String expected = "It's not allowed to persist a null object";
        String actual = exception.getMessage();

        assertTrue(actual.contains(expected));
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

        person.add(Link.of("api/person/" + person.getId()));

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

        assertTrue(result.toString().contains("link=</api/person/1>;rel=\"self\""));

    }

    @Test
    @DisplayName("Should throw RequiredObjectIsNullException message if the object is null")
    void testUpdateObjectIsNull(){
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () ->{
            service.updatePerson(null);
        });

        String expected = "It's not allowed to persist a null object";
        String actual = exception.getMessage();

        assertTrue(actual.contains(expected));
    }
    /*
    @Test
    void testFindAllPerson(){

        List<Person> persons = new ArrayList<>();
        persons.add(new Person("person1", "person1", "Rua xxx", "male", true));
        persons.add(new Person("person2", "person2", "Rua yyy", "female", true));
        persons.get(0).setId(1);
        persons.get(1).setId(2);

        persons.stream().map(p -> new PersonData(p.getId(), p.getFirstName(), p.getLastName(), p.getAddress(), p.getGender(), p.getEnabled(), p.getLinks())).toList();

        when(repository.findAll()).thenReturn(persons);

        var result = service.findAllPerson(Pageable page);

        assertNotNull(result);

        var person1 = result.get(0);

        assertNotNull(person1);
        assertNotNull(person1.id());
        assertNotNull(person1.link());

        assertTrue(person1.toString().contains("link=</api/person/1>;rel=\"self\""));
        assertEquals("person1", person1.firstName());
        assertEquals("person1", person1.lastName());
        assertEquals("Rua xxx", person1.address());
        assertEquals("male", person1.gender());

        var person2 = result.get(1);


        assertNotNull(person2);
        assertNotNull(person2.id());
        assertNotNull(person2.link());

        assertTrue(person2.toString().contains("link=</api/person/2>;rel=\"self\""));
        assertEquals("person2", person2.firstName());
        assertEquals("person2", person2.lastName());
        assertEquals("Rua yyy", person2.address());
        assertEquals("female", person2.gender());

    }

     */

}