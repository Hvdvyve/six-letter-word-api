package com.example.words.api;

import com.example.words.domain.CombinationFinder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CombinationController {

    private final CombinationFinder combinationFinder;
    private final InputWordSource inputWordSource;
    private final DatabaseCombinationStore databaseCombinationStore;

    public CombinationController(
            CombinationFinder combinationFinder,
            InputWordSource inputWordSource,
            DatabaseCombinationStore databaseCombinationStore) {
        this.combinationFinder = combinationFinder;
        this.inputWordSource = inputWordSource;
        this.databaseCombinationStore = databaseCombinationStore;
    }

    @PostMapping("/combinations")
    public CombinationResponse findCombinations(@Valid @RequestBody CombinationRequest request) {
        inputWordSource.validate(request.words());
        return new CombinationResponse(
                combinationFinder.find(request.words(), request.resolvedTargetLength()));
    }

    @PostMapping("/file")
    public String findCombinationsFromFile() {
        return String.join(System.lineSeparator(), combinationFinder.find(inputWordSource.words(), 6));
    }

    @PostMapping("/database")
    public CombinationResponse storeCombinationsInDatabase() {
        var combinations = combinationFinder.find(inputWordSource.words(), 6);
        databaseCombinationStore.replace(combinations);
        return new CombinationResponse(combinations);
    }

    @GetMapping("/database")
    public CombinationResponse findCombinationsFromDatabase() {
        return new CombinationResponse(databaseCombinationStore.findAll());
    }
}
