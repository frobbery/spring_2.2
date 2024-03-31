package com.example.spring_22.services.book;

import com.example.spring_22.dao.book.BookRepository;
import com.example.spring_22.domain.Author;
import com.example.spring_22.domain.Book;
import com.example.spring_22.domain.Genre;
import com.example.spring_22.services.author.AuthorService;
import com.example.spring_22.services.genre.GenreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Сервис для работы с книгами должен:")
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    @Mock
    private GenreService genreService;

    @InjectMocks
    private BookServiceImpl sut;

    @Test
    @DisplayName("Должен сохранять книгу")
    void shouldSaveBook() {
        //given
        var bookToBeSaved = Book.builder()
                .name("name")
                .build();
        var author = Author.builder()
                .fullName("fullName");
        var genre = Genre.builder()
                .genreName("genreName");
        var bookFromDao = Book.builder()
                .id(1L)
                .name("name")
                .build();
        var authorFromService = author
                .id(2L)
                .build();
        var genreFromService = genre
                .id(3L)
                .build();
        var book = Book.builder()
                .name("name")
                .author(author.build())
                .genres(List.of(genre.build()))
                .build();
        when(bookRepository.save(bookToBeSaved)).thenReturn(bookFromDao);
        when(authorService.saveAuthorIfNotExists(author.build())).thenReturn(authorFromService);
        when(genreService.saveGenreIfNotExists(genre.build())).thenReturn(genreFromService);

        //when
        var result = sut.saveBook(book);

        //then
        assertEquals(bookFromDao.getId(), result);
        verify(bookRepository, times(1)).updateAuthor(bookFromDao.getId(), authorFromService);
        verify(bookRepository, times(1)).addGenreToBook(bookFromDao.getId(), genreFromService);
    }

    @Test
    @DisplayName("Должен получать книгу по id")
    void shouldGetBookById() {
        //given
        var author = Author.builder()
                .id(2L)
                .fullName("fullName")
                .build();
        var genre = Genre.builder()
                .id(3L)
                .genreName("genreName")
                .build();
        var expectedBook = Book.builder()
                .id(1L)
                .name("name")
                .author(author)
                .genres(List.of(genre))
                .build();
        when(bookRepository.findById(expectedBook.getId())).thenReturn(Optional.of(expectedBook));

        //when
        var actualBook = sut.getBookById(expectedBook.getId());

        //then
        assertThat(actualBook)
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(expectedBook));
    }

    @Test
    @DisplayName("Должен получать все книги")
    void shouldGetAllBooks() {
        //given
        var author = Author.builder()
                .id(2L)
                .fullName("fullName")
                .build();
        var genre = Genre.builder()
                .id(3L)
                .genreName("genreName")
                .build();
        var expectedBooks = List.of(Book.builder()
                .id(1L)
                .name("name")
                .author(author)
                .genres(List.of(genre))
                .build());
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        //when
        var actualBooks = sut.getAllBooks();

        //then
        assertThat(actualBooks)
                .containsAll(expectedBooks);
    }

    @ParameterizedTest
    @MethodSource("getAuthorsAndNamesCombinations")
    @DisplayName("Должен обновлять книгу, у которой нет жанров")
    void shouldUpdateBook_whenNoGenres(String name, String newName, Author author, Author newAuthor) {
        //given
        var genre = Genre.builder()
                .id(3L)
                .genreName("genreName")
                .build();
        var bookBefore = Book.builder()
                .id(1L)
                .name(name)
                .author(author)
                .genres(List.of(genre))
                .build();
        var expectedBook = Book.builder()
                .id(1L)
                .name(newName)
                .author(newAuthor)
                .genres(List.of())
                .build();
        when(bookRepository.findById(expectedBook.getId())).thenReturn(Optional.of(bookBefore));
        if (nonNull(newAuthor)) {
            when(authorService.saveAuthorIfNotExists(newAuthor)).thenReturn(newAuthor);
        }

        //when
        sut.updateBookById(expectedBook);

        //then
        verify(bookRepository, times(1)).updateNameById(expectedBook.getId(), expectedBook.getName());
        verify(bookRepository, times(1)).updateAuthor(expectedBook.getId(), newAuthor);
        verify(bookRepository, times(1)).deleteBookGenreLinks(expectedBook.getId());
    }

    private static Stream<Arguments> getAuthorsAndNamesCombinations() {
        var author1 = Author.builder()
                .id(2L)
                .fullName("fullName")
                .build();
        var author2 = Author.builder()
                .id(4L)
                .fullName("newFullName")
                .build();
        return Stream.of(
                Arguments.of("name", null, author1, null),
                Arguments.of(null, "name", null, author1),
                Arguments.of("name", "newName", author1, author2)
        );
    }

    @Test
    @DisplayName("Должен обновлять книгу c новыми жанрами")
    void shouldUpdateBook_whenNewGenres() {
        //given
        var oldGenre = Genre.builder()
                .id(3L)
                .genreName("genreName")
                .build();
        var newGenre = Genre.builder()
                .id(4L)
                .genreName("newGenreName")
                .build();
        var bookBefore = Book.builder()
                .id(1L)
                .name("name")
                .author(null)
                .genres(List.of(oldGenre))
                .build();
        var expectedBook = Book.builder()
                .id(1L)
                .name("name")
                .author(null)
                .genres(List.of(newGenre))
                .build();
        when(bookRepository.findById(expectedBook.getId())).thenReturn(Optional.of(bookBefore));

        //when
        sut.updateBookById(expectedBook);

        //then
        verify(bookRepository, times(0)).updateNameById(expectedBook.getId(), expectedBook.getName());
        verify(bookRepository, times(0)).updateAuthor(expectedBook.getId(), expectedBook.getAuthor());
        verify(bookRepository, times(1)).addGenreToBook(expectedBook.getId(), newGenre);
        verify(bookRepository, times(1)).deleteGenreFromBook(expectedBook.getId(), oldGenre);
    }

    @Test
    @DisplayName("Должен удалять книгу по id")
    void shouldDeleteBookById() {
        //given
        var book = Book.builder().id(1L).build();

        //when
        sut.deleteBookById(book.getId());

        //then
        verify(bookRepository, times(1)).deleteById(book.getId());
    }

}