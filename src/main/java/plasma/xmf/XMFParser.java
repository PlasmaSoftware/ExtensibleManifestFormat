package plasma.xmf;

import plasma.xmf.exceptions.InvalidMacroDeclarationException;
import plasma.xmf.exceptions.XMFException;
import plasma.xmf.tokens.AbstractToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class XMFParser {

    private static final Pattern MACRO_DECLARATION = Pattern.compile("(\\$[a-zA-Z0-9]+<-(?=.))");
    private static final Pattern MACRO = Pattern.compile("(\\$[a-zA-Z0-9]+)");

    //Gotta handle macros and some primitive imports first
    private static String prescreen(String xmfString) throws InvalidMacroDeclarationException {
        Map<String, String> macros = new HashMap<>();
        int lineCounter = 0;
        for (String line : xmfString.split("\n+")) {
            Matcher m = MACRO_DECLARATION.matcher(line);
            if (m.matches()) {
                if (m.start() != 0) {
                    throw new InvalidMacroDeclarationException("Macro Declarations expected to start at the beginning " +
                            "of a line! (offending line: \"" + lineCounter + "\")");
                }
                String block = readBlock(xmfString, );
            } else {
                //TODO check for imports to inherit macros or for actual macros for replacements
            }
            lineCounter++;
        }
    }

    private static String readBlock(String input, int startIndex) {

    }

    public static List<? extends AbstractToken> tokenize(String xmfString) throws XMFException {
        //TODO
    }
}
