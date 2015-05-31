package org.eclipse.tracecompass.lttng2.kernel.core.tests.analysis.container;

import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.tracecompass.internal.lttng2.kernel.core.analysis.container.ContainerAttributes;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystem;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.tmf.core.signal.TmfTraceOpenedSignal;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.core.trace.TmfTrace;
import org.junit.Before;
import org.junit.Test;

/**
 * This is an integration tests for the container analysis module.
 *
 * @author Sebastien Lorrain
 * @author Francis Jolivet
 *
 */
public class ContainerAnalysisTest {

    private ContainerAnalysisTestModule analysisUnderTest;


//    @BeforeClass
//    public static void setUpBeforeClass() throws Exception {
//
//    }

//    @AfterClass
//    public static void tearDownAfterClass() throws Exception {
//    }
//
    /**
     * Setup a new analysis module for every test traces
     * @throws Exception
     */
    @Before
    public void setUp() {
        analysisUnderTest = new ContainerAnalysisTestModule();
        analysisUnderTest = checkNotNull(analysisUnderTest);
    }

//    @After
//    public void tearDown() {
//    }

    /** Trace 0 : No containers at all, trace without the expected field in the trace
     *           Event SCHED_PROCESS_FORK without
     *           LttngEventLayout.fieldChildNSInum()
     *           LttngEventLayout.fieldParentNSInum()
     *           Event STATEDUMP_PROCESS without
     *           LttngEventLayout.fieldNSInum()
     */
    @Test
    public void testTrace0() {
        ITmfTrace trace0 = (new ContainerTrace("container/trace0.xml")).getTrace();
        ITmfStateSystem ss = openTraceAndRun(trace0, analysisUnderTest);

        try {
            // Should always be created
            int containerRoot = ss.getQuarkAbsolute(ContainerAttributes.ROOT);
            assertTrue(containerRoot == 0);

            // Should have
            int count = ss.getNbAttributes();
            assertTrue(count == 4);

            ArrayList<String> expectedFields = new ArrayList<>();
            expectedFields.add(ContainerAttributes.ROOT);
            expectedFields.add(ContainerAttributes.CONTAINER_CPU);
            expectedFields.add("0"); //CPU number
            expectedFields.add(ContainerAttributes.RUNNING_TID);
            //Test expected fields
            for(int i = 0; i < count; ++i) {
                assertTrue(ss.getAttributeName(i).compareTo(expectedFields.get(i)) == 0);
            }
        } catch (AttributeNotFoundException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Trace 1 : LTTng statedump, already running containers with nested containers
     */
    @Test
    public void testTrace1() {
        ITmfTrace trace1 = (new ContainerTrace("container/trace1.xml")).getTrace();
        ITmfStateSystem ss = openTraceAndRun(trace1, analysisUnderTest);

        try {
            int count = ss.getNbAttributes();
            // Should have
            assertTrue(count == 34);

            // Should always be created
            int containerRootQuark = ss.getQuarkAbsolute(ContainerAttributes.ROOT);
            assertTrue(containerRootQuark == 0);

            // Check first nested section
            int quarkNested1 = ss.getQuarkRelative(containerRootQuark, ContainerAttributes.CONTAINERS_SECTION);
            assertTrue(quarkNested1 != -1);

            ArrayList<String> expectedFields = new ArrayList<>();
            expectedFields.add(ContainerAttributes.CONTAINER_CPU);
            expectedFields.add("0"); //CPU number
            expectedFields.add(ContainerAttributes.RUNNING_TID);
            expectedFields.add(ContainerAttributes.CONTAINER_INFO);
            expectedFields.add(ContainerAttributes.PPID);
            expectedFields.add(ContainerAttributes.CONTAINER_INODE);
            expectedFields.add(ContainerAttributes.HOSTNAME);
            expectedFields.add(ContainerAttributes.CONTAINERS_SECTION);
            expectedFields.add(ContainerAttributes.CONTAINER_ID_PREFIX + "0");
            expectedFields.add(ContainerAttributes.CONTAINER_INFO);
            expectedFields.add(ContainerAttributes.PPID);
            expectedFields.add(ContainerAttributes.CONTAINER_INODE);
            expectedFields.add(ContainerAttributes.HOSTNAME);
            expectedFields.add(ContainerAttributes.CONTAINER_TASKS);
            expectedFields.add("1"); //number of tasks
            expectedFields.add(ContainerAttributes.REAL_TID);
            expectedFields.add("2"); //number of tid
            expectedFields.add(ContainerAttributes.REAL_TID);
            expectedFields.add(ContainerAttributes.CONTAINER_CPU);
            expectedFields.add("0"); //CPU number
            expectedFields.add(ContainerAttributes.RUNNING_TID);
            expectedFields.add(ContainerAttributes.CONTAINERS_SECTION);
            expectedFields.add(ContainerAttributes.CONTAINER_ID_PREFIX + "1");
            expectedFields.add(ContainerAttributes.CONTAINER_INFO);
            expectedFields.add(ContainerAttributes.PPID);
            expectedFields.add(ContainerAttributes.CONTAINER_INODE);
            expectedFields.add(ContainerAttributes.HOSTNAME);
            expectedFields.add(ContainerAttributes.CONTAINER_TASKS);
            expectedFields.add("1"); //number of tasks
            expectedFields.add(ContainerAttributes.REAL_TID);
            expectedFields.add(ContainerAttributes.CONTAINER_CPU);
            expectedFields.add("0"); //CPU number
            expectedFields.add(ContainerAttributes.RUNNING_TID);

            List<Integer> attributes = ss.getSubAttributes(containerRootQuark, true);
            testAttributesMatching(expectedFields, attributes, ss);
        } catch (AttributeNotFoundException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Trace 2 : LTTng statedump and event SCHED_PROCESS_FORK, containers created during the tracing (containers with nested containers)
     */
    //@Test
    public void testTrace2() {
        ITmfTrace trace2 = (new ContainerTrace("container/trace2.xml")).getTrace();
        ITmfStateSystem ss = openTraceAndRun(trace2, analysisUnderTest);

        try {
            int count = ss.getNbAttributes();
            // Should have
            assertTrue(count == 79);

            // Should always be created
            int containerRootQuark = ss.getQuarkAbsolute(ContainerAttributes.ROOT);
            assertTrue(containerRootQuark == 0);

            ArrayList<String> expectedFields = new ArrayList<>();
            expectedFields.add(ContainerAttributes.CONTAINER_CPU);
            expectedFields.add("0"); //CPU number
            expectedFields.add(ContainerAttributes.RUNNING_TID);
            expectedFields.add(ContainerAttributes.CONTAINER_INFO);
            expectedFields.add(ContainerAttributes.PPID);
            expectedFields.add(ContainerAttributes.CONTAINER_INODE);
            expectedFields.add(ContainerAttributes.HOSTNAME);
            /* Container 0 */
            expectedFields.add(ContainerAttributes.CONTAINERS_SECTION);
            expectedFields.add(ContainerAttributes.CONTAINER_ID_PREFIX + "0");
            expectedFields.add(ContainerAttributes.CONTAINER_INFO);
            expectedFields.add(ContainerAttributes.PPID);
            expectedFields.add(ContainerAttributes.CONTAINER_INODE);
            expectedFields.add(ContainerAttributes.HOSTNAME);
            expectedFields.add(ContainerAttributes.CONTAINER_TASKS);
            expectedFields.add("1"); //number of tasks
            expectedFields.add(ContainerAttributes.REAL_TID);
            expectedFields.add("2"); //number of tid
            expectedFields.add(ContainerAttributes.REAL_TID);
            expectedFields.add(ContainerAttributes.CONTAINER_CPU);
            expectedFields.add("0"); //CPU number
            expectedFields.add(ContainerAttributes.RUNNING_TID);
            /* Nested Containers */
            expectedFields.add(ContainerAttributes.CONTAINERS_SECTION);
            /* Container 1 */
            expectedFields.add(ContainerAttributes.CONTAINER_ID_PREFIX + "1");
            expectedFields.add(ContainerAttributes.CONTAINER_INFO);
            expectedFields.add(ContainerAttributes.PPID);
            expectedFields.add(ContainerAttributes.CONTAINER_INODE);
            expectedFields.add(ContainerAttributes.HOSTNAME);
            expectedFields.add(ContainerAttributes.CONTAINER_TASKS);
            expectedFields.add("1"); //number of tasks
            expectedFields.add(ContainerAttributes.REAL_TID);
            expectedFields.add(ContainerAttributes.CONTAINER_CPU);
            expectedFields.add("0"); //CPU number
            expectedFields.add(ContainerAttributes.RUNNING_TID);
            expectedFields.add(ContainerAttributes.CONTAINERS_SECTION);
            expectedFields.add(ContainerAttributes.CONTAINER_ID_PREFIX + "2");
            expectedFields.add(ContainerAttributes.CONTAINER_INFO);
            expectedFields.add(ContainerAttributes.PPID);
            expectedFields.add(ContainerAttributes.CONTAINER_INODE);
            expectedFields.add(ContainerAttributes.HOSTNAME);
            expectedFields.add(ContainerAttributes.CONTAINER_TASKS);
            expectedFields.add("1"); //number of tasks
            expectedFields.add(ContainerAttributes.REAL_TID);
            expectedFields.add(ContainerAttributes.CONTAINER_CPU);
            expectedFields.add("0"); //CPU number
            /* Container 2 */
            expectedFields.add(ContainerAttributes.CONTAINER_ID_PREFIX + "3");
            expectedFields.add(ContainerAttributes.CONTAINER_INFO);
            expectedFields.add(ContainerAttributes.PPID);
            expectedFields.add(ContainerAttributes.CONTAINER_INODE);
            expectedFields.add(ContainerAttributes.HOSTNAME);
            expectedFields.add(ContainerAttributes.CONTAINER_TASKS);
            expectedFields.add("1"); //number of tasks
            expectedFields.add(ContainerAttributes.REAL_TID);
            expectedFields.add(ContainerAttributes.CONTAINER_CPU);
            expectedFields.add("0"); //CPU number
            /* Nested Containers */
            expectedFields.add(ContainerAttributes.CONTAINERS_SECTION);
            /* Container 4 */
            expectedFields.add(ContainerAttributes.CONTAINER_ID_PREFIX + "4");
            expectedFields.add(ContainerAttributes.CONTAINER_INFO);
            expectedFields.add(ContainerAttributes.PPID);
            expectedFields.add(ContainerAttributes.CONTAINER_INODE);
            expectedFields.add(ContainerAttributes.HOSTNAME);
            expectedFields.add(ContainerAttributes.CONTAINER_TASKS);
            expectedFields.add("1"); //number of tasks
            expectedFields.add(ContainerAttributes.REAL_TID);
            expectedFields.add("2");
            expectedFields.add(ContainerAttributes.REAL_TID);
            expectedFields.add(ContainerAttributes.CONTAINER_CPU);
            expectedFields.add("0"); //CPU number
            /* Nested Containers */
            expectedFields.add(ContainerAttributes.CONTAINERS_SECTION);
            /* Container 5 */
            expectedFields.add(ContainerAttributes.CONTAINER_ID_PREFIX + "5");
            expectedFields.add(ContainerAttributes.CONTAINER_INFO);
            expectedFields.add(ContainerAttributes.PPID);
            expectedFields.add(ContainerAttributes.CONTAINER_INODE);
            expectedFields.add(ContainerAttributes.HOSTNAME);
            expectedFields.add(ContainerAttributes.CONTAINER_TASKS);
            expectedFields.add("1"); //number of tasks
            expectedFields.add(ContainerAttributes.REAL_TID);
            expectedFields.add(ContainerAttributes.CONTAINER_CPU);
            expectedFields.add("0"); //CPU number

            List<Integer> attributes = ss.getSubAttributes(containerRootQuark, true);
            testAttributesMatching(expectedFields, attributes, ss);
        } catch (AttributeNotFoundException e) {
            fail(e.getMessage());
        }

    }

    /**
     * Trace 3 : Unordered statedump
     * */
    @Test
    public void testTrace3() {
        ITmfTrace trace3 = (new ContainerTrace("container/trace3.xml")).getTrace();
        ITmfStateSystem ss = openTraceAndRun(trace3, analysisUnderTest);

        try {
            int count = ss.getNbAttributes();
            // Should have
            assertTrue(count == 32);

            // Should always be created
            int containerRootQuark = ss.getQuarkAbsolute(ContainerAttributes.ROOT);
            assertTrue(containerRootQuark == 0);

            // Check first nested section
            int quarkNested1 = ss.getQuarkRelative(containerRootQuark, ContainerAttributes.CONTAINERS_SECTION);
            assertTrue(quarkNested1 != -1);

            ArrayList<String> expectedFields = new ArrayList<>();
            expectedFields.add(ContainerAttributes.CONTAINER_CPU);
            expectedFields.add("0"); //CPU number
            expectedFields.add(ContainerAttributes.RUNNING_TID);
            expectedFields.add(ContainerAttributes.CONTAINER_INFO);
            expectedFields.add(ContainerAttributes.PPID);
            expectedFields.add(ContainerAttributes.CONTAINER_INODE);
            expectedFields.add(ContainerAttributes.HOSTNAME);
            expectedFields.add(ContainerAttributes.CONTAINERS_SECTION);
            expectedFields.add(ContainerAttributes.CONTAINER_ID_PREFIX + "0");
            expectedFields.add(ContainerAttributes.CONTAINER_INFO);
            expectedFields.add(ContainerAttributes.PPID);
            expectedFields.add(ContainerAttributes.CONTAINER_INODE);
            expectedFields.add(ContainerAttributes.HOSTNAME);
            expectedFields.add(ContainerAttributes.CONTAINER_TASKS);
            expectedFields.add("2"); //number of tasks
            expectedFields.add(ContainerAttributes.REAL_TID);
            expectedFields.add(ContainerAttributes.CONTAINER_CPU);
            expectedFields.add("0"); //CPU number
            expectedFields.add(ContainerAttributes.RUNNING_TID);
            expectedFields.add(ContainerAttributes.CONTAINERS_SECTION);
            expectedFields.add(ContainerAttributes.CONTAINER_ID_PREFIX + "1");
            expectedFields.add(ContainerAttributes.CONTAINER_INFO);
            expectedFields.add(ContainerAttributes.PPID);
            expectedFields.add(ContainerAttributes.CONTAINER_INODE);
            expectedFields.add(ContainerAttributes.HOSTNAME);
            expectedFields.add(ContainerAttributes.CONTAINER_TASKS);
            expectedFields.add("1"); //number of tasks
            expectedFields.add(ContainerAttributes.REAL_TID);
            expectedFields.add(ContainerAttributes.CONTAINER_CPU);
            expectedFields.add("0"); //CPU number
            expectedFields.add(ContainerAttributes.RUNNING_TID);

            List<Integer> attributes = ss.getSubAttributes(containerRootQuark, true);
            testAttributesMatching(expectedFields, attributes, ss);
        } catch (AttributeNotFoundException e) {
            fail(e.getMessage());
        }

    }


    private static void testAttributesMatching(ArrayList<String> expectedFields, List<Integer> attributes, ITmfStateSystem ss) {
        int index = 0;
        for(Integer i : attributes) {
            String s = ss.getAttributeName(i);
            /* Traces can be processed in multiple order so the container ID must not agree */
            if(expectedFields.get(index).compareTo(ContainerAttributes.CONTAINER_ID_PREFIX) == 0) {
                assertTrue(s.startsWith(expectedFields.get(index++)));
            }
            else {
                assertTrue(s.compareTo(expectedFields.get(index++)) == 0);
            }
        }
    }

    private static ITmfStateSystem openTraceAndRun(ITmfTrace trace, ContainerAnalysisTestModule analysisModule) {
        ((TmfTrace) trace).traceOpened(new TmfTraceOpenedSignal(analysisModule, trace, null));

        try {
        assertTrue(analysisModule.setTrace(trace));
        } catch (Exception e){
            fail("Could not assign trace " + trace.getPath() + " properly");
            e.printStackTrace();
        }

        analysisModule.schedule();
        if (!analysisModule.waitForCompletion()) {
            fail("Module did not complete properly" + trace.getPath());
        }

        return analysisModule.getStateSystem();
    }
}
