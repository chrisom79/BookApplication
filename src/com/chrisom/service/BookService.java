package com.chrisom.service;

import com.chrisom.dto.request.service.CreateBookRequest;
import com.chrisom.dto.request.service.UpdateBookRequest;
import com.chrisom.dto.response.service.BookResponse;

import java.util.List;

public interface BookService {
    String createBook(CreateBookRequest createBookRequest);

    boolean updateBook(UpdateBookRequest updateBookRequest);

    List<BookResponse> getAllBooks();

    BookResponse getBookDetails(String id);

    boolean removeBook(String id);
}
