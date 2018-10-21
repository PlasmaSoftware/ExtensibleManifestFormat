package plasma.xmf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class XMF {

    private static final List<ExecutionStep> BASE;

    private final ManifestContext context;
    private final List<ExecutionStep> toExecute;

    static {
        List<ExecutionStep> tBASE;
        try {
            tBASE = XMFParser.handleXmf(ImportResolver.findXMF("classpath:base.xmf").get());
        } catch (Exception e) {
            tBASE = Collections.emptyList();
            System.err.println("[XMF Parser] WARNING: Cannot load base.xmf, this library will continue to function but bugs are likely to occur!");
            e.printStackTrace();
        }
        BASE = tBASE;
    }

    private XMF(ManifestContext context, List<ExecutionStep> toExecute) {
        this.context = context;
        this.toExecute = Collections.unmodifiableList(toExecute);
    }

    public ManifestContext getContext() {
        return context;
    }

    public List<ExecutionStep> getExecutionSteps() {
        return toExecute;
    }

    public static XMF fromString(String xmf, ManifestContext baseContext, boolean importBaseXmf) {
        List<ExecutionStep> steps = new ArrayList<>(importBaseXmf ? BASE : Collections.emptyList());
        steps.addAll(XMFParser.handleXmf(xmf));
        return new XMF(baseContext, steps);
    }

    public static XMF fromString(String xmf, ManifestContext baseContext) {
        return fromString(xmf, baseContext, true);
    }

    public static XMF fromString(String xmf) {
        return fromString(xmf, new ManifestContext());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("# Auto-Generated from pre-processed XMF\n");
        for (ExecutionStep step : getExecutionSteps()) {
            builder.append(step.toString()).append("\n");
        }
        return builder.toString();
    }
}
