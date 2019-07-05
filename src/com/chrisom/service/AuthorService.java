package com.chrisom.service;

import com.chrisom.dto.request.service.CreateAuthorRequest;
import com.chrisom.dto.response.service.AuthorResponse;
import com.chrisom.dto.response.service.BookResponse;
import com.chrisom.entity.Author;

import java.util.List;

public interface AuthorService {
    String createAuthor(CreateAuthorRequest createAuthorRequest);

    List<AuthorResponse> getAllAuthors();
}
