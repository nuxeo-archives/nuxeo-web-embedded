/*
 * (C) Copyright 2011 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     matic
 */
package org.nuxeo.ecm.web.embedded;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.osgi.application.FrameworkBootstrap;

/**
 * @author matic
 */
public class NuxeoEmbeddedLoader implements ServletContextListener {

    protected static NuxeoEmbeddedLoader instance;

    protected final Log log = LogFactory.getLog(NuxeoEmbeddedLoader.class);

    protected FrameworkBootstrap fb;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        fb = loadRuntime(sce.getServletContext());
        instance = this;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            fb.stop();
        } catch (Exception e) {
            log.error("Cannot stop embedded server", e);
        }
        fb = null;
        instance = null;
    }

    protected static FrameworkBootstrap loadRuntime(ServletContext context) {
        String warFile = context.getRealPath("");
        String root = context.getInitParameter("nxhome");
        if (root == null) {
            root = "";
        }
        File appRoot = null;
        if (root.startsWith("/")) {
            appRoot = new File(root);
        } else {
            appRoot = new File(warFile + "/" + root);
        }
        root = appRoot.getAbsolutePath();

        FrameworkBootstrap fb;
        try {
            fb = new FrameworkBootstrap(NuxeoFilter.class.getClassLoader(), appRoot);
            fb.initialize();
            fb.start();
        } catch (Exception e) {
            throw new Error("Cannot load embedded server", e);
        }
        return fb;
    }

}
