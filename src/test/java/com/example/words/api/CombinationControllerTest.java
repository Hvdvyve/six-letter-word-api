package com.example.words.api;

import com.example.words.domain.CombinationFinder;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CombinationControllerTest {

    private final CombinationFinder finder = mock(CombinationFinder.class);
    private final DatabaseCombinationStore databaseStore = mock(DatabaseCombinationStore.class);
    private final LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    private final MockMvc mvc = org.springframework.test.web.servlet.setup.MockMvcBuilders
            .standaloneSetup(new CombinationController(finder, new InputWordSource(), databaseStore))
            .setValidator(validator)
            .setControllerAdvice(new ApiExceptionHandler())
            .build();

    @Test
    void combinationsUsesWordsFromRequest() throws Exception {
        when(finder.find(eq(java.util.List.of("abroad", "a", "broad")), eq(6)))
                .thenReturn(java.util.List.of("a+broad=abroad"));

        mvc.perform(post("/api/combinations")
                        .contentType("application/json")
                        .content("""
                                {"words":["abroad","a","broad"],"targetLength":6}
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"combinations":["a+broad=abroad"]}
                        """));

        verify(finder).find(java.util.List.of("abroad", "a", "broad"), 6);
    }

    @Test
    void combinationsShowsMissingWordError() throws Exception {
        mvc.perform(post("/api/combinations")
                        .contentType("application/json")
                        .content("""
                                {"words":["abroad","unknown"],"targetLength":6}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {"error":"Words not present in input.txt: unknown"}
                        """));
    }

    @Test
    void fileEndpointUsesInputFileAndSixCharacterTargets() throws Exception {
        when(finder.find(new InputWordSource().words(), 6))
                .thenReturn(java.util.List.of("a+broad=abroad"));

        mvc.perform(post("/api/file"))
                .andExpect(status().isOk())
                .andExpect(content().string("a+broad=abroad"));

        verify(finder).find(new InputWordSource().words(), 6);
    }

    @Test
    void combinationsShowsRequestValidationError() throws Exception {
        mvc.perform(post("/api/combinations")
                        .contentType("application/json")
                        .content("""
                                {"words":[],"targetLength":6}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {"error":"words: words must contain at least one word"}
                        """));
    }

    @Test
    void databaseEndpointStoresFileCombinations() throws Exception {
        when(finder.find(new InputWordSource().words(), 6))
                .thenReturn(java.util.List.of("a+broad=abroad"));

        mvc.perform(post("/api/database"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"combinations":["a+broad=abroad"]}
                        """));

        verify(databaseStore).replace(java.util.List.of("a+broad=abroad"));
    }

    @Test
    void databaseEndpointRetrievesStoredCombinations() throws Exception {
        when(databaseStore.findAll()).thenReturn(java.util.List.of("a+broad=abroad"));

        mvc.perform(get("/api/database"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"combinations":["a+broad=abroad"]}
                        """));
    }
}
