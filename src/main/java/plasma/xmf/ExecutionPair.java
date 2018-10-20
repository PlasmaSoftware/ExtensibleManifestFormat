package plasma.xmf;

public final class ExecutionPair {

    private final String verb;
    private final String block;

    public ExecutionPair(String verb, String block) {
        this.verb = verb;
        this.block = block;
    }

    public String getVerb() {
        return verb;
    }

    public String getBlock() {
        return block;
    }
}
