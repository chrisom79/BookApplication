package com.chrisom.service.impl;

import com.chrisom.dto.request.service.CreateAuthorRequest;
import com.chrisom.dto.response.service.AuthorResponse;
import com.chrisom.entity.Author;
import com.chrisom.repository.AuthorRepository;
import com.chrisom.service.AuthorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.Single;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public String createAuthor(CreateAuthorRequest createAuthorRequest) {
        return saveAuthor(createAuthorRequest);
    }

    @Override
    public List<AuthorResponse> getAllAuthors() {

        return findAllAuthors().stream().map(
                author -> new AuthorResponse(author.getId(), author.getName())).collect(Collectors.toList());
    }

    @Override
    public Single<String> createAuthorV2(CreateAuthorRequest createAuthorRequest) {

        return Single.create(subscriber -> {
            String authorId = authorRepository.save(convertToAuthor(createAuthorRequest)).getId();
            subscriber.onSuccess(authorId);
        });
    }

    @Override
    public Single<List<AuthorResponse>> getAllAuthorsV2() {
        return Single.create(singleSubscriber -> {

            List<AuthorResponse> authorResponses = findAllAuthors().stream().map(
                    author -> new AuthorResponse(author.getId(), author.getName())).collect(Collectors.toList());

            singleSubscriber.onSuccess(authorResponses);
        });

    }

    public Observable<List<AuthorResponse>> getAllAuthorsV3() {
        return Observable.create(observer -> {

            List<AuthorResponse> authorResponses = findAllAuthors().stream().map(
                    author -> new AuthorResponse(author.getId(), author.getName())).collect(Collectors.toList());

            observer.onNext(authorResponses);
            observer.onCompleted();
        });
    }

    private String saveAuthor(CreateAuthorRequest createAuthorRequest) {
        return authorRepository.save(convertToAuthor(createAuthorRequest)).getId();
    }

    private Author convertToAuthor(CreateAuthorRequest createAuthorRequest) {
        Author author = new Author();
        BeanUtils.copyProperties(createAuthorRequest, author);
        author.setId(UUID.randomUUID().toString());

        return author;
    }

    private List<Author> findAllAuthors() {
        return authorRepository.findAll();
    }
}
