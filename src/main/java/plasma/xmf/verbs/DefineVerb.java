package plasma.xmf.verbs;

import com.austinv11.servicer.WireService;
import plasma.xmf.ManifestContext;
import plasma.xmf.exceptions.InvalidVerbArgumentsException;

@WireService(Verb.class)
public class DefineVerb implements Verb {

    @Override
    public ManifestContext invoke(ManifestContext context, String[] args) {
        if (args.length != 2)
            throw new InvalidVerbArgumentsException("DEFINE requires 2 arguments!");
        if (args[0].equalsIgnoreCase("import") || args[0].equalsIgnoreCase("define"))
            throw new InvalidVerbArgumentsException("Cannot override the DEFINE and IMPORT verbs!");

        context.setVerb(args[0].toLowerCase(), args[1]);
        return context;
    }
}
