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
 * Based on Geneviève Bastien VmTraces.java
 ******************************************************************************/

package org.eclipse.tracecompass.lttng2.kernel.core.tests.analysis.container;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.lttng2.kernel.core.tests.Activator;
import org.eclipse.tracecompass.tmf.core.event.TmfEvent;
import org.eclipse.tracecompass.tmf.core.exceptions.TmfTraceException;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.tests.stubs.trace.xml.TmfXmlTraceStub;

/**
 * List the available container test traces
 *
 * @author Francis Jolivet
 * @author Sébastien Lorrain
 *
 */

public class ContainerTrace {

    private static final @NonNull String filePath = "testfiles";

    private final IPath fPath;

    /**
     * Constructor to load the ITmfTrace stub
     *
     * @param path
     *      Path to the actual trace stub
     */
    public ContainerTrace(String path) {
        IPath relativePath = new Path(filePath + File.separator + path);
        Activator plugin = Activator.getDefault();
        if (plugin == null) {
            /*
             * Shouldn't happen but at least throw something to get the test to
             * fail early
             */
            throw new IllegalStateException();
        }
        URL location = FileLocator.find(plugin.getBundle(), relativePath, null);
        try {
            fPath = new Path(FileLocator.toFileURL(location).getPath());
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }

    /**
     * Return a TmfXmlTraceStub object of this test trace. It will be already
     * initTrace()'ed.
     *
     *
     * @return A TmfXmlTraceStub reference to this trace
     */
    public ITmfTrace getTrace() {
        ITmfTrace trace = new TmfXmlTraceStub();
        IStatus status = trace.validate(null, fPath.toOSString());
        if (!status.isOK()) {
            return null;
        }
        try {
            trace.initTrace(null, fPath.toOSString(), TmfEvent.class);
        } catch (TmfTraceException e) {
            return null;
        }
        return trace;
    }

    /**
     * Return the path to the file
     *
     * @return the path to the file
     */
    public String getFileName() {
        String path = fPath.toString();
        String name = path.substring(path.lastIndexOf("/")+1);
        return name;
    }
}
