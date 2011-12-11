/*
 * (C) Copyright 2011 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
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
 * 
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
