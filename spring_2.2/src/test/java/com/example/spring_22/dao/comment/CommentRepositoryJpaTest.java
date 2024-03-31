package com.example.spring_22.dao.comment;

import com.example.spring_22.config.YamlPropertySourceFactory;
import com.example.spring_22.domain.Book;
import com.example.spring_22.domain.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с комментариями должно:")
@DataJpaTest
@Import(CommentRepositoryJpa.class)
@TestPropertySource(value = "/application-test.yml", factory = YamlPropertySourceFactory.class)
@Sql(value = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class CommentRepositoryJpaTest {

    @Autowired
    private CommentRepositoryJpa sut;

    @Test
    @DisplayName("Сохранять комментарий")
    void shouldSaveComment() {
        //given
        var expectedComment = Comment.builder()
                .id(3L)
                .text("new")
                .build();

        //when
        var actualComment = sut.save(expectedComment);

        //then
        assertThat(actualComment)
                .usingRecursiveComparison()
                .isEqualTo(expectedComment);
    }

    @Test
    @DisplayName("Находить комментарий по id")
    void shouldFindCommentById() {
        //given
        var expectedComment = Comment.builder()
                .id(1L)
                .text("GOOD")
                .build();

        //when
        var actualComment = sut.findById(expectedComment.getId());

        //then
        assertThat(actualComment)
                .isPresent()
                .usingRecursiveComparison()
                .ignoringFields("value.book")
                .isEqualTo(Optional.of(expectedComment));
    }

    @Test
    @DisplayName("Возврашать все комментарии по id книги")
    void shouldFindAllCommentsByBookId() {
        //given
        var bookId = 1L;
        var expectedComments = List.of(Comment.builder()
                        .id(1L)
                        .text("GOOD")
                        .build(),
                Comment.builder()
                        .id(2L)
                        .text("BAD")
                        .build());

        //when
        var actualComments = sut.findAllByBookId(bookId);

        //then
        assertThat(actualComments)
                .usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(expectedComments);
    }

    @Test
    @DisplayName("Обновлять текст комментария по id")
    void shouldUpdateCommentTextById() {
        //given
        var expectedComment = Comment.builder()
                .id(1L)
                .text("new")
                .build();

        //when
        sut.updateTextById(expectedComment.getId(), expectedComment.getText());

        //then
        var actualComment = sut.findById(expectedComment.getId());
        assertThat(actualComment)
                .isPresent()
                .usingRecursiveComparison()
                .ignoringFields("value.book")
                .isEqualTo(Optional.of(expectedComment));
    }

    @Test
    @DisplayName("Удалять комментарий по id")
    void shouldDeleteCommentById() {
        //given
        var commentId = 2L;

        //when
        sut.deleteById(commentId);

        //then
        assertThat(sut.findById(commentId))
                .isEmpty();
    }

    @Test
    @DisplayName("Должен обновлять книгу комментария")
    void shouldUpdateCommentBook() {
        //given
        var expectedComment = Comment.builder()
                .id(2L)
                .text("BAD")
                .build();
        var book = Book.builder()
                .id(2L)
                .build();

        //when
        sut.updateCommentBook(expectedComment, book);

        //then
        assertThat(sut.findAllByBookId(book.getId()))
                .hasSize(1)
                .usingRecursiveComparison()
                .ignoringFields("book")
                .isEqualTo(List.of(expectedComment));
    }
}