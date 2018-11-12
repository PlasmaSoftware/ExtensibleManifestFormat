package plasma.xmf;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is a container for the current state for a given XMF file.
 */
public final class ManifestContext {

    private final Map<String, String> macros = new HashMap<>();
    private final Map<String, String> verbs = new HashMap<>();
    private final Map<String, Object> data = new HashMap<>();

    /**
     * Initialized with the default configuration.
     */
    public ManifestContext() {
        fillSpecialMacros(System.getProperty("user.dir"));
        fillInBuiltins();
    }

    /**
     * Initialized with the default configuration but an overridden $CWD macro.
     */
    public ManifestContext(String cwd) {
        fillSpecialMacros(cwd);
        fillInBuiltins();
    }

    /**
     * Initialized with the data from the provided parent context.
     */
    public ManifestContext(ManifestContext base) {
        this.macros.putAll(base.macros);
        this.verbs.putAll(base.verbs);
        this.data.putAll(base.data);
    }

    private void fillSpecialMacros(String cwd) {
        macros.put("cwd", cwd);
    }

    private void fillInBuiltins() {
        verbs.put("define", "plasma.xmf.verbs.DefineVerb");
    }

    /**
     * Gets the content stored by a macro.
     *
     * @param key The name of the macro (without the $)
     * @return The content or null.
     */
    public String getMacro(String key) {
        return macros.getOrDefault(key.toLowerCase(), null);
    }

    /**
     * Gets the currently available macros.
     *
     * @return The macro names.
     */
    public Set<String> getAvailableMacros() {
        return macros.keySet();
    }

    /**
     * Sets the value of a macro.
     *
     * @param key The macro name, without the dollar sign.
     * @param value The value of the macro.
     */
    public void setMacro(String key, String value) {
        macros.put(key.toLowerCase(), value);
    }

    /**
     * Checks if the provided macro is present.
     *
     * @param key The macro name, without the dollar sign.
     * @return True if the macro is available.
     */
    public boolean hasMacro(String key) {
        return macros.containsKey(key.toLowerCase());
    }

    /**
     * Gets a verb mapping.
     *
     * @param verb The verb name.
     * @return The verb mapping.
     */
    public String getVerb(String verb) {
        return verbs.getOrDefault(verb.toLowerCase(), null);
    }

    /**
     * Gets the currently available verbs.
     *
     * @return The available verbs.
     */
    public Set<String> getAvailableVerbs() {
        return verbs.keySet();
    }

    /**
     * Sets a mapping of a verb name to a namespace.
     *
     * @param key The name of the verb.
     * @param value The mapping of the verb.
     */
    public void setVerb(String key, String value) {
        verbs.put(key.toLowerCase(), value);
    }

    /**
     * Checks if the context currently has the provided verb available.
     *
     * @param key The verb to check.
     * @return True if the verb is currently available, else false.
     */
    public boolean hasVerb(String key) {
        return verbs.containsKey(key.toLowerCase());
    }

    /**
     * Gets a piece of arbitrary metadata.
     *
     * @param key The key for the data.
     * @param type The type of the metadata.
     * @return The data or null.
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(String key, Class<T> type) {
        return (T) data.getOrDefault(key, null);
    }

    /**
     * Gets all the available metadata keys.
     *
     * @return The available metadata keys.
     */
    public Set<String> getAvailableData() {
        return data.keySet();
    }

    /**
     * Assigns metadata to a key.
     *
     * @param key The key.
     * @param o The metadata.
     */
    public void setData(String key, Object o) {
        data.put(key, o);
    }

    /**
     * Checks if a piece of metadata is available.
     *
     * @param key The key for the metadata.
     * @return True if the metadata is available, false if otherwise.
     */
    public boolean hasData(String key) {
        return data.containsKey(key);
    }

    /**
     * Copies the data of this context instance to a new instance.
     *
     * @return The copied context instance.
     */
    public ManifestContext copy() {
        return new ManifestContext(this);
    }
}
