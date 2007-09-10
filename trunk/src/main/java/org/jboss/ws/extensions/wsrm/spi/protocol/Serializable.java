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
package org.jboss.ws.extensions.wsrm.spi.protocol;

import javax.xml.soap.SOAPMessage;

import org.jboss.ws.extensions.wsrm.ReliableMessagingException;

/**
 * This interface identifies classes that are de/serializable from/to SOAP messages
 *
 * @author richard.opalka@jboss.com
 */
public interface Serializable
{
   
   /**
    * Serialize object instance to SOAP message
    * @param soapMessage 
    */
   void serializeTo(SOAPMessage soapMessage) throws ReliableMessagingException;
   
   /**
    * Deserialize object instance from SOAP message
    * @param soapMessage
    */
   void deserializeFrom(SOAPMessage soapMessage) throws ReliableMessagingException;
   
   /**
    * Validate object state if everything is all right
    */
   void validate();
   
}
