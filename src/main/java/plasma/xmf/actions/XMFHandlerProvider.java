package plasma.xmf.actions;

/**
 * @see plasma.xmf.actions.DefaultXMFHandler.Provider
 */
@FunctionalInterface
public interface XMFHandlerProvider {

    XMFHandler provide();
}
