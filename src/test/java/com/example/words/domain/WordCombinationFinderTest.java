package com.example.words.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WordCombinationFinderTest {

    private final WordCombinationFinder finder = new WordCombinationFinder();

    @Test
    void findsCombinationsWithAnyNumberOfParts() {
        List<String> result = finder.find(
                List.of("foobar", "fo", "obar", "o", "bar", "f", "oo"), 6);

        assertThat(result).containsExactly(
                "f+o+o+bar=foobar",
                "f+o+obar=foobar",
                "f+oo+bar=foobar",
                "fo+o+bar=foobar",
                "fo+obar=foobar");
    }

    @Test
    void ignoresWordsThatAreNotCompleteTargetWords() {
        assertThat(finder.find(List.of("fo", "obar"), 6)).isEmpty();
    }

    @Test
    void supportsAnotherTargetLength() {
        assertThat(finder.find(List.of("cat", "c", "at"), 3))
                .containsExactly("c+at=cat");
    }
}
