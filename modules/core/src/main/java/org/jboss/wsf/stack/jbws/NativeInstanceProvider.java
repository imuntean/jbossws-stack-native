/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.wsf.stack.jbws;

import java.util.HashMap;
import java.util.Map;

import org.jboss.ws.NativeMessages;
import org.jboss.ws.common.deployment.ReferenceFactory;
import org.jboss.wsf.spi.deployment.InstanceProvider;
import org.jboss.wsf.spi.deployment.Reference;

/**
 * Native instance provider.
 *
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 */
final class NativeInstanceProvider implements InstanceProvider {

    private final ClassLoader loader;
    private final Map<String, Reference> cache = new HashMap<String, Reference>();

    NativeInstanceProvider(final ClassLoader loader)
    {
        this.loader = loader;
    }

    public synchronized Reference getInstance(final String className) {
        Reference instance = cache.get(className);
        if (instance == null) {
            try {
                instance = ReferenceFactory.newUninitializedReference(loader.loadClass(className).newInstance());
                cache.put(className, instance);
            } catch (Exception e) {
                throw NativeMessages.MESSAGES.cannotLoadClass(className, e);
            }
        }
        return instance;
    }

}
