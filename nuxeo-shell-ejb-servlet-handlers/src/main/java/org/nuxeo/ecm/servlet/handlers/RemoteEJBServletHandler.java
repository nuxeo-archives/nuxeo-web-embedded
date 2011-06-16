/*
 * (C) Copyright 2006-2007 Nuxeo SAS (http://nuxeo.com/) and contributors.
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

package org.nuxeo.ecm.servlet.handlers;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nuxeo.ecm.core.api.CoreInstance;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.repository.Repository;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
import org.nuxeo.ecm.core.client.DefaultLoginHandler;
import org.nuxeo.ecm.core.client.LoginHandler;
import org.nuxeo.ecm.core.client.NuxeoClient;
import org.nuxeo.ecm.servlet.NuxeoServlet;
import org.nuxeo.ecm.servlet.ServletHandler;
import org.nuxeo.runtime.api.Framework;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class RemoteEJBServletHandler extends ServletHandler {

    String host;
    int port;

    LoginHandler lh;

    
    @Override
    protected void init(NuxeoServlet servlet) throws ServletException {
        super.init(servlet);

       host = servlet.getInitParameter("host");
       String p = servlet.getInitParameter("port");
       String username = servlet.getInitParameter("user");
       if (username == null) {
           username = "Administrator";
       }
       String password = servlet.getInitParameter("password");
       if (password == null) {
           password = "Administrator";
       }
       lh = new DefaultLoginHandler(username, password);
       port = Integer.parseInt(p);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        OutputStream out = resp.getOutputStream();
        CoreSession session = null;
        NuxeoClient nc = new NuxeoClient();
        try {
            nc.setLoginHandler(lh);
            nc.tryConnect(host, port);
            Repository repository = Framework.getService(RepositoryManager.class).getDefaultRepository();
            session = repository.open();
            String path = session.getRootDocument().getPathAsString();
            out.write(("Root: "+path).getBytes());
        } catch (Exception e) {
            throw new ServletException("Failed to run demo", e);
        } finally {
            try {
                if (session != null) {
                    CoreInstance.getInstance().close(session);
                }
                nc.tryDisconnect();
            } catch (Exception e) {
                throw new ServletException("Failed to close session", e);
            }
        }
    }
}
