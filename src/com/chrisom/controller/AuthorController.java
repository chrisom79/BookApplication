package com.chrisom.controller;

import com.chrisom.dto.request.service.CreateAuthorRequest;
import com.chrisom.dto.request.service.UpdateAuthorRequest;
import com.chrisom.dto.response.service.AuthorResponse;
import com.chrisom.dto.response.web.BaseWebResponse;
import com.chrisom.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class AuthorController {

    @Autowired
    AuthorService authorService;

    @PostMapping(value = "/v1/authors", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseWebResponse> createAuthor(@RequestBody CreateAuthorRequest createAuthorRequest) {
        String id =   authorService.createAuthor(createAuthorRequest);
        return ResponseEntity.created(URI.create("/api/v1/authors/" + id))
                .body(BaseWebResponse.successWithData(id));
    }

    @GetMapping(value = "/v1/authors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseWebResponse<List<AuthorResponse>>> getAllAuthors() {
        List<AuthorResponse> authors =  authorService.getAllAuthors();
        return ResponseEntity.ok(BaseWebResponse.successWithData(authors));
    }

    @PostMapping(value = "/v2/authors", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Single<ResponseEntity<BaseWebResponse>> createAuthorV2(@RequestBody CreateAuthorRequest createAuthorRequest) {
        return authorService.createAuthorV2(createAuthorRequest)
                .subscribeOn(Schedulers.io())
                .map(id -> ResponseEntity.created(URI.create("/api/v1/authors/" + id))
                        .body(BaseWebResponse.successWithData(id)));
    }

    @GetMapping(value = "/v2/authors", produces = MediaType.APPLICATION_JSON_VALUE)
    public Single<ResponseEntity<BaseWebResponse<List<AuthorResponse>>>> getAllAuthorsV2() {
        return authorService.getAllAuthorsV2()
                .subscribeOn(Schedulers.io())
                .map(authorResponses -> ResponseEntity.ok(BaseWebResponse.successWithData(authorResponses)));

    }

    @GetMapping(value = "/v2/authors/{authorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Single<ResponseEntity<BaseWebResponse<AuthorResponse>>> getBookDetails(@PathVariable(value = "authorId") String bookId) {
        return authorService.getAuthorDetailsV2(bookId).subscribeOn(Schedulers.io()).map(
                authorResponse -> ResponseEntity.ok(BaseWebResponse.successWithData(authorResponse))
        );
    }

    @PutMapping(value = "/v2/authors", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Single<ResponseEntity<BaseWebResponse>> updateAuthor(@RequestBody UpdateAuthorRequest updateAuthorRequest) {
        return authorService.updateAuthorV2(updateAuthorRequest).subscribeOn(Schedulers.io())
                .toSingle(() -> ResponseEntity.ok(BaseWebResponse.successNoData()));
    }

    @GetMapping(value = "/v3/authors", produces = MediaType.APPLICATION_JSON_VALUE)
    public Observable<ResponseEntity<BaseWebResponse<List<AuthorResponse>>>> getAllAuthorsV3() {
        return authorService.getAllAuthorsV3().subscribeOn(Schedulers.io()).map(
                authorResponse -> ResponseEntity.ok(BaseWebResponse.successWithData(authorResponse)));
    }

}
