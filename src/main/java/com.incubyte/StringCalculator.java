package com.incubyte;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple String Calculator following the kata rules.
 */
public class StringCalculator {

    // Public API: add numbers from the input string according to kata rules.
    public int add(String numbers) {
        // Step 1: empty input returns 0
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }

        // Step 2: detect custom delimiter syntax: "//[delim]\n..."
        DelimiterAndRest dar = extractDelimiter(numbers);

        // Step 3: split by delimiter(s) including newline handling
        String[] tokens = splitNumbers(dar.rest, dar.delimiters);

        // Step 4: parse tokens, validate negatives and sum
        List<Integer> negatives = new ArrayList<>();
        int sum = 0;
        for (String t : tokens) {
            if (t.isEmpty()) {
                continue; // ignore empty tokens

            }
            int value = Integer.parseInt(t);
            if (value < 0) {
                negatives.add(value);
            }
            sum += value;
        }

        // Step 5: if negatives found, throw exception with all negatives listed
        if (!negatives.isEmpty()) {
            throw new NegativeNumberException("negative numbers not allowed " + negativesToString(negatives));
        }

        return sum;
    }

    // Helper to format negative list as comma-separated in the message
    private String negativesToString(List<Integer> negatives) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < negatives.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(negatives.get(i));
        }
        return sb.toString();
    }

    // Represents extracted delimiter(s) and the rest string (numbers part)
    private static class DelimiterAndRest {

        String[] delimiters;
        String rest;

        DelimiterAndRest(String[] delimiters, String rest) {
            this.delimiters = delimiters;
            this.rest = rest;
        }
    }

    // If custom delimiter header is present, parse it. Otherwise default delimiters.
    private DelimiterAndRest extractDelimiter(String numbers) {
        // Pattern for single-line delimiter declaration starting with //
        if (numbers.startsWith("//")) {
            // Support both forms:
            // 1) //;\n1;2   -> delimiter is ';'
            // 2) //[***]\n1***2***3 -> delimiter '***' (supports multi-char in brackets)
            // 3) //[delim1][delim2]\n... (optional multi-delimiter support)
            int newlineIndex = numbers.indexOf("\n");
            String header = numbers.substring(2, newlineIndex); // between '//' and '\n'
            String rest = numbers.substring(newlineIndex + 1);

            // if header contains [ ... ] we support multiple or multi-char delimiters
            if (header.startsWith("[") && header.endsWith("]")) {
                List<String> delims = new ArrayList<>();
                Matcher m = Pattern.compile("\\[(.*?)]").matcher(header);
                while (m.find()) {
                    delims.add(Pattern.quote(m.group(1))); // quote for regex usage
                }
                return new DelimiterAndRest(delims.toArray(new String[0]), rest);
            } else {
                // single-character delimiter (no brackets)
                return new DelimiterAndRest(new String[]{Pattern.quote(header)}, rest);
            }
        }
        // default delimiters: comma and newline
        return new DelimiterAndRest(new String[]{",", "\n"}, numbers);
    }

    // Splits the numbers string by the provided delimiters (regex-ready)
    private String[] splitNumbers(String rest, String[] delimiters) {
        // join delimiters into a regex alternation
        StringBuilder regex = new StringBuilder();
        for (int i = 0; i < delimiters.length; i++) {
            if (i > 0) {
                regex.append("|");
            }
            regex.append(delimiters[i]);
        }
        return rest.split(regex.toString());
    }
}
