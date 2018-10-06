package plasma.xmf.verbs;

import com.austinv11.servicer.Service;
import plasma.xmf.ManifestContext;

@Service
public interface Verb {

    default String namespace() {
        return this.getClass().getCanonicalName();
    }

    EvaluationStage stage();

    default ManifestContext invoke(ManifestContext context, String clause) {
        return context;
    }

    enum EvaluationStage {
        EARLY, LATE
    }
}
