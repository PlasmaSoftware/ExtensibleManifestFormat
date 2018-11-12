package plasma.xmf.verbs;

import plasma.xmf.ManifestContext;
import plasma.xmf.exceptions.InvalidVerbArgumentsException;

public abstract class PropertyVerb<T> implements Verb {

    public String key() {
        return this.namespace();
    }

    public abstract T convert(String s);

    @Override
    public ManifestContext invoke(ManifestContext context, String[] clause) {
        if (clause.length < 1)
            throw new InvalidVerbArgumentsException("Property verbs require an argument!");

        String val = String.join(" ", clause);

        context.setData(key(), convert(val));
        return context;
    }

    public static <T> T getProperty(ManifestContext context, String prop, Class<T> type) {
        if (!context.hasData(prop))
            return null;

        return context.getData(prop, type);
    }
}
