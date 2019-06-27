package com.chrisom.controller;

import com.chrisom.dto.response.web.BaseWebResponse;
import com.chrisom.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chrisom.dto.request.service.CreateAuthorRequest;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/authors")
public class AuthorController {

    @Autowired
    AuthorService authorService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseWebResponse> createAuthor(@RequestBody CreateAuthorRequest createAuthorRequest) {
        String id =   authorService.createAuthor(createAuthorRequest);
        return ResponseEntity.created(URI.create("/api/authors/" + id))
                .body(BaseWebResponse.successWithData(id));
    }
}
