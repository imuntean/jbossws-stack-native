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
package org.jboss.ws.core.soap;

import java.util.ResourceBundle;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.jboss.ws.core.soap.BundleUtils;
import org.jboss.ws.core.client.HTTPRemotingConnection;
import org.jboss.ws.core.client.RemoteConnection;
import org.jboss.ws.core.client.SOAPProtocolConnectionHTTP;

/**
 * SOAPConnection implementation
 * 
 * @author Thomas.Diesler@jboss.org
 * @author <a href="mailto:jason@stacksmash.com">Jason T. Greene</a>
 * 
 * @since 02-Feb-2005
 */
public class SOAPConnectionImpl extends SOAPConnection
{
   private static final ResourceBundle bundle = BundleUtils.getBundle(SOAPConnectionImpl.class);

   private RemoteConnection remotingConnection;

   public SOAPConnectionImpl()
   {
      remotingConnection = new SOAPProtocolConnectionHTTP();
   }

   /**
    * Sends the given message to the specified endpoint and blocks until it has
    * returned the response.
    */
   @Override
   public SOAPMessage call(SOAPMessage reqMessage, Object endpoint) throws SOAPException
   {
      if (reqMessage == null)
         throw new IllegalArgumentException(BundleUtils.getMessage(bundle, "SOAPMESSAGE_CANNOT_BE_NULL"));

      return callInternal(reqMessage, endpoint, false);
   }

   /**
    * Sends an HTTP GET request to an endpoint and blocks until a SOAP message is received
    */
   @Override
   public SOAPMessage get(Object endpoint) throws SOAPException
   {
      return callInternal(null, endpoint, false);
   }

   /**
    * Sends the given message to the specified endpoint. This method is logically
    * non blocking.
    */
   public SOAPMessage callOneWay(SOAPMessage reqMessage, Object endpoint) throws SOAPException
   {
      if (reqMessage == null)
         throw new IllegalArgumentException(BundleUtils.getMessage(bundle, "SOAPMESSAGE_CANNOT_BE_NULL"));

      return callInternal(reqMessage, endpoint, true);
   }

   private RemoteConnection getRemotingConnection(Object endpoint)
   {
      if (endpoint == null)
         throw new IllegalArgumentException(BundleUtils.getMessage(bundle, "ENDPOINT_CANNOT_BE_NULL"));

      return remotingConnection;
   }

   /**
    * Closes this SOAPConnection
    */
   @Override
   public void close() throws SOAPException
   {
      if (remotingConnection != null && remotingConnection instanceof HTTPRemotingConnection)
      {
         HTTPRemotingConnection conn = (HTTPRemotingConnection) remotingConnection;
         if (conn.isClosed())
            throw new SOAPException(BundleUtils.getMessage(bundle, "SOAPCONNECTION_IS_ALREADY_CLOSED"));

         conn.setClosed(true);
      }
   }

   private SOAPMessage callInternal(SOAPMessage reqMessage, Object endpoint, boolean oneway) throws SOAPException
   {
      try
      {
         remotingConnection = getRemotingConnection(endpoint);
         SOAPMessage resMessage = remotingConnection.invoke((SOAPMessageImpl)reqMessage, endpoint, oneway);
         return resMessage;
      }
      catch (Exception ex)
      {
         Throwable cause = ex.getCause();
         if (cause instanceof SOAPException)
            throw (SOAPException)cause;

         throw new SOAPException(ex);
      }
   }

}
