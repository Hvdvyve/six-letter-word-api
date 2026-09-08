package com.example.words.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(DatabaseCombinationStore.class)
class DatabaseCombinationStoreTest {

    @Autowired
    private DatabaseCombinationStore store;

    @Test
    void replacesAndReadsCombinationsInInsertionOrder() {
        store.replace(List.of("a+broad=abroad", "a+board=aboard"));

        assertThat(store.findAll())
                .containsExactly("a+broad=abroad", "a+board=aboard");
    }

    @Test
    void replacingWithEmptyResultsClearsStoredCombinations() {
        store.replace(List.of("a+broad=abroad"));

        store.replace(List.of());

        assertThat(store.findAll()).isEmpty();
    }
}
