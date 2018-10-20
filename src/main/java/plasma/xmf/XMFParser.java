package plasma.xmf;

import plasma.xmf.exceptions.InvalidBlockStartException;
import plasma.xmf.exceptions.InvalidMacroDeclarationException;
import plasma.xmf.exceptions.InvalidVerbException;
import plasma.xmf.exceptions.MalformedBlockException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class XMFParser {

    private static final String WHITESPACE = " \t\n\r\f";
    private static final String BLOCK_START = "'\"";

    private static final Map<String, String> CHARACTER_ESCAPES = new HashMap<>();

    static {
        CHARACTER_ESCAPES.put("\\n", "\n");
        CHARACTER_ESCAPES.put("\\#", "#");
        CHARACTER_ESCAPES.put("\\$", "$");
    }

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

    private static String handleBlock(ManifestContext context, String block) {
        int blockType = block.startsWith("'") ? 1 : (block.startsWith("\"") ? 2 : 0);
        if (blockType == 0) { //Implicit block
            for (String macro : context.getAvailableMacros()) {
                block = block.replaceAll(String.format("(?i:\\$%s)", macro), context.getMacro(macro));
            }
            for (String toReplace : CHARACTER_ESCAPES.keySet()) {
                block = block.replaceAll(toReplace, CHARACTER_ESCAPES.get(toReplace));
            }
            StringBuilder newBlock = new StringBuilder();
            for (String line : block.split("\n")) {
                newBlock.append(line).append("\n");
            }
            block = newBlock.toString();
            return block.substring(0, block.length()-1);
        } else { //Explicit literal block, so we don't need to do pre processing
            if ((blockType == 1 && !block.endsWith("'")) || (blockType == 2 && !block.endsWith("\""))) {
                throw new MalformedBlockException("Block not quoted properly!");
            }
            return block.substring(1, block.length()-1);
        }
    }

    public static void handleXmf(String xmfString) {
        List<String> chunks = chunkString(xmfString);
        ManifestContext context = new ManifestContext();
        List<ExecutionPair> verbs = new ArrayList<>();
        for (String chunk : chunks) {
            if (chunk.startsWith("$")) {
                if (!chunk.contains("<-")) {
                    throw new MalformedBlockException("Blocks cannot start with a macro unless its a declaration!");
                }
                String macroName = chunk.split("<-")[0].substring(1);
                if (macroName.isEmpty() || macroName.contains(" ")) {
                    throw new InvalidMacroDeclarationException("Macro name is invalid!");
                }
                String block = handleBlock(context, chunk.substring(1 + macroName.length() + 2));
                context.setMacro(macroName.toLowerCase(), block);
            } else {
                String verb = chunk.split(" ")[0].toLowerCase();
                if (verb.isEmpty() || verb.contains("$")) {
                    throw new InvalidVerbException("Verb name is invalid!");
                }

                String block = chunk.contains(" ") ? handleBlock(context, chunk.substring(verb.length() + 1)) : "";

                if (verb.equals("import")) {  //Special case handling

                } else if (verb.equals("define")) {  //More special case handling

                } else {
                    verbs.add(new ExecutionPair(verb, block));
                }
            }
        }
    }
}
