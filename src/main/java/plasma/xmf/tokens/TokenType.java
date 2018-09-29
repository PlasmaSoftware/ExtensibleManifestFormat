package plasma.xmf.tokens;

import java.lang.reflect.InvocationTargetException;

public enum TokenType {

    COMMENT(CommentToken.class),
    MACRO(MacroToken.class),
    MACRO_ASSIGNMENT(MacroAssignmentToken.class),
    BLOCK(BlockToken.class),
    VERB(VerbToken.class);

    private final Class<? extends AbstractToken> type;

    TokenType(Class<? extends AbstractToken> type) {this.type = type;}

    public Class<? extends AbstractToken> getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractToken> T newToken(String rawContent) {
        try {
            return (T) type.getConstructor(String.class).newInstance(rawContent);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
