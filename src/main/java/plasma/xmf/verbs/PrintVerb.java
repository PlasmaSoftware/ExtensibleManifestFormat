package plasma.xmf.verbs;

import com.austinv11.servicer.WireService;
import plasma.xmf.ManifestContext;
import plasma.xmf.exceptions.InvalidVerbArgumentsException;

@WireService(Verb.class)
public class PrintVerb implements Verb {

    @Override
    public ManifestContext invoke(ManifestContext context, String[] args) throws InvalidVerbArgumentsException {
        System.out.println(String.join(" ", args));
        return context;
    }
}
