package com.chrisom.controller;

import com.chrisom.dto.request.service.CreateBookRequest;
import com.chrisom.dto.request.service.UpdateBookRequest;
import com.chrisom.dto.response.service.BookResponse;
import com.chrisom.dto.response.web.BaseWebResponse;
import com.chrisom.dto.response.web.UpdateBookWebRequest;
import com.chrisom.exception.ErrorCode;
import com.chrisom.service.BookService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseWebResponse> createBook(@RequestBody CreateBookRequest createBookRequest) {
        String id = bookService.createBook(createBookRequest);

        return ResponseEntity.created(URI.create("/api/v1/books/" + id))
                .body(BaseWebResponse.successWithData(id));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseWebResponse<List<BookResponse>>> getAllBooks() {
        List<BookResponse> bookResponses = bookService.getAllBooks();
        return ResponseEntity.ok(BaseWebResponse.successWithData(bookResponses));
    }

    @PutMapping(value = "/{bookId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseWebResponse> updateBook(@PathVariable(value = "bookId") String bookId, @RequestBody UpdateBookWebRequest updateBookRequest) {
        if(bookService.updateBook(convertToUpdateBookRequest(bookId, updateBookRequest))) {
            return ResponseEntity.ok(BaseWebResponse.successNoData());
        } else {
            return ResponseEntity.created(URI.create("/api/v1/books/" + bookId))
                    .body(BaseWebResponse.error(ErrorCode.ENTITY_NOT_FOUND));
        }
    }

    @GetMapping(value = "/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseWebResponse<BookResponse>> getBookById(@PathVariable(value = "bookId") String bookId) {
        return ResponseEntity.ok(BaseWebResponse.successWithData(bookService.getBookDetails(bookId)));
    }

    @DeleteMapping(value = "/{bookId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseWebResponse> deleteBook(@PathVariable(value = "bookId") String bookId) {
        if(bookService.removeBook(bookId)) {
            return ResponseEntity.ok(BaseWebResponse.successNoData());
        } else {
            return ResponseEntity.created(URI.create("/api/v1/books/" + bookId))
                    .body(BaseWebResponse.error(ErrorCode.SERVER_ERROR));
        }
    }

    private UpdateBookRequest convertToUpdateBookRequest(String bookId, UpdateBookWebRequest updateBookWebRequest) {
        UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        BeanUtils.copyProperties(updateBookWebRequest, updateBookRequest);
        updateBookRequest.setId(bookId);
        return updateBookRequest;
    }

}
