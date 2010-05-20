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
import org.jboss.util.xml.DOMUtils;
import org.jboss.ws.binding.BindingException;
import org.w3c.dom.Element;

/**
 * A deserializer that can handle xsd:anyType
 *
 * @author Thomas.Diesler@jboss.org
 * @since 23-Jun-2005
 */
public class ElementDeserializer extends DeserializerSupport
{
   // provide logging
   private static final Logger log = Logger.getLogger(ElementDeserializer.class);
   
   /** Deserialize the given simple xmlString
    */
   public Object deserialize(QName xmlName, QName xmlType, String xmlFragment, SerializationContextImpl serContext) throws BindingException
   {
      log.debug("deserialize: [xmlName=" + xmlName + ",xmlType=" + xmlType + "]");
      try
      {
         Element domElement = DOMUtils.parse(xmlFragment);
         return domElement;
      }
      catch (RuntimeException rte)
      {
         throw rte;
      }
      catch (Exception ex)
      {
         throw new BindingException();
      }
   }
}
