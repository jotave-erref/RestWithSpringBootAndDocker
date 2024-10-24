package br.com.jotave_erref.RestWithSpringBoot.controller;

import br.com.jotave_erref.RestWithSpringBoot.domain.person.DetailPersonData;
import br.com.jotave_erref.RestWithSpringBoot.domain.person.PersonData;
import br.com.jotave_erref.RestWithSpringBoot.domain.person.UpdatePersonData;
import br.com.jotave_erref.RestWithSpringBoot.service.PersonService;
import br.com.jotave_erref.RestWithSpringBoot.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/person")
@Tag(name = "People", description = "Endpoints to Management People")
public class PersonController {
    @Autowired
    private PersonService service;


    @PostMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Transactional
    @Operation(description = "Adds a New Person", summary = "Adds a new person  by Passing in a Json Representation of the Person",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = DetailPersonData.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<DetailPersonData> create(@RequestBody DetailPersonData data){
        var person = service.created(data);
        return ResponseEntity.ok().body(person);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(description = "Finds all people", summary = "Finds all people",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = { @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PersonData.class))
                    )
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Server Error", responseCode = "500", content = @Content)
        })
    public ResponseEntity<List<PersonData>> findAll(){
        var person = service.findAllPerson();
        return ResponseEntity.ok().body(person);
    }

    @GetMapping(value = "{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(description = "Finds a person", summary = "Finds a person",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = DetailPersonData.class))
                            ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<DetailPersonData> search(@PathVariable Long id){
        var person = service.searchPerson(id);
        return ResponseEntity.ok().body(person);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
    consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(description = "Updates a Person", summary = "Updates a Person by Passing in a Json Representation of the Person",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = DetailPersonData.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<DetailPersonData> update(@RequestBody UpdatePersonData data){
        var person = service.updatePerson(data);
        return ResponseEntity.ok().body(person);
    }

    @DeleteMapping(value = "{id}")
    @Transactional
    @Operation(description = "Deletes a Person", summary = "Deletes a Person by Passing in a Json Representation of the Person",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity delete(@PathVariable Long id){
        service.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(description = "Disabling a especify person by our id", summary = "Disabling a especify person by our id",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = DetailPersonData.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<PersonData> disabling(@PathVariable(value = "id") Long id){
        var person = service.disablePerson(id);
        return ResponseEntity.ok().body(person);
    }
}
