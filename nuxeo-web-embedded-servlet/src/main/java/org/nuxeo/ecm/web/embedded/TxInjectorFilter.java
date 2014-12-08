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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.nuxeo.runtime.transaction.TransactionHelper;

/**
 * @author matic
 */
public class TxInjectorFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        if (TransactionHelper.isTransactionActive()) {
            chain.doFilter(request, response);
            return;
        }

        boolean started = TransactionHelper.startTransaction();
        boolean done = false;
        try {
            chain.doFilter(request, response);
            done = true;
        } finally {
            if (done == false) {
                TransactionHelper.setTransactionRollbackOnly();
            }
            if (started) {
                TransactionHelper.commitOrRollbackTransaction();
            }
        }
    }

    @Override
    public void destroy() {

    }

}
