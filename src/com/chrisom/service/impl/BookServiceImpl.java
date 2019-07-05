package com.chrisom.service.impl;

import com.chrisom.dto.request.service.CreateBookRequest;
import com.chrisom.dto.request.service.UpdateBookRequest;
import com.chrisom.dto.response.service.BookResponse;
import com.chrisom.entity.Author;
import com.chrisom.entity.Book;
import com.chrisom.repository.AuthorRepository;
import com.chrisom.repository.BookRepository;
import com.chrisom.service.BookService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public String createBook(CreateBookRequest createBookRequest) {
        return saveBook(createBookRequest);
    }

    @Override
    public boolean updateBook(UpdateBookRequest updateBookRequest) {
        return updateBookRepository(updateBookRequest);
    }

    @Override
    public List<BookResponse> getAllBooks() {
        return findAllBooks().stream().map(this::converToBookResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponse getBookDetails(String id) {
        return converToBookResponse(findBookById(id));
    }

    @Override
    public boolean removeBook(String id) {
        return removeBookInRepository(id);
    }

    private String saveBook(CreateBookRequest createBookRequest) {
        Optional<Author> optionalAuthor = authorRepository.findById(createBookRequest.getAuthorId());

        return bookRepository.save(convertToBook(createBookRequest)).getId();
    }

    private boolean updateBookRepository(UpdateBookRequest updateBookRequest) {
        Optional<Book> optionalBook = bookRepository.findById(updateBookRequest.getId());
        if(!optionalBook.isPresent()) {
            return false;
        } else {
            Book book = optionalBook.get();
            book.setTitle(updateBookRequest.getTitle());
            bookRepository.save(book);
            return true;
        }
    }

    private List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    private Book findBookById(String id) {
        return bookRepository.findById(id).get();
    }

    private boolean removeBookInRepository(String id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if(!optionalBook.isPresent()) {
            return false;
        } else {
            bookRepository.delete(optionalBook.get());
            return true;
        }
    }

    private Book convertToBook(CreateBookRequest createBookRequest) {
        Book book = new Book();

        BeanUtils.copyProperties(createBookRequest, book);
        book.setId(UUID.randomUUID().toString());
        book.setAuthor(Author.builder().id(createBookRequest.getAuthorId()).build());

        return book;
    }

    private BookResponse converToBookResponse(Book book) {
        BookResponse bookResponse = new BookResponse();
        BeanUtils.copyProperties(book, bookResponse);
        bookResponse.setAuthorName(book.getAuthor().getName());
        return bookResponse;
    }
}
