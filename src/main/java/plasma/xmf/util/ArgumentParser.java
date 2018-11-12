package plasma.xmf.util;

import plasma.xmf.ExecutionStep;
import plasma.xmf.XMFParser;

import java.util.Arrays;
import java.util.Iterator;
import java.util.StringTokenizer;

public class ArgumentParser implements Iterable<String> {

    private final String[] tokens;

    private ArgumentParser(String[] tokens) {
        this.tokens = tokens;
    }

    public static ArgumentParser parse(String content, boolean isImplicit) {
        if (!isImplicit) {
            return new ArgumentParser(new String[]{content});
        } else {
            String c = content;
            c = c.trim();
            int iStr = 0, iArr = 0;
            String[] buf = new String[StringUtil.countChar(c, ' ')+1]; //Max amount of args are the # of words
            while (iStr < c.length()) {
                String currSubstring = c.substring(iStr);
                if (XMFParser.WHITESPACE.indexOf(currSubstring.charAt(0)) == 0) {
                    iStr++;
                    continue;
                }

                if (XMFParser.BLOCK_START.indexOf(currSubstring.charAt(0)) == 0) {
                    char blockStart = currSubstring.charAt(0);
                    int blockEnd = currSubstring.indexOf(blockStart, 1);
                    if (blockEnd != -1) {
                        //This is a valid block so parse it as a block
                        buf[iArr++] = currSubstring.substring(1, blockEnd); //Cutoff the leading and trailing quotes
                        iStr += blockEnd+1;
                        continue;
                    }
                }

                StringTokenizer tokenizer = new StringTokenizer(currSubstring, XMFParser.WHITESPACE);
                buf[iArr++] = tokenizer.nextToken();
                iStr += buf[iArr-1].length();
            }
            return new ArgumentParser(iArr == buf.length ? buf : Arrays.copyOf(buf, iArr+1));
        }
    }

    public String[] getTokens() {
        return tokens;
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < tokens.length;
            }

            @Override
            public String next() {
                return tokens[i++];
            }
        };
    }
}
