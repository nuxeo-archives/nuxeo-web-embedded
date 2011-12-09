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
package org.nuxeo.ecm.servlet.handlers;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.servlet.ServletHandler;
import org.nuxeo.runtime.transaction.TransactionHelper;

/**
 * @author matic
 * 
 */
public class EmbeddedServletHandler extends ServletHandler {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
            RootDocumentFetcher rdf = new RootDocumentFetcher();
            try {
                rdf.runUnrestricted();
            } catch (ClientException e) {
                throw new ServletException("Cannot fetch root doc", e);
            }
            OutputStream out = resp.getOutputStream();
            out.write(("Root: " + rdf.rootDoc.getPathAsString()).getBytes());
    }
}
