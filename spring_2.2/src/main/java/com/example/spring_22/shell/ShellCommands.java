package com.example.spring_22.shell;


import com.example.spring_22.domain.Author;
import com.example.spring_22.domain.Book;
import com.example.spring_22.domain.Comment;
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
    public void addCommentToBook(@ShellOption String commentText, @ShellOption Long bookId) {
        long savedCommentId = commentService.addCommentToBook(Comment.builder()
                .book(Book.builder()
                        .id(bookId)
                        .build())
                .build());
        System.out.println("Saved comment id : " + savedCommentId);
    }

    @ShellMethod(value = "Get all comments by book id", key = {"c"})
    @CatchAndWrite
    @Transactional
    public void getAllCommentsByBookId(@ShellOption Long bookId) {
        var comments = commentService.getAllCommentsOfBook(bookId);
        System.out.println("Comments are : " + comments.stream()
                .map(Comment::toString)
                .collect(Collectors.joining("\n")));
    }

    @ShellMethod(value = "Delete comment by id", key = {"dc"})
    @CatchAndWrite
    @Transactional
    public void deleteCommentById(@ShellOption Long commentId) {
        commentService.deleteCommentById(commentId);
        System.out.println(MessageFormat.format("Comment by id {0} is deleted", commentId));
    }

    @ShellMethod(value = "Update comment text by id", key = {"uc"})
    @CatchAndWrite
    @Transactional
    public void updateCommentTextById(@ShellOption Long commentId, @ShellOption(defaultValue = "__NULL__") String newText) {
        commentService.updateCommentTextById(commentId, newText);
        System.out.println(MessageFormat.format("Comment by id {0} is updated", commentId));
    }
}
