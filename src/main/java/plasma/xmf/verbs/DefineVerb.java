package plasma.xmf.verbs;

import com.austinv11.servicer.WireService;
import plasma.xmf.ManifestContext;
import plasma.xmf.exceptions.InvalidVerbArgumentsException;

@WireService(Verb.class)
public class DefineVerb implements Verb {

    @Override
    public ManifestContext invoke(ManifestContext context, String clause) {
        String[] split = clause.trim().split(" ");
        if (split.length != 2)
            throw new InvalidVerbArgumentsException("DEFINE requires 2 arguments!");
        if (split[0].equalsIgnoreCase("import") || split[0].equalsIgnoreCase("define"))
            throw new InvalidVerbArgumentsException("Cannot override the DEFINE and IMPORT verbs!");

        context.setVerb(split[0].toLowerCase(), split[1]);
        return context;
    }
}
