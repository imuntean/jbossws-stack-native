/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
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
package org.jboss.ws.jaxrpc.encoding;

// $Id$

import javax.xml.namespace.QName;

import org.jboss.logging.Logger;
import org.jboss.ws.binding.BindingException;
import org.jboss.ws.utils.JavaUtils;
import org.jboss.xb.binding.NamespaceRegistry;
import org.jboss.xb.binding.SimpleTypeBindings;
import org.w3c.dom.NamedNodeMap;

/**
 * Serializer for base64
 *
 * @author Thomas.Diesler@jboss.org
 * @since 04-Dec-2004
 */
public class Base64Serializer extends SerializerSupport
{
   // provide logging
   private static final Logger log = Logger.getLogger(Base64Serializer.class);

   public String serialize(QName xmlName, QName xmlType, Object value, SerializationContextImpl serContext, NamedNodeMap attributes) throws BindingException
   {
      log.debug("serialize: [xmlName=" + xmlName + ",xmlType=" + xmlType + "]");

      value = JavaUtils.getPrimitiveValue(value);
      String valueStr = SimpleTypeBindings.marshalBase64((byte[])value);

      NamespaceRegistry nsRegistry = serContext.getNamespaceRegistry();
      String xmlFragment = wrapValueStr(xmlName, valueStr, nsRegistry, attributes);
      return xmlFragment;
   }
}
