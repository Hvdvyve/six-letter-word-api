package com.example.words.domain;

import java.util.List;

@FunctionalInterface
public interface CombinationFinder {

    List<String> find(List<String> words, int targetLength);
}
