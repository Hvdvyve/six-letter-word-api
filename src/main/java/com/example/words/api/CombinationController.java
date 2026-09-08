package com.example.words.api;

import com.example.words.domain.CombinationFinder;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CombinationController {

    private final CombinationFinder combinationFinder;

    public CombinationController(CombinationFinder combinationFinder) {
        this.combinationFinder = combinationFinder;
    }

    @PostMapping(value = "/combinations",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CombinationResponse findCombinations(@Valid @RequestBody CombinationRequest request) {
        return new CombinationResponse(
                combinationFinder.find(request.words(), request.resolvedTargetLength()));
    }

    @PostMapping(value = "/file",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public String findCombinationsFromFile(@RequestBody String input) {
        List<String> words = input.lines().toList();
        return String.join(System.lineSeparator(), combinationFinder.find(words, 6));
    }
}
