package com.example.spring_22.dao.genre;

import com.example.spring_22.config.YamlPropertySourceFactory;
import com.example.spring_22.domain.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с жанрами должно:")
@DataJpaTest
@Import(GenreRepositoryJpa.class)
@TestPropertySource(value = "/application-test.yml", factory = YamlPropertySourceFactory.class)
@Sql(value = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class GenreRepositoryJpaTest {

    @Autowired
    private GenreRepositoryJpa sut;

    @Test
    @DisplayName("Возврашать жанр по названию")
    void shouldGetGenreByName() {
        //given
        var expectedGenre = Genre.builder()
                .id(1L)
                .genreName("Adventure")
                .build();

        //when
        var actualGenre = sut.getByName(expectedGenre.getGenreName());

        //then
        assertThat(actualGenre)
                .isNotEmpty()
                .contains(expectedGenre);
    }

    @Test
    @DisplayName("Сохранять жанр")
    void shouldSaveNewGenre() {
        //given
        var expectedGenre = Genre.builder()
                .id(3L)
                .genreName("New")
                .build();

        //when
        var actualGenre = sut.save(expectedGenre);

        //then
        assertThat(actualGenre)
                .usingRecursiveComparison()
                .isEqualTo(expectedGenre);
    }
}