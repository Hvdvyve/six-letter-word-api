package com.example.words.api;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class InputWordSourceTest {

    private final InputWordSource source = new InputWordSource();

    @Test
    void loadsWordsFromInputFile() {
        assertThat(source.words()).contains("abroad", "a", "broad");
    }

    @Test
    void acceptsWordsPresentInInputFile() {
        source.validate(List.of("abroad", "a", "broad"));
    }

    @Test
    void rejectsWordsMissingFromInputFile() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> source.validate(List.of("abroad", "unknown")))
                .withMessage("Words not present in input.txt: unknown");
    }
}
