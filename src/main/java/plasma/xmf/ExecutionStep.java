package plasma.xmf;

/**
 * This represents a single step in the XMF.
 */
public final class ExecutionStep {

    private final String binding;
    private final Block block;
    private final boolean isVerbCall;

    /**
     * @param binding The verb or macro this step is referring to.
     * @param block The block (args) for the binding.
     * @param isVerbCall True if this is a verb invocation, false if a macro declaration.
     */
    public ExecutionStep(String binding, Block block, boolean isVerbCall) {
        this.binding = binding;
        this.block = block;
        this.isVerbCall = isVerbCall;
    }

    /**
     * Gets the binding this step is referring to.
     *
     * @return The binding (the name of the macro or verb).
     */
    public String getBinding() {
        return binding;
    }

    /**
     * Gets the block this step uses as its arguments.
     *
     * @return The step's block.
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Checks if this step has a valid block associated with it.
     *
     * @return True if so, false if otherwise.
     */
    public boolean hasBlock() {
        return this.block != Block.NULL_BLOCK;
    }

    /**
     * Checks if this step is a verb call or not.
     *
     * @return True if a verb call, false if a macro declaration.
     */
    public boolean isVerbCall() {
        return isVerbCall;
    }

    @Override
    public String toString() {
        return (isVerbCall ? "" : "$") +
                getBinding() +
                (isVerbCall ? " " : "<-") +
                getBlock().toString();
    }

    /**
     * This represents a nested block in a XMF file.
     */
    public static final class Block {

        /**
         * Placeholder for non-existent blocks.
         */
        public static final Block NULL_BLOCK = new Block(true, "");

        private final boolean isImplicit;
        private final String rawContent;

        /**
         * @param isImplicit True if this block was not declared with explicit quotations.
         * @param rawContent The content of the block.
         */
        public Block(boolean isImplicit, String rawContent) {
            this.isImplicit = isImplicit;
            this.rawContent = rawContent;
        }

        /**
         * Checks if this block was declared without explicit quotations.
         *
         * @return True if so, false if otherwise.
         */
        public boolean isImplicit() {
            return isImplicit;
        }

        /**
         * Gets the content of this block.
         *
         * @return The block's contents.
         */
        public String getRawContent() {
            return rawContent;
        }

        @Override
        public String toString() {
            if (isImplicit()) {
                return getRawContent().replaceAll("\n", "\t\n");
            } else {
                return "\"" + getRawContent() + "\"";
            }
        }
    }
}
