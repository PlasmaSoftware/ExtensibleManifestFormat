package plasma.xmf.verbs;

import com.austinv11.servicer.WireService;
import plasma.xmf.ManifestContext;

@WireService(Verb.class)
public class AuthorPropertyVerb extends StringPropertyVerb {

    private final static String KEY = "__internal_author";

    @Override
    public String key() {
        return KEY;
    }

    public static String extractAuthor(ManifestContext context) {
        return PropertyVerb.getProperty(context, KEY, String.class);
    }
}
