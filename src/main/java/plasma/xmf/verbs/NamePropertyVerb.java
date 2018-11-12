package plasma.xmf.verbs;

import com.austinv11.servicer.WireService;
import plasma.xmf.ManifestContext;

@WireService(Verb.class)
public class NamePropertyVerb extends StringPropertyVerb {

    private final static String KEY = "__internal_name";

    @Override
    public String key() {
        return KEY;
    }

    public static String extractName(ManifestContext context) {
        return PropertyVerb.getProperty(context, KEY, String.class);
    }
}
