package plasma.xmf.actions;

import plasma.xmf.ExecutionStep;
import plasma.xmf.ManifestContext;

/**
 * @see plasma.xmf.actions.DefaultXMFHandler
 */
@FunctionalInterface
public interface XMFHandler {

    default void introduce(ManifestContext context) {}

    void nextStep(ManifestContext context, ExecutionStep currStep);

    default void exit(ManifestContext context) {}
}
