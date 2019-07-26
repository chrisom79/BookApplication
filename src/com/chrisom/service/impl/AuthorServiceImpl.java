package com.chrisom.service.impl;

import com.chrisom.dto.request.service.CreateAuthorRequest;
import com.chrisom.dto.request.service.UpdateAuthorRequest;
import com.chrisom.dto.request.service.UpdateBookRequest;
import com.chrisom.dto.response.service.AuthorResponse;
import com.chrisom.dto.response.service.BookResponse;
import com.chrisom.entity.Author;
import com.chrisom.entity.Book;
import com.chrisom.repository.AuthorRepository;
import com.chrisom.service.AuthorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Completable;
import rx.Observable;
import rx.Single;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    @Override
    public Single<AuthorResponse> getAuthorDetailsV2(String id) {
        return findAuthorDetailInRepository(id);
    }

    @Override
    public Completable updateAuthorV2(UpdateAuthorRequest updateAuthorRequest) {
        return updateAuthorInRepository(updateAuthorRequest);
    }

    @Override
    public Completable deleteAuthorV2(String id) {
        return null;
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

    private Single<AuthorResponse> findAuthorDetailInRepository(String id) {
        return Single.create(singleSubscriber -> {
            Optional<Author> optionalAuthor = authorRepository.findById(id);
            if(!optionalAuthor.isPresent()) {
                singleSubscriber.onError(new EntityNotFoundException());
            } else {
                AuthorResponse authorResponse = convertToAuthorResponse(optionalAuthor.get());
                singleSubscriber.onSuccess(authorResponse);
            }
        });
    }

    private Completable updateAuthorInRepository(UpdateAuthorRequest updateAuthorRequest) {
        return Completable.create(completableSubscriber -> {
            Optional<Author> optionalAuthor = authorRepository.findById(updateAuthorRequest.getId());
            if(!optionalAuthor.isPresent()) {
                completableSubscriber.onError(new EntityNotFoundException());
            } else {
                Author author = new Author();
                author.setId(updateAuthorRequest.getId());
                author.setName(updateAuthorRequest.getName());
                authorRepository.save(author);
                completableSubscriber.onCompleted();
            }
        });

    }

    private AuthorResponse convertToAuthorResponse(Author author) {
        AuthorResponse authorResponse = new AuthorResponse();
        BeanUtils.copyProperties(author, authorResponse);
        authorResponse.setName(author.getName());
        return authorResponse;
    }

}
