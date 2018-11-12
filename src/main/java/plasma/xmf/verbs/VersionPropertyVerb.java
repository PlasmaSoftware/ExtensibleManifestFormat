package plasma.xmf.verbs;

import com.austinv11.servicer.WireService;
import plasma.xmf.ManifestContext;

@WireService(Verb.class)
public class VersionPropertyVerb extends StringPropertyVerb {

    private final static String KEY = "__internal_version";

    @Override
    public String key() {
        return KEY;
    }

    public static String extractVersion(ManifestContext context) {
        return PropertyVerb.getProperty(context, KEY, String.class);
    }
}
