package com.example.spring_22.shell;


import com.example.spring_22.domain.Author;
import com.example.spring_22.domain.Book;
import com.example.spring_22.domain.Genre;
import com.example.spring_22.services.book.BookService;
import com.example.spring_22.services.comment.CommentService;
import com.example.spring_22.shell.aspect.CatchAndWrite;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@ShellComponent
public class ShellCommands {

    private final BookService bookService;

    private final CommentService commentService;

    @ShellMethod(value = "Add book", key = {"a", "add"})
    @CatchAndWrite
    @Transactional
    public void addBook(@ShellOption String bookName, @ShellOption(defaultValue = "__NULL__") String authorFullName,
                        @ShellOption(defaultValue = "__NULL__") String... genreNames) {
        List<Genre> genres = new ArrayList<>();
        Book bookToBeSaved = Book.builder()
                .name(bookName)
                .build();
        if (nonNull(genreNames)) {
            for (String genreName : genreNames) {
                genres.add(Genre.builder()
                        .genreName(genreName)
                        .build());
            }
            bookToBeSaved.setGenres(genres);
        }
        if (nonNull(authorFullName)) {
            bookToBeSaved.setAuthor(Author.builder()
                    .fullName(authorFullName)
                    .build());
        }
        long savedBookId = bookService.saveBook(bookToBeSaved);
        System.out.println("Saved book id : " + savedBookId);
    }

    @ShellMethod(value = "Get book by id", key = {"g", "get"})
    @CatchAndWrite
    public void getBookById(@ShellOption long id) {
        Optional<Book> foundBook = bookService.getBookById(id);
        if (foundBook.isPresent()) {
            System.out.println(MessageFormat.format("Book by id {0}: {1}", id, foundBook));
        } else {
            System.out.println(MessageFormat.format("Book by id {0} not found", id));
        }
    }

    @ShellMethod(value = "Get all books", key = {"all"})
    @CatchAndWrite
    public void getAllBooks() {
        List<Book> foundBooks = bookService.getAllBooks();
        System.out.println("Found books by are :\n" + foundBooks.stream()
                .map(Book::toString)
                .collect(Collectors.joining("\n")));
    }

    @ShellMethod(value = "Update book by id", key = {"u", "update"})
    @CatchAndWrite
    @Transactional
    public void updateBook(@ShellOption long bookId, @ShellOption String bookName,
                           @ShellOption(defaultValue = "__NULL__") String authorFullName,
                           @ShellOption(defaultValue = "__NULL__") String... genreNames) {
        List<Genre> genres = new ArrayList<>();
        Book bookToBeUpdated = Book.builder()
                .name(bookName)
                .build();
        if (nonNull(genreNames)) {
            for (String genreName : genreNames) {
                genres.add(Genre.builder()
                        .genreName(genreName)
                        .build());
            }
            bookToBeUpdated.setGenres(genres);
        }
        if (nonNull(authorFullName)) {
            bookToBeUpdated.setAuthor(Author.builder()
                    .fullName(authorFullName)
                    .build());
        }
        bookService.updateBookById(bookToBeUpdated);
        System.out.println(MessageFormat.format("Book by id {0} is updated", bookId));
    }

    @ShellMethod(value = "Delete book by id", key = {"d", "delete"})
    @CatchAndWrite
    @Transactional
    public void deleteBookById(@ShellOption long id) {
        bookService.deleteBookById(id);
        System.out.println(MessageFormat.format("Book by id {0} is deleted", id));
    }

    @ShellMethod(value = "Add comment", key = {"ac"})
    @CatchAndWrite
    @Transactional
    public void addCommentToBook(@ShellOption String commentText, @ShellOption(defaultValue = "__NULL__") Long bookId,) {
        List<Genre> genres = new ArrayList<>();
        Book bookToBeSaved = Book.builder()
                .name(bookName)
                .build();
        if (nonNull(genreNames)) {
            for (String genreName : genreNames) {
                genres.add(Genre.builder()
                        .genreName(genreName)
                        .build());
            }
            bookToBeSaved.setGenres(genres);
        }
        if (nonNull(authorFullName)) {
            bookToBeSaved.setAuthor(Author.builder()
                    .fullName(authorFullName)
                    .build());
        }
        long savedBookId = bookService.saveBook(bookToBeSaved);
        System.out.println("Saved book id : " + savedBookId);
    }
}
