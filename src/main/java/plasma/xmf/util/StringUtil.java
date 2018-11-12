package plasma.xmf.util;

public final class StringUtil {

    public static int countChar(String str, char c) {
        int count = 0;
        int currIndex = 0;
        while (currIndex < str.length() && currIndex != -1) {
            currIndex = str.indexOf(c, currIndex);
            if (currIndex != -1)
                count++;
        }
        return count;
    }
}
