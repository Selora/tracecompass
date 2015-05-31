/*******************************************************************************
 * Copyright (c) 2015 Ericsson
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Sébastien Lorrain
 *   Francis Jolivet
 ******************************************************************************/

package org.eclipse.tracecompass.lttng2.kernel.core.tests.analysis.container;

import org.eclipse.tracecompass.internal.lttng2.kernel.core.analysis.container.ContainerAnalysisModule;
import org.eclipse.tracecompass.internal.lttng2.kernel.core.analysis.container.ContainerStateProvider;
import org.eclipse.tracecompass.internal.lttng2.kernel.core.trace.layout.Lttng27EventLayout;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.tests.stubs.trace.xml.TmfXmlTraceStub;

/**
 * Class to test ContaineAnalysis through ContainerAnalysisTest
 *
 * @author Francis Jolivet
 * @author Sébastien Lorrain
 *
 */
public class ContainerAnalysisTestModule extends ContainerAnalysisModule {

    /**
     * Constructor
     */
    public ContainerAnalysisTestModule(){
        super();
    }

    @Override
    protected ITmfStateProvider createStateProvider() {
        ITmfTrace trace = getTrace();
        Lttng27EventLayout layout;

        if(!(trace instanceof TmfXmlTraceStub)){
            throw new IllegalStateException();
        }
        layout = Lttng27EventLayout.INSTANCE;
        return new ContainerStateProvider(trace, layout);
    }

}
