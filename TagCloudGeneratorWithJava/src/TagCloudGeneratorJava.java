import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Put a short phrase describing the program here.
 *
 * @author Henry Saltzman
 *
 */
public final class TagCloudGeneratorJava {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private TagCloudGeneratorJava() {
    }

    /**
     * Definition of whitespace separators.
     */
    private static final String SEPARATORS = " \t\n\r,-.!?[]';:/()";

    /**
     * Comparator to sort map.pair objects by their count.
     */
    private static class SortByCount
            implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {
            return o2.getValue().compareTo(o1.getValue());
        }
    }

    /**
     * Comparator to sort map.pair objects by their name (alphabetical).
     */
    private static class SortByAlphabet
            implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {
            return o1.getKey().compareTo(o2.getKey());

        }
    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code SEPARATORS}) or "separator string" (maximal length string of
     * characters in {@code SEPARATORS}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection entries(SEPARATORS) = {}
     * then
     *   entries(nextWordOrSeparator) intersection entries(SEPARATORS) = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection entries(SEPARATORS) /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of entries(SEPARATORS)  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of entries(SEPARATORS))
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position) {
        assert text != null : "Violation of: text is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        int i = position + 1;

        boolean word = SEPARATORS.indexOf(text.charAt(position)) < 0;

        while (i < text.length()
                && word == SEPARATORS.indexOf(text.charAt(i)) < 0) {

            i++;

        }

        return text.substring(position, i);
    }

    /**
     * Method takes a SimpleReader object with text and outputs a map with each
     * word and the number of times that it occurs.
     *
     * @return map with each word and the number of times that it occurs
     * @param text
     *            object with text from file
     */
    private static Map<String, Integer> processText(BufferedReader text) {

        Map<String, Integer> wordFrequencies = new HashMap<>();

        try {
            String line = text.readLine();

            while (!(line == null)) {

                int i = 0;

                while (i < line.length()) {

                    String next = nextWordOrSeparator(line, i);

                    if (SEPARATORS.indexOf(next.charAt(0)) < 0) {

                        next = next.toLowerCase();
                        if (wordFrequencies.containsKey(next)) {
                            wordFrequencies.replace(next,
                                    wordFrequencies.get(next),
                                    wordFrequencies.get(next) + 1);
                        } else {
                            wordFrequencies.put(next, 1);
                        }
                    }

                    i += next.length();
                }

                line = text.readLine();
            }
        } catch (IOException e) {
            System.err.println("Error reading from file");
        }

        return wordFrequencies;

    }

    /**
     * Method fills both sorting machines needed for sorting data.
     *
     * @return array with minimum and maximum frequencies
     * @param terms
     *            map with all terms and frequencies
     * @param sortAlpha
     *            ArrayList to sort alphabetically
     * @param n
     *            number of words appearing in cloud tag
     */
    private static int[] fillSortingMachines(Map<String, Integer> terms,
            ArrayList<Map.Entry<String, Integer>> sortAlpha, int n) {

        int n1 = n;
        Comparator<Map.Entry<String, Integer>> countSort = new SortByCount();
        ArrayList<Map.Entry<String, Integer>> sortCount = new ArrayList<>();
        Comparator<Map.Entry<String, Integer>> alphaSort = new SortByAlphabet();

        int[] largestAndTotalFreq = new int[2];

        Set<Map.Entry<String, Integer>> set = terms.entrySet();
        for (Map.Entry<String, Integer> s : set) {
            sortCount.add(s);
        }

        sortCount.sort(countSort);

        if (n1 > sortCount.size()) {
            n1 = sortCount.size();
            System.out.println("Program auto set n to the number of words"
                    + " in file because n was greater than number of words in file.");
        }

        for (int i = 0; i < n1; i++) {
            Map.Entry<String, Integer> mapPair = sortCount.get(i);
            sortAlpha.add(mapPair);
        }
        largestAndTotalFreq[0] = sortAlpha.get(0).getValue();
        largestAndTotalFreq[1] = sortAlpha.get(sortAlpha.size() - 1).getValue();

        sortAlpha.sort(alphaSort);

        return largestAndTotalFreq;

    }

    /**
     * Method prints tagCloud in HTML.
     *
     * @param output
     *            PrintWriter to output HTML file
     * @param sortAlpha
     *            ArrayList to sort alphabetically
     * @param n
     *            number of words appearing in cloud tag
     * @param inputName
     *            name of input file
     * @param maxFreq
     *            array with minimum and maximum frequencies
     */
    private static void printOutput(PrintWriter output,
            ArrayList<Map.Entry<String, Integer>> sortAlpha, int n,
            String inputName, int[] maxFreq) {
        int n1 = n;
        if (n1 > sortAlpha.size()) {
            n1 = sortAlpha.size();
        }

        output.println("<html>");
        output.println("<head>");
        output.println(
                "<title> Top " + n1 + " words in " + inputName + "</title>");
        output.println(
                "<link href=\"http://web.cse.ohio-state.edu/software/2231/"
                        + "web-sw2/assignments/projects/tag-cloud-generator/data/ta"
                        + "gcloud.css\" rel=\"stylesheet\" type=\"text/css\">");
        output.println(
                "<link href=\"tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">");
        output.println("</head>");
        output.println("<body>");
        output.println("<h2> Top " + n1 + " words in " + inputName + "</h2>");
        output.println("<hr>");
        output.println("<div class=\"cdiv\">");
        output.println("<p class=\"cbox\">");
        while (sortAlpha.size() > 0) {
            Map.Entry<String, Integer> map = sortAlpha.remove(0);
            output.println("<span style=\"cursor:default\" class=\"f"
                    + ((int) (((48.0 - 11.0) / (1.0 * maxFreq[0] - maxFreq[1]))
                            * (double) map.getValue()
                            + (48.0 - maxFreq[0] * ((48.0 - 11.0)
                                    / (1.0 * maxFreq[0] - maxFreq[1])))))
                    + "\" title=\"count: " + map.getValue() + "\">"
                    + map.getKey() + "</span>");
        }
        output.println("</p>");
        output.println("</div>");
        output.println("</body>");
        output.println("</html>");
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        String inputName;
        String outputName;
        int n = 0;

        System.out.println("Enter the name of an input file: ");
        inputName = input.nextLine();

        System.out.println("Enter the name of the output file: ");
        outputName = input.nextLine();

        System.out.println(
                "Enter the the number of words to be included in the tag cloud ");
        boolean parsed = false;
        while (!parsed) {
            try {
                n = Integer.parseInt(input.nextLine());
                parsed = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid entry. " + "Enter the the number of"
                        + " words to be included in the tag cloud ");
            }
        }
        input.close();

        PrintWriter output;
        try {
            output = new PrintWriter(
                    new BufferedWriter(new FileWriter(outputName)));
        } catch (IOException e) {
            System.err.println("Error creating file to write.");
            return;
        }

        BufferedReader inputFile;
        try {
            inputFile = new BufferedReader(new FileReader(inputName));
        } catch (IOException e) {
            System.err.println("Error opening file.");
            output.close();
            return;
        }

        Map<String, Integer> termFrequencies = processText(inputFile);
        ArrayList<Map.Entry<String, Integer>> alpha = new ArrayList<>();
        int[] maxFreq = fillSortingMachines(termFrequencies, alpha, n);

        printOutput(output, alpha, n, inputName, maxFreq);

        output.close();
        try {
            inputFile.close();
        } catch (IOException e) {
            System.err.println("Error closing reading file");
            return;
        }

    }
}
