package com.fuzzymatch;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for performing fuzzy name matching.
 */
@RestController
@RequestMapping("/api/fuzzy-match") // Base URL for clarity and versioning
@RequiredArgsConstructor
public class FuzzyMatchController {

    private final FuzzyMatcher fuzzyMatcher;

    /**
     * Matches a customer name with a beneficiary name and returns a similarity score.
     *
     * @param request contains customer and beneficiary names
     * @return MatchResult containing the beneficiary name and similarity score
     */
    @Operation(summary = "Fuzzy match customer name with a beneficiary")
    @ApiResponse(
            responseCode = "200",
            description = "Matching results returned",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MatchResult.class)
            )
    )

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public MatchResult matchName(@RequestBody MatchRequest request) {
        double score = fuzzyMatcher.getBestMatchScore(request.getCustomer(), request.getBeneficiary());
        return new MatchResult(request.getBeneficiary(), score);
    }
}
