package plasma.xmf.actions;

import plasma.xmf.ExecutionStep;
import plasma.xmf.ManifestContext;
import plasma.xmf.util.StringUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @see plasma.xmf.actions.DefaultXMFHandler
 */
@FunctionalInterface
public interface XMFHandler {

    Pattern MACRO_PATTERN = Pattern.compile("(?:(^|[^\\\\]))\\$[^ $<\\-]+");

    default String injectMacros(ManifestContext context, String args) {
        if (args == null)
            return null;

        Matcher m = MACRO_PATTERN.matcher(args);
        Map<int[], String> toReplace = new HashMap<>();
        while (m.find()) {
            toReplace.put(new int[]{m.start(), m.end()}, m.group().substring(1));
        }

        for (int[] indices : toReplace.keySet()) {
            String macro = toReplace.get(indices).toLowerCase();

            if (!context.hasMacro(macro))
                continue;

            args = StringUtil.insert(args, context.getMacro(macro), indices[0], indices[1]);
        }

        return args;
    }

    default ManifestContext introduce(ManifestContext context) {
        return context;
    }

    ManifestContext nextStep(ManifestContext context, ExecutionStep currStep);

    default ManifestContext exit(ManifestContext context) {
        return context;
    }
}
