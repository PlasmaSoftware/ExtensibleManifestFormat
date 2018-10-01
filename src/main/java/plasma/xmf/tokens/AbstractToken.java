package plasma.xmf.tokens;

public abstract class AbstractToken {

    private final String rawContent;

    protected AbstractToken(String rawContent) {
        this.rawContent = rawContent;
    }

    public String getRawContent() {
        return rawContent;
    }

    @Override
    public String toString() {
        return getRawContent();
    }
}
