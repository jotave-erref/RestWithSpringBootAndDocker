package br.com.jotave_erref.RestWithSpringBoot.Controller;

import br.com.jotave_erref.RestWithSpringBoot.domain.dto.DetailPersonData;
import br.com.jotave_erref.RestWithSpringBoot.domain.dto.PersonData;
import br.com.jotave_erref.RestWithSpringBoot.domain.dto.UpdatePersonData;
import br.com.jotave_erref.RestWithSpringBoot.service.PersonService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonService service;


    @PostMapping
    @Transactional
    public ResponseEntity<DetailPersonData> create(@RequestBody DetailPersonData data){
        var person = service.created(data);
        return ResponseEntity.ok().body(person);
    }

    @GetMapping("{id}")
    public ResponseEntity<DetailPersonData> search(@PathVariable Long id){
        var person = service.searchPerson(id);
        return ResponseEntity.ok().body(person);
    }

    @GetMapping()
    public ResponseEntity<List<PersonData>> findAll(){
        var person = service.findAllPerson();
        return ResponseEntity.ok().body(person);
    }

    @PutMapping()
    public ResponseEntity<DetailPersonData> update(@RequestBody UpdatePersonData data){
        var person = service.updatePerson(data);
        return ResponseEntity.ok().body(person);
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id){
        service.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
}
