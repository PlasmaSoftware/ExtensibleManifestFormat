package plasma.xmf.verbs;

public abstract class StringPropertyVerb extends PropertyVerb<String> {

    @Override
    public String convert(String s) {
        return s;
    }
}
