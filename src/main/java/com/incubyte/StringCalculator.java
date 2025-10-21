package com.incubyte;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

    public int add(String numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }

        DelimiterAndRest dar = extractDelimiter(numbers);

        String[] tokens = splitNumbers(dar.rest, dar.delimiters);

        List<Integer> negatives = new ArrayList<>();
        int sum = 0;
        for (String t : tokens) {
            if (t.isEmpty()) {
                continue;

            }
            int value = Integer.parseInt(t);
            if (value < 0) {
                negatives.add(value);
            }
            sum += value;
        }

        if (!negatives.isEmpty()) {
            throw new NegativeNumberException("negative numbers not allowed " + negativesToString(negatives));
        }

        return sum;
    }

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

    private static class DelimiterAndRest {

        String[] delimiters;
        String rest;

        DelimiterAndRest(String[] delimiters, String rest) {
            this.delimiters = delimiters;
            this.rest = rest;
        }
    }

    private DelimiterAndRest extractDelimiter(String numbers) {
        if (numbers.startsWith("//")) {
            int newlineIndex = numbers.indexOf("\n");
            String header = numbers.substring(2, newlineIndex);
            String rest = numbers.substring(newlineIndex + 1);

            if (header.startsWith("[") && header.endsWith("]")) {
                List<String> delims = new ArrayList<>();
                Matcher m = Pattern.compile("\\[(.*?)]").matcher(header);
                while (m.find()) {
                    delims.add(Pattern.quote(m.group(1)));
                }
                return new DelimiterAndRest(delims.toArray(String[]::new), rest);
            } else {
                return new DelimiterAndRest(new String[]{Pattern.quote(header)}, rest);
            }
        }
        return new DelimiterAndRest(new String[]{",", "\n"}, numbers);
    }

    private String[] splitNumbers(String rest, String[] delimiters) {
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
