package oss.jthinker.diagrams;

import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import oss.jthinker.diagrams.DiagramSpec;
import oss.jthinker.tocmodel.DiagramType;
import oss.jthinker.widgets.JNodeSpec;
import static org.junit.Assert.*;

/**
 *
 * @author 1
 */
public class DiagramSpecTest {

    public DiagramSpecTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void nullInConstructor() {
        DiagramSpec spec = new DiagramSpec(new LinkedList<JNodeSpec>(),
                    new LinkedList<JEdgeSpec>(),
                    DiagramType.CURRENT_REALITY_TREE);
        assertNotNull(spec.legSpecs);
        spec = new DiagramSpec(null, null, new LinkedList<JLegSpec>());
        assertNotNull(spec.nodeSpecs);
        assertNotNull(spec.edgeSpecs);
    }
}