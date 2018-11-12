package plasma.xmf.actions;

import plasma.xmf.ExecutionStep;
import plasma.xmf.ManifestContext;
import plasma.xmf.XMF;
import plasma.xmf.exceptions.XMFException;

public class XMFExecutionEnvironment {

    private final XMFHandlerProvider provider;

    public XMFExecutionEnvironment() {
        this(DefaultXMFHandler.buildDefaultProvider());
    }

    public XMFExecutionEnvironment(XMFHandlerProvider provider) {
        this.provider = provider;
    }

    public XMFHandlerProvider getProvider() {
        return provider;
    }

    public ManifestContext execute(XMF xmf) throws XMFException {
        XMFHandler h = provider.provide();
        ManifestContext context = new ManifestContext(xmf.getContext());
        context = h.introduce(context);
        for (ExecutionStep step : xmf.getExecutionSteps())
            context = h.nextStep(context, step);
        context = h.exit(context);
        return context;
    }

    public ManifestContext execute(String xmf, ManifestContext baseContext, boolean importBaseXmf) throws XMFException {
        return execute(XMF.fromString(xmf, baseContext, importBaseXmf));
    }

    public ManifestContext execute(String xmf, ManifestContext baseContext) throws XMFException {
        return execute(XMF.fromString(xmf, baseContext));
    }

    public ManifestContext execute(String xmf) throws XMFException {
        return execute(XMF.fromString(xmf));
    }
}
