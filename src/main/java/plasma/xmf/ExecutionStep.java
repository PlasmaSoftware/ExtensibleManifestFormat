package plasma.xmf;

public final class ExecutionStep {

    private final String binding;
    private final Block block;
    private final boolean isVerbCall;

    public ExecutionStep(String binding, Block block, boolean isVerbCall) {
        this.binding = binding;
        this.block = block;
        this.isVerbCall = isVerbCall;
    }

    public String getBinding() {
        return binding;
    }

    public Block getBlock() {
        return block;
    }

    public boolean hasBlock() {
        return this.block != Block.NULL_BLOCK;
    }

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

    public static final class Block {

        public static final Block NULL_BLOCK = new Block(true, "");

        private final boolean isImplicit;
        private final String rawContent;

        public Block(boolean isImplicit, String rawContent) {
            this.isImplicit = isImplicit;
            this.rawContent = rawContent;
        }

        public boolean isImplicit() {
            return isImplicit;
        }

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
