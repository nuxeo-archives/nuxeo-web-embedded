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
package org.nuxeo.ecm.catalina;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.naming.ContextAccessController;

/**
 * @author matic
 * 
 */
public class ContextSecurityWriter implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String name = "/" + ContextSecurityGrabber.source.getDomain() + "/"
                + ContextSecurityGrabber.source.getHostname()
                + ContextSecurityGrabber.source.getPath();

        ContextAccessController.setWritable(name, ContextSecurityGrabber.token);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ;
    }

}
