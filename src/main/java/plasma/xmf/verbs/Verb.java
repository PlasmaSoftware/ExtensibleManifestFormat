package plasma.xmf.verbs;

import com.austinv11.servicer.Service;
import plasma.xmf.ManifestContext;

@Service
public interface Verb {

    default String namespace() {
        return this.getClass().getCanonicalName();
    }

    ManifestContext invoke(ManifestContext context, String[] args);
}
