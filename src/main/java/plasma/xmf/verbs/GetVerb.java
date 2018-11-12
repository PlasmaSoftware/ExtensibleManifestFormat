package plasma.xmf.verbs;

import com.austinv11.servicer.WireService;
import plasma.xmf.ManifestContext;
import plasma.xmf.exceptions.InvalidVerbArgumentsException;

@WireService(Verb.class)
public class GetVerb implements Verb {

    @Override
    public ManifestContext invoke(ManifestContext context, String[] args) throws InvalidVerbArgumentsException {
        if (args.length < 1)
            throw new InvalidVerbArgumentsException("GET requires 1 argument!");

        String k = String.join(" ", args);

        String prop = System.getenv(k);

        if (prop == null)
            prop = System.getProperty(k ,"");

        ManifestContext context1 = context.copy();
        context1.setMacro("result", prop);

        return context1;
    }
}
