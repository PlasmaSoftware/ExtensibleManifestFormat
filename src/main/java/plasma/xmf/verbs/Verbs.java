package plasma.xmf.verbs;

import java.util.*;

public final class Verbs {

    private final static Map<String, Verb> VERB_MAPPING = Collections.synchronizedMap(new HashMap<>());

    static {
        ServiceLoader.load(Verb.class).iterator().forEachRemaining(v -> VERB_MAPPING.put(v.namespace(), v));
    }

    public static Set<String> getAvailableVerbs() {
        return VERB_MAPPING.keySet();
    }

    @SuppressWarnings("unchecked")
    public static Verb getVerb(String namespace) {
        Verb v = VERB_MAPPING.get(namespace);

        if (v != null)
            return v;

        // Fallback to java classloading as a last ditch effort
        try {
            Class<? extends Verb> vc = (Class<? extends Verb>) Class.forName(namespace);
            v = vc.newInstance();
            VERB_MAPPING.put(namespace, v);
            System.err.println("[XMF Parser] WARNING: The verb under the namespace '" + namespace + "' had to be loaded " +
                    "by a fallback handler, please report this to the verb's developer to wire the service correctly!");
            return v;
        } catch (Exception e) {
            return null;
        }
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
