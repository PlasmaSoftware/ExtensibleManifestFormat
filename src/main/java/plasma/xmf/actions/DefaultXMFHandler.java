package plasma.xmf.actions;

import plasma.xmf.ExecutionStep;
import plasma.xmf.ManifestContext;
import plasma.xmf.exceptions.InvalidMacroDeclarationException;
import plasma.xmf.exceptions.InvalidVerbException;
import plasma.xmf.util.ArgumentParser;
import plasma.xmf.verbs.Verb;
import plasma.xmf.verbs.Verbs;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class DefaultXMFHandler implements XMFHandler {

    public static XMFHandlerProvider buildDefaultProvider() {
        return buildDefaultProvider(false);
    }

    public static XMFHandlerProvider buildDefaultProvider(boolean restrictDefinitions) {
        Provider p = new Provider();
        p.addObserver(new MagicMacroLock());
        if (restrictDefinitions)
            p.addObserver(new DefineLock());
        return p;
    }

    private final Set<XMFObserver> observers;

    private DefaultXMFHandler(Set<XMFObserver> observers) {
        this.observers = observers;
    }

    @Override
    public void introduce(ManifestContext context) {
        observers.forEach(o -> o.observePreExecution(context));
    }

    @Override
    public void nextStep(ManifestContext context, ExecutionStep currStep) {
        if (currStep.isVerbCall()) {
            String verb = currStep.getBinding();

            if (!context.hasVerb(verb))
                throw new InvalidVerbException("Verb '" + verb + "' does not exist!");

            String block = currStep.hasBlock() ? currStep.getBlock().getRawContent() : null;
            String[] args = block == null ? new String[0] : ArgumentParser.parse(currStep.getBlock()).getTokens();

            String normalizedVerb = context.getVerb(verb);

            Verb v = Verbs.getVerb(normalizedVerb);

            if (v == null)
                throw new InvalidVerbException("Verb '" + verb + "' is incorrectly mapped! (mapping=" + normalizedVerb + ")");

            if (!observers.stream().allMatch(o -> o.observePreVerb(context, normalizedVerb, args)))
                return;

            v.invoke(context, args);

            observers.forEach(o -> o.observePostVerb(context, normalizedVerb, args));
        } else {
            String macro = currStep.getBinding();

            if (!currStep.hasBlock())
                throw new InvalidMacroDeclarationException("Macros cannot have non-existent blocks!");

            String block = currStep.getBlock().getRawContent();

            if (!observers.stream().allMatch(o -> o.observePreMacro(context, macro, block)))
                return;

            context.setMacro(macro, block);

            observers.forEach(o -> o.observePostMacro(context, macro, block));
        }
    }

    @Override
    public void exit(ManifestContext context) {
        observers.forEach(o -> o.observePostExecution(context));
    }

    public interface XMFObserver {

        default void observePreExecution(ManifestContext context) {}

        default boolean observePreMacro(ManifestContext context, String macro, String content) {
            return true;
        }

        default void observePostMacro(ManifestContext context, String macro, String content) {}

        default boolean observePreVerb(ManifestContext context, String verb, String[] args) {
            return true;
        }

        default void observePostVerb(ManifestContext context, String verb, String[] args) {}

        default void observePostExecution(ManifestContext context) {}
    }

    public static final class Provider implements XMFHandlerProvider {

        private final Set<XMFObserver> observers = new LinkedHashSet<>();

        public void addObserver(XMFObserver observer) {
            this.observers.add(observer);
        }

        @Override
        public XMFHandler provide() {
            return new DefaultXMFHandler(observers);
        }
    }

    private static final class MagicMacroLock implements XMFObserver {

        private static final Set<String> MAGIC_MACROS = Collections.singleton("cwd");

        @Override
        public boolean observePreMacro(ManifestContext context, String macro, String content) {
            return !MAGIC_MACROS.contains(macro.toLowerCase());
        }
    }

    private static final class DefineLock implements XMFObserver {

        @Override
        public boolean observePreVerb(ManifestContext context, String verb, String[] args) {
            return !verb.equalsIgnoreCase("plasma.xmf.verbs.DefineVerb");
        }
    }
}
