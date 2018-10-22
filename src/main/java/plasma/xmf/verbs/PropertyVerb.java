package plasma.xmf.verbs;

import plasma.xmf.ManifestContext;

public abstract class PropertyVerb<T> implements Verb {

    public String key() {
        return this.namespace();
    }

    public abstract T convert(String s);

    @Override
    public ManifestContext invoke(ManifestContext context, String clause) {
        context.setData(key(), convert(clause));
        return context;
    }
}
