package plasma.xmf.verbs;

import com.austinv11.servicer.WireService;
import plasma.xmf.ManifestContext;
import plasma.xmf.exceptions.InvalidVerbArgumentsException;

@WireService(Verb.class)
public class EnvVerb implements Verb {

    @Override
    public ManifestContext invoke(ManifestContext context, String[] args) throws InvalidVerbArgumentsException {
        if (args.length < 2)
            throw new InvalidVerbArgumentsException("ENV requires exactly 2 arguments!");

        String[] sub = new String[args.length-1];
        System.arraycopy(args, 1, sub, 0, sub.length);
        System.setProperty(args[0], String.join(" ", sub));

        return context;
    }
}
