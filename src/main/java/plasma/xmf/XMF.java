package plasma.xmf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This represents a naive XMF file, meaning that it has been fully parse but its steps have not been executed at this
 * time.
 */
public final class XMF {

    private static final List<ExecutionStep> BASE;

    private final ManifestContext context;
    private final List<ExecutionStep> toExecute;

    static {
        List<ExecutionStep> tBASE;
        try {
            tBASE = XMFParser.handleXmf(ImportResolver.findXMF("classpath:/xmf/base.xmf").get());
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

    /**
     * Gets the inherited context intrinsic to this XMF object.
     *
     * @return The base xmf context.
     */
    public ManifestContext getContext() {
        return context;
    }

    /**
     * Gets the naive execution steps to this XMF file.
     *
     * @return The steps, in order.
     */
    public List<ExecutionStep> getExecutionSteps() {
        return toExecute;
    }

    /**
     * Parses an XMF string.
     *
     * @param xmf The raw XMF string to parse.
     * @param baseContext The base context to inherit from implicitly.
     * @param importBaseXmf Whether to implicitly import the most basic XMF (base.xmf).
     * @return The parsed XMF object.
     */
    public static XMF fromString(String xmf, ManifestContext baseContext, boolean importBaseXmf) {
        List<ExecutionStep> steps = new ArrayList<>(importBaseXmf ? BASE : Collections.emptyList());
        steps.addAll(XMFParser.handleXmf(xmf));
        return new XMF(baseContext, steps);
    }

    /**
     * Parses an XMF string.
     *
     * @param xmf The raw XMF string to parse.
     * @param baseContext The base context to inherit from implicitly.
     * @return The parsed XMF object.
     */
    public static XMF fromString(String xmf, ManifestContext baseContext) {
        return fromString(xmf, baseContext, true);
    }

    /**
     * Parses an XMF string.
     *
     * @param xmf The raw XMF string to parse.
     * @return The parsed XMF object.
     */
    public static XMF fromString(String xmf) {
        return fromString(xmf, new ManifestContext());
    }

    /**
     * Builds an XMF instance which only contains the default data.
     *
     * @return The most basic XMF.
     */
    public static XMF base() {
        return new XMF(new ManifestContext(), BASE);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("# Auto-Generated from a pre-processed XMF, this is not guaranteed to be accurate\n");
        for (ExecutionStep step : getExecutionSteps()) {
            builder.append(step.toString()).append("\n");
        }
        return builder.toString();
    }

    /**
     * This builder allows one to programmatically generate XMFs.
     */
    public static final class Builder {

        private ManifestContext context = new ManifestContext();
        private final List<ExecutionStep> executionSteps = new ArrayList<>();

        /**
         * Set the base context of the XMF.
         *
         * @param context The base context.
         * @return The same instance for chaining.
         */
        public Builder setContext(ManifestContext context) {
            this.context = context;
            return this;
        }

        /**
         * Adds an {@link plasma.xmf.ExecutionStep} to the XMF.
         *
         * @param step The step to add.
         * @return The same instance for chaining.
         */
        public Builder addStep(ExecutionStep step) {
            this.executionSteps.add(step);
            return this;
        }

        /**
         * Adds multiple {@link plasma.xmf.ExecutionStep}s to the XMF.
         *
         * @param steps The steps to add.
         * @return The same instance for chaining.
         */
        public Builder addSteps(Iterable<ExecutionStep> steps) {
            steps.forEach(this.executionSteps::add);
            return this;
        }

        /**
         * Sets the {@link plasma.xmf.ExecutionStep}s for the XMF.
         *
         * @param steps The steps to use.
         * @return The same instance for chaining.
         */
        public Builder setSteps(Iterable<ExecutionStep> steps) {
            this.executionSteps.clear();
            return addSteps(steps);
        }

        /**
         * Builds the XMF object.
         *
         * @return The built XMF.
         */
        public XMF build() {
            return new XMF(this.context, this.executionSteps);
        }

        /**
         * This copies the current builder.
         *
         * @return The copied builder.
         */
        public Builder copy() {
            Builder b = new Builder();
            b.setContext(this.context);
            b.executionSteps.addAll(this.executionSteps);
            return b;
        }
    }
}
