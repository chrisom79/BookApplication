package com.chrisom.service.impl;

import com.chrisom.dto.request.service.CreateAuthorRequest;
import com.chrisom.entity.Author;
import com.chrisom.repository.AuthorRepository;
import com.chrisom.service.AuthorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public String createAuthor(CreateAuthorRequest createAuthorRequest) {
        return saveAuthor(createAuthorRequest);
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
}
