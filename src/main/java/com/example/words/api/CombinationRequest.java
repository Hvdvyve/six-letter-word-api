package com.example.words.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Min;

import java.util.List;

public record CombinationRequest(
        @NotEmpty(message = "words must contain at least one word")
        List<String> words,
        @Min(value = 1, message = "targetLength must be greater than zero")
        Integer targetLength) {

    public int resolvedTargetLength() {
        return targetLength == null ? 6 : targetLength;
    }
}
