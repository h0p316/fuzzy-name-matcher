package com.fuzzymatch;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service // Marks this class as a Spring Service (for dependency injection)
public class FuzzyMatcher {

    /**
     * Normalizes a name by:
     * - Converting to lowercase
     * - Removing all non-alphabetic characters except spaces
     * - Replacing multiple spaces with a single space
     * - Trimming leading/trailing spaces
     */
    private String normalize(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z ]", "")  // Remove everything except a-z and space
                .replaceAll("\\s+", " ")    // Replace multiple spaces with single space
                .trim();                    // Remove leading/trailing spaces
    }

    /**
     * Calculates the Jaccard similarity at the word level between two strings.
     * Jaccard = (Intersection size) / (Union size)
     */
    private double wordLevelJaccard(String s1, String s2) {
        // Convert both names to sets of words
        Set<String> set1 = new HashSet<>(Arrays.asList(normalize(s1).split(" ")));
        Set<String> set2 = new HashSet<>(Arrays.asList(normalize(s2).split(" ")));

        // Find intersection
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        // Find union
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        // Return ratio of intersection to union, or 0 if union is empty
        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }

    /**
     * Calculates Levenshtein similarity between two strings.
     * Levenshtein similarity = 1 - (edit distance / max length of strings)
     */
    private double levenshteinSim(String s1, String s2) {
        LevenshteinDistance dist = new LevenshteinDistance();
        int distance = dist.apply(normalize(s1), normalize(s2));
        int maxLen = Math.max(s1.length(), s2.length());

        // If both strings are empty, similarity is 1 (perfect match)
        return maxLen == 0 ? 1.0 : 1.0 - ((double) distance / maxLen);
    }

    /**
     * Generates all permutations of the words in a given name.
     * Example: "John Doe" -> ["John Doe", "Doe John"]
     */
    private List<String> getAllPermutations(String name) {
        List<String> parts = Arrays.asList(normalize(name).split(" "));
        List<String> result = new ArrayList<>();
        permute(parts, 0, result);
        return result;
    }

    /**
     * Recursively generates permutations using backtracking.
     */
    private void permute(List<String> arr, int k, List<String> result) {
        for (int i = k; i < arr.size(); i++) {
            Collections.swap(arr, i, k);          // Swap current index with k
            permute(arr, k + 1, result);          // Recurse for next index
            Collections.swap(arr, k, i);          // Swap back (backtracking)
        }
        // When all positions are fixed, join into a string and add to results
        if (k == arr.size() - 1) {
            result.add(String.join(" ", arr));
        }
    }

    /**
     * Calculates the best fuzzy match score between customer name and beneficiary name:
     * - Creates all permutations of customer name words
     * - For each permutation, computes:
     * - Word-level Jaccard similarity
     * - Levenshtein similarity
     * - Takes the average of these two scores for each permutation
     * - Keeps the highest score found
     * - Returns the score as a percentage (0.00 - 100.00)
     */
    public double getBestMatchScore(String custName, String beneName) {
        List<String> custPerms = getAllPermutations(custName);
        double bestScore = 0.0;

        // Compare each permutation of customer name to beneficiary name
        for (String perm : custPerms) {
            double jaccard = wordLevelJaccard(perm, beneName);
            double levenshtein = levenshteinSim(perm, beneName);

            // Average of Jaccard & Levenshtein
            double combinedScore = (jaccard + levenshtein) / 2.0;

            // Keep the highest score
            bestScore = Math.max(bestScore, combinedScore);
        }

        // Convert to percentage and round to 2 decimal places
        BigDecimal finalScore = BigDecimal.valueOf(bestScore * 100)
                .setScale(2, RoundingMode.HALF_UP);

        return finalScore.doubleValue();
    }
}
