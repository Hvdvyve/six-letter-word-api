package com.example.words.api;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InputWordSource {

    private final List<String> words;
    private final Set<String> wordSet;

    public InputWordSource() {
        try {
            words = new ClassPathResource("input.txt")
                    .getContentAsString(StandardCharsets.UTF_8)
                    .lines()
                    .toList();
            wordSet = words.stream()
                    .map(String::trim)
                    .filter(word -> !word.isBlank())
                    .collect(Collectors.toUnmodifiableSet());
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load input.txt", exception);
        }
    }

    public List<String> words() {
        return words;
    }

    public void validate(List<String> requestedWords) {
        List<String> missingWords = requestedWords.stream()
                .filter(word -> word == null || !wordSet.contains(word.trim()))
                .distinct()
                .toList();

        if (!missingWords.isEmpty()) {
            throw new IllegalArgumentException(
                    "Words not present in input.txt: " + String.join(", ", missingWords));
        }
    }
}
