package br.com.jotave_erref.RestWithSpringBoot.Controller;

import br.com.jotave_erref.RestWithSpringBoot.domain.book.CreateBookData;
import br.com.jotave_erref.RestWithSpringBoot.domain.book.DetailBookData;
import br.com.jotave_erref.RestWithSpringBoot.domain.person.DetailPersonData;
import br.com.jotave_erref.RestWithSpringBoot.domain.person.PersonData;
import br.com.jotave_erref.RestWithSpringBoot.domain.person.UpdatePersonData;
import br.com.jotave_erref.RestWithSpringBoot.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
public class BookController {
    @Autowired
    private BookService service;

    @PostMapping
    @Transactional
    @Operation(description = "Adds a New Book", summary = "Adds a new book  by Passing in a Json Representation of the Book",
            tags = {"Books"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = CreateBookData.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<CreateBookData> create(@RequestBody CreateBookData data){
        var book = service.created(data);
        return ResponseEntity.ok().body(book);
    }

    @GetMapping()
    @Operation(description = "Finds all Books", summary = "Finds all Books",
            tags = {"Books"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = { @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DetailBookData.class))
                            )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<List<DetailBookData>> findAll(){
        var books = service.findAllBooks();
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("{id}")
    @Operation(description = "Finds a person", summary = "Finds a person",
            tags = {"Books"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = DetailBookData.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<DetailBookData> search(@PathVariable Long id){
        var book = service.searchBook(id);
        return ResponseEntity.ok().body(book);
    }

    @PutMapping()
    @Operation(description = "Updates a Book", summary = "Updates a Book by Passing in a Json Representation of the Person",
            tags = {"Books"},
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
    public ResponseEntity<DetailBookData> update(@RequestBody DetailBookData data){
        var book = service.updateBook(data);
        return ResponseEntity.ok().body(book);
    }

    @DeleteMapping("{id}")
    @Transactional
    @Operation(description = "Deletes a Book", summary = "Deletes a Book by Passing in a Json Representation of the Person",
            tags = {"Books"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Server Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity delete(@PathVariable Long id){
        service.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
