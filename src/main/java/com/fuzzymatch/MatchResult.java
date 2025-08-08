package com.fuzzymatch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MatchResult {
    private String beneficiary;
    private double score;
}
