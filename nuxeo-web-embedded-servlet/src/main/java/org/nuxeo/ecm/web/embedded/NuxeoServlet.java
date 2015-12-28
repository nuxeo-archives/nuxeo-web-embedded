/*
 * (C) Copyright 2006-2007 Nuxeo SA (http://nuxeo.com/) and others.
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
 *     bstefanescu
 *
 * $Id$
 */

package org.nuxeo.ecm.web.embedded;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nuxeo.osgi.application.FrameworkBootstrap;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 */
public class NuxeoServlet extends HttpServlet {

    private static final long serialVersionUID = 7547124250731789991L;

    protected FrameworkBootstrap fb;

    protected NuxeoServletHandler handler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (NuxeoEmbeddedLoader.instance == null) {
            throw new ServletException("Nuxeo embedded not configured in web context");
        }
        this.fb = NuxeoEmbeddedLoader.instance.fb;
        String handlerClass = getInitParameter("handler");
        if (handlerClass == null) {
            throw new ServletException("No servlet handler class specified. "
                    + "In web.xml you need to add an init parameter to that server "
                    + "named 'handler' that points to a servlet handler");
        }

        try {
            final Object newInstance = NuxeoEmbeddedLoader.instance.fb.getClassLoader().loadClass(handlerClass).newInstance();
            handler = (NuxeoServletHandler) newInstance;
            initHandler();
        } catch (Exception e) {
            throw new ServletException("Failed to instantiate handler", e);
        }
    }

    @Override
    public void destroy() {
        destroyHandler();
        handler = null;
        fb = null;
        super.destroy();
    }

    private void initHandler() throws Exception {
        Thread cthread = Thread.currentThread();
        ClassLoader cl = cthread.getContextClassLoader();
        cthread.setContextClassLoader(fb.getClassLoader());
        try {
            handler.init(this);
        } finally {
            cthread.setContextClassLoader(cl);
        }
    }

    private void destroyHandler() {
        Thread cthread = Thread.currentThread();
        ClassLoader cl = cthread.getContextClassLoader();
        cthread.setContextClassLoader(fb.getClassLoader());
        try {
            handler.destroy();
        } finally {
            cthread.setContextClassLoader(cl);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Thread cthread = Thread.currentThread();
        ClassLoader cl = cthread.getContextClassLoader();
        cthread.setContextClassLoader(fb.getClassLoader());
        try {
            if (req.getAuthType() != null) {
                if (req.getUserPrincipal() == null) {
                    throw new ServletException("Not authenticated");
                }
            }
            super.service(req, resp);
        } finally {
            cthread.setContextClassLoader(cl);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handler.doDelete(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handler.doGet(req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handler.doHead(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        handler.doOptions(arg0, arg1);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handler.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handler.doPut(req, resp);
    }

    @Override
    protected void doTrace(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
        handler.doTrace(arg0, arg1);
    }

}
