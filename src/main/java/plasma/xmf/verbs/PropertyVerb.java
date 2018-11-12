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
        if (clause.length != 1)
            throw new InvalidVerbArgumentsException("Property verbs require EXACTLY 1 argument");

        context.setData(key(), convert(clause[0]));
        return context;
    }
}
