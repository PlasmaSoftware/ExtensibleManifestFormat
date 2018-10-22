package plasma.xmf.verbs;

import java.util.*;

public final class Verbs {

    private final static Map<String, Verb> VERB_MAPPING = new HashMap<>();

    static {
        ServiceLoader.load(Verb.class).iterator().forEachRemaining(v -> VERB_MAPPING.put(v.namespace(), v));
    }

    public static Set<String> getAvailableVerbs() {
        return VERB_MAPPING.keySet();
    }

    public static Verb getVerb(String namespace) {
        return VERB_MAPPING.get(namespace);
    }

    public static Set<String> getAvailableProperties() {
        Set<String> props = new HashSet<>();
        VERB_MAPPING.forEach((s, v) -> {
            if (v instanceof PropertyVerb)
                props.add(s);
        });
        return props;
    }
}
