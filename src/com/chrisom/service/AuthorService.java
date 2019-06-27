package com.chrisom.service;

import com.chrisom.dto.request.service.CreateAuthorRequest;

public interface AuthorService {
    String createAuthor(CreateAuthorRequest createAuthorRequest);
}
