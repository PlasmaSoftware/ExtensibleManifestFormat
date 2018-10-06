package plasma.xmf;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ManifestContext {

    private final Map<String, String> macros = new HashMap<>();
    private final Map<String, String> verbs = new HashMap<>();

    public ManifestContext() {
        fillSpecialMacros();
    }

    private void fillSpecialMacros() {

    }

    public String getMacro(String key) {
        return macros.getOrDefault(key, null);
    }

    public Set<String> getAvailableMacros() {
        return macros.keySet();
    }

    public void setMacro(String key, String value) {
        macros.put(key, value);
    }

    public String getVerb(String verb) {
        return verbs.getOrDefault(verb, null);
    }

    public Set<String> getAvailableVerbs() {
        return verbs.keySet();
    }

    public void setVerb(String key, String value) {
        verbs.put(key, value);
    }
}
