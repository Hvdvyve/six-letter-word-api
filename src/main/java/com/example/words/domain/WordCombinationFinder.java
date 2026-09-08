package com.example.words.domain;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class WordCombinationFinder implements CombinationFinder {

    @Override
    public List<String> find(List<String> words, int targetLength) {
        if (targetLength < 1) {
            throw new IllegalArgumentException("targetLength must be greater than zero");
        }

        Set<String> dictionary = normalize(words);
        List<String> targets = dictionary.stream()
                .filter(word -> word.length() == targetLength)
                .sorted()
                .toList();
        List<String> results = new ArrayList<>();

        for (String target : targets) {
            findForTarget(target, dictionary, new ArrayList<>(), 0, results);
        }

        return results;
    }

    private void findForTarget(
            String target,
            Set<String> dictionary,
            List<String> parts,
            int position,
            List<String> results) {
        if (position == target.length()) {
            if (parts.size() > 1) {
                results.add(format(parts, target));
            }
            return;
        }

        dictionary.stream()
                .filter(word -> target.startsWith(word, position))
                .sorted(Comparator.comparingInt(String::length).thenComparing(String::compareTo))
                .forEach(word -> {
                    parts.add(word);
                    findForTarget(target, dictionary, parts, position + word.length(), results);
                    parts.removeLast();
                });
    }

    private Set<String> normalize(List<String> words) {
        if (words == null) {
            throw new IllegalArgumentException("words must not be null");
        }

        return words.stream()
                .filter(word -> word != null && !word.isBlank())
                .map(String::trim)
                .collect(java.util.stream.Collectors.toCollection(HashSet::new));
    }

    private String format(List<String> parts, String target) {
        return String.join("+", parts) + "=" + target;
    }
}
