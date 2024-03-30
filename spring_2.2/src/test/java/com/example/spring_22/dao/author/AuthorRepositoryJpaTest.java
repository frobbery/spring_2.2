package com.example.spring_22.dao.author;

import com.example.spring_22.config.YamlPropertySourceFactory;
import com.example.spring_22.domain.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с авторами должно:")
@DataJpaTest
@Import(AuthorRepositoryJpa.class)
@TestPropertySource(value = "/application-test.yml", factory = YamlPropertySourceFactory.class)
@Sql(value = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class AuthorRepositoryJpaTest {

    @Autowired
    private AuthorRepositoryJpa sut;

    @Test
    void shouldGetByFullname() {
        //given
        var expectedAuthor = Author.builder()
                .id(1L)
                .fullName("Pushkin")
                .build();

        //when
        var actualAuthor = sut.getByFullName(expectedAuthor.getFullName());

        //then
        assertThat(actualAuthor)
                .isNotEmpty()
                .contains(expectedAuthor);
    }

    @Test
    void shouldSaveNewAuthor() {
        //given
        var expectedAuthor = Author.builder()
                .id(3L)
                .fullName("New")
                .build();

        //when
        var actualAuthor = sut.save(expectedAuthor);

        //then
        assertThat(actualAuthor)
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthor);
    }
}