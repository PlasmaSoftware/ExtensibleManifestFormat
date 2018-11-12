package plasma.xmf.verbs;

import com.austinv11.servicer.WireService;
import plasma.xmf.ManifestContext;
import plasma.xmf.exceptions.InvalidVerbArgumentsException;

@WireService(Verb.class)
public class AliasVerb implements Verb {

    @Override
    public ManifestContext invoke(ManifestContext context, String[] args) {
        if (args.length != 2)
            throw new InvalidVerbArgumentsException("ALIAS requires exactly 2 arguments!");

        ManifestContext context1 = context.copy();
        String orig = args[0].toLowerCase();
        String n = args[1].toLowerCase();

        if (!context.hasVerb(orig))
            throw new InvalidVerbArgumentsException("The verb '" + orig + "' doesn't exist!");

        if (context.hasVerb(n))
            throw new InvalidVerbArgumentsException("The verb '" + n + "' already exists!");

        context1.setVerb(n, context1.getVerb(orig));
        return context1;
    }
}
