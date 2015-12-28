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
package org.nuxeo.ecm.web.embedded.demo;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.web.embedded.NuxeoServletHandler;

/**
 * @author matic
 */
public class FetchRootServletHandler extends NuxeoServletHandler {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
