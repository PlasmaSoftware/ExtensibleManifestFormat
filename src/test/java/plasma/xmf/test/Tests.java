package plasma.xmf.test;

import org.junit.Test;
import plasma.xmf.ManifestContext;
import plasma.xmf.XMF;
import plasma.xmf.actions.XMFExecutionEnvironment;
import plasma.xmf.verbs.AuthorPropertyVerb;
import plasma.xmf.verbs.DescriptionPropertyVerb;
import plasma.xmf.verbs.NamePropertyVerb;
import plasma.xmf.verbs.VersionPropertyVerb;

import static org.junit.Assert.*;

public class Tests {

    @Test
    public void testBaseParse() {
        XMF xmf = XMF.base();

        assertEquals(1, xmf.getExecutionSteps().size());

        ManifestContext context = new XMFExecutionEnvironment().execute(xmf);

        assertEquals("1.0", context.getMacro("xmf_version"));
    }

    @Test
    public void testPropertiesParse() {
        String str = "IMPORT classpath:/xmf/properties.xmf";

        XMF xmf = XMF.fromString(str);

        assertEquals(9, xmf.getExecutionSteps().size());

        ManifestContext context = new XMFExecutionEnvironment().execute(xmf);

        String author = AuthorPropertyVerb.extractAuthor(context);
        String desc = DescriptionPropertyVerb.extractDescription(context);
        String name = NamePropertyVerb.extractName(context);
        String version = VersionPropertyVerb.extractVersion(context);

        assertEquals("Unknown Author", author);
        assertEquals("N/A", desc);
        assertEquals("Unknown Name", name);
        assertEquals(context.getMacro("xmf_version"), version);
    }

    @Test
    public void testUtilitiesParse() {
        String str = "IMPORT classpath:/xmf/utils.xmf\nECHO hello world\nENV test_env hello world2\nGET test_env\nPRINT $RESULT";

        XMF xmf = XMF.fromString(str);

        assertEquals(10, xmf.getExecutionSteps().size());

        ManifestContext context = new XMFExecutionEnvironment().execute(xmf);

        assertEquals(6, context.getAvailableVerbs().size());
        assertEquals(context.getMacro("result"), System.getProperty("test_env"));
    }
}
