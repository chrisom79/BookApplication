package com.chrisom.service;

import com.chrisom.dto.request.service.CreateAuthorRequest;
import com.chrisom.dto.response.service.AuthorResponse;
import rx.Observable;
import rx.Single;

import java.util.List;
import java.util.stream.Collectors;

public interface AuthorService {
    String createAuthor(CreateAuthorRequest createAuthorRequest);

    List<AuthorResponse> getAllAuthors();

    Single<String> createAuthorV2(CreateAuthorRequest createAuthorRequest);

    Single<List<AuthorResponse>> getAllAuthorsV2();

    Observable<List<AuthorResponse>> getAllAuthorsV3();
}
