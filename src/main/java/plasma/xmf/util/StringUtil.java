package plasma.xmf.util;

public final class StringUtil {

    public static int countChar(String str, char c) {
        int count = 0;
        int currIndex = 0;
        while (currIndex < str.length() && currIndex != -1) {
            currIndex = str.indexOf(c, currIndex);
            if (currIndex != -1) {
                count++;
                currIndex++;
            }
        }
        return count;
    }

    public static String insert(String str, String replacement, int startIndex, int endIndex) {
        StringBuilder sb = new StringBuilder();
        char[] buf = str.toCharArray();
        sb.append(buf, 0, startIndex).append(replacement);
        sb.append(buf, endIndex - startIndex, buf.length - (endIndex - startIndex));
        return sb.toString();
    }
}
