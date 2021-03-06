/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.ws.core.jaxrpc.binding;

import javax.xml.namespace.QName;
import javax.xml.transform.Result;

import org.jboss.logging.Logger;
import org.jboss.ws.NativeMessages;
import org.jboss.ws.common.DOMWriter;
import org.jboss.ws.core.binding.BindingException;
import org.jboss.ws.core.binding.SerializationContext;
import org.jboss.ws.core.binding.SerializerSupport;
import org.jboss.ws.util.xml.BufferedStreamResult;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 * A serializer that can handle xsd:anyType
 *
 * @author Thomas.Diesler@jboss.org
 * @since 18-Oct-2004
 */
public class ElementSerializer extends SerializerSupport
{
   // provide logging
   private static final Logger log = Logger.getLogger(ElementSerializer.class);

   /** Marshal the value for a given XMLSchema type
    * @param xmlType local part of the schema type
    * @param value the value to marshal
    * @param serContext
    * @return the string representation od the value
    */
   public Result serialize(QName xmlName, QName xmlType, Object value, SerializationContext serContext, NamedNodeMap attributes)
         throws BindingException
   {
      log.debug("serialize: [xmlName=" + xmlName + ",xmlType=" + xmlType + "]");
      if (value == null)
         throw NativeMessages.MESSAGES.illegalNullArgument("value");
      if ((value instanceof Element) == false)
         throw NativeMessages.MESSAGES.valueIsNotA(Element.class, value.getClass());

      String xmlFragment = DOMWriter.printNode((Element)value, false);
      return new BufferedStreamResult(xmlFragment);
   }
}
