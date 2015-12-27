/*
 * (C) Copyright 2006-2007 Nuxeo SA (http://nuxeo.com/) and others.
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
