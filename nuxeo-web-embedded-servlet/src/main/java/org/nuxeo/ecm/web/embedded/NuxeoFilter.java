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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.nuxeo.osgi.application.FrameworkBootstrap;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 */
public class NuxeoFilter implements Filter {

    protected FrameworkBootstrap fb;

    @Override
    public void init(FilterConfig config) throws ServletException {
        if (NuxeoEmbeddedLoader.instance == null) {
            throw new ServletException("nuxeo embedded not configured in web context");
        }
        this.fb = NuxeoEmbeddedLoader.instance.fb;
    }

    @Override
    public void destroy() {

    }

    protected void checkIsAuthenticated(HttpServletRequest request) throws ServletException {
        if (request.getAuthType() != null) {
            if (request.getUserPrincipal() == null) {
                throw new ServletException("Not authenticated");
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        checkIsAuthenticated((HttpServletRequest) request);
        Thread cthread = Thread.currentThread();
        ClassLoader cl = cthread.getContextClassLoader();
        cthread.setContextClassLoader(NuxeoEmbeddedLoader.instance.fb.getClassLoader());
        try {
            chain.doFilter(request, response);
        } finally {
            cthread.setContextClassLoader(cl);
        }
    }

}
