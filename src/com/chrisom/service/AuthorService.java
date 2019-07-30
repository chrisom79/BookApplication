package com.chrisom.service;

import com.chrisom.dto.request.service.CreateAuthorRequest;
import com.chrisom.dto.request.service.UpdateAuthorRequest;
import com.chrisom.dto.response.service.AuthorResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rx.Completable;
import rx.Observable;
import rx.Single;

import java.util.List;

public interface AuthorService {
    String createAuthor(CreateAuthorRequest createAuthorRequest);

    Single<String> createAuthorV2(CreateAuthorRequest createAuthorRequest);

    List<AuthorResponse> getAllAuthors();

    Single<List<AuthorResponse>> getAllAuthorsV2();

    Observable<List<AuthorResponse>> getAllAuthorsV3();

    Single<AuthorResponse> getAuthorDetailsV2(String id);

    Completable updateAuthorV2(UpdateAuthorRequest updateAuthorRequest);

    Completable deleteAuthorV2(String id);

    Flux<AuthorResponse> getAllAuthorsFlux();
}
