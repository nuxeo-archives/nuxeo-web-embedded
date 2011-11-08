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

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.core.StandardContext;

/**
 * @author matic
 *
 */
public class ContextSecurityGrabber implements LifecycleListener {

    protected static Lifecycle token;
    
    protected static StandardContext source;
    
    @Override
    public void lifecycleEvent(LifecycleEvent event) {
        if (!"start".equals(event.getType())) {
            return;
        }
        token = event.getLifecycle();
        source = (StandardContext) event.getSource();
    }
   
}
