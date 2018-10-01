package plasma.xmf.util;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCrawler {

    private static final String WHITESPACE = " \t\n\r\f";

    private final String string;
    private int currPos = 0;

    public StringCrawler(String string) {
        Objects.requireNonNull(string);
        this.string = string;
    }

    public StringCrawler(String string, int currPos) {
        this(string);
        this.currPos = currPos;
    }

    public String getString() {
        return string;
    }

    public String getRemaining() {
        return string.substring(currPos);
    }

    public int getCurrentPos() {
        return currPos;
    }

    public int getLength() {
        return string.length();
    }

    public boolean hasRemaining() {
        return currPos < string.length();
    }

    private void canContinue() {
        if (!hasRemaining()) {
            throw new StringIndexOutOfBoundsException("Traversed too far!");
        }
    }

    private String traverseWhileContainsAnyOf(String target) {
        StringBuilder sb = new StringBuilder();
        for (char c = string.charAt(currPos); hasRemaining() && target.indexOf(c) != -1; currPos++) {
            sb.append(c);
        }
        return sb.toString();
    }

    private String traverseWhileContains(String target) {
        StringBuilder sb = new StringBuilder();
        while (string.startsWith(target, currPos)) {
            sb.append(target);
            currPos += target.length();
        }
        return sb.toString();
    }

    public String nextWhitespace() {
        if (!hasRemaining())
            return null;
        return traverseWhileContainsAnyOf(WHITESPACE);
    }

    public char nextChar() {
        if (!hasRemaining())
            return '\u0000';
        return string.charAt(currPos++);
    }

    public String nextWord() {
        if (!hasRemaining())
            return null;
        nextWhitespace();
        StringBuilder sb = new StringBuilder();
        for (char c = string.charAt(currPos); hasRemaining() && WHITESPACE.indexOf(c) == -1; currPos++) {
            sb.append(c);
        }
        return sb.toString();
    }

    public String nextRegex(String regex) {
        if (!hasRemaining())
            return null;

        Pattern pattern = Pattern.compile("^" + regex)
        Matcher m = pattern.matcher(getRemaining());
        if (!)
    }
}
