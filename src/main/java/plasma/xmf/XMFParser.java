package plasma.xmf;

import plasma.xmf.exceptions.InvalidBlockStartException;
import plasma.xmf.exceptions.MalformedBlockException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public final class XMFParser {

    private static final Pattern MACRO_DECLARATION = Pattern.compile("(\\$[a-zA-Z0-9]+<-(?=.))");
    private static final Pattern MACRO = Pattern.compile("(\\$[a-zA-Z0-9]+)");
    private static final String WHITESPACE = " \t\n\r\f";
    private static final String BLOCK_START = "'\"";

    private static String stripComments(String s) {
        String[] lines = s.split("\n+");
        StringBuilder newS = new StringBuilder();
        for (String l : lines) {
            if (l.contains("#")) {
                if (!l.startsWith("#")) {
                    int from = 0;
                    int i;
                    while ((i = l.indexOf("#", from)) != -1) {
                        if (l.charAt(i) == '\\') {
                            from = i+1;
                        } else {
                            break;
                        }
                    }
                    newS.append(l, 0, i).append("\n");
                }
            } else {
                newS.append(l).append("\n");
            }
        }
        return newS.toString();
    }

    private static String stripEmptyLines(String s) {
        String[] lines = s.split("\n");
        StringBuilder newS = new StringBuilder();
        for (String line : lines) {
            if (line.isEmpty())
                continue;
            newS.append(line).append("\n");
        }
        return newS.toString();
    }

    private static <T> void replaceLast(List<T> l, Function<T, T> replacer) {
        int i = l.size() - 1;
        T t = l.get(i);
        l.set(i, replacer.apply(t));
    }

    private static String getClause(String line) {
        int i = line.indexOf(" ");
        if (i == -1)
            return null;
        return line.substring(i+1);
    }

    private static List<String> chunkString(String s) {
        s = stripComments(s);
        s = stripEmptyLines(s);

        // Need to do primitive block parsing
        List<String> chunks = new ArrayList<>();
        int chunkType = -1; //-1=none or whitespace delineated, 0=single quote, 1=double quote
        for (String line : s.split("\n")) {
            switch (chunkType) {
                case -1:
                    if (WHITESPACE.indexOf(line.charAt(0)) == -1 && BLOCK_START.indexOf(line.charAt(0)) == -1) {
                        chunks.add(line);
                        String clause = getClause(line);
                        if (clause != null) {
                            if (clause.charAt(0) == '\'') {
                                chunkType = 0;
                            } else if (clause.charAt(0) == '\"') {
                                chunkType = 1;
                            }
                        }
                    } else {
                        if (chunks.size() == 0 || BLOCK_START.indexOf(line.charAt(0)) == -1) {
                            throw new InvalidBlockStartException("Line '" + line + "' has illegally started a block!");
                        }

                        replaceLast(chunks, str -> str + "\n" + line);
                    }
                    break;
                case 0:
                    if (line.indexOf('\'') != -1) {
                        replaceLast(chunks, str -> str + "\n" + line.substring(line.indexOf('\'')+1));
                        chunkType = -1;
                    } else {
                        replaceLast(chunks, str -> str + "\n" + line);
                    }
                    break;
                case 1:
                    if (line.indexOf('"') != -1) {
                        replaceLast(chunks, str -> str + "\n" + line.substring(line.indexOf('"')+1));
                        chunkType = -1;
                    } else {
                        replaceLast(chunks, str -> str + "\n" + line);
                    }
                    break;
                default:
                    throw new RuntimeException("This should be impossible!");
            }
        }

        if (chunkType != -1) {
            throw new MalformedBlockException("Block not terminated!");
        }

        return chunks;
    }

    public static void handleXmf(String xmfString) {
        List<String> chunks = chunkString(xmfString);
        ManifestContext context = new ManifestContext();
        for (String chunk : chunks) {

        }
    }
}
