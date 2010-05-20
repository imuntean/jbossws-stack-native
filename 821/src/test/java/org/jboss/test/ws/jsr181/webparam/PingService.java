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
package org.jboss.test.ws.jsr181.webparam;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.WebParam;
import javax.jws.Oneway;
import javax.jws.soap.SOAPBinding;

import org.jboss.logging.Logger;

/**
 * Test the JSR-181 annotation: javax.jws.WebParam
 *
 * @author Thomas.Diesler@jboss.org
 * @since 07-Oct-2005
 */
@WebService(targetNamespace = "http://www.openuri.org/jsr181/WebParamExample")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class PingService
{
   // Provide logging
   private static Logger log = Logger.getLogger(PingService.class);

   @WebMethod
   public PingDocument echo(PingDocument p)
   {
      log.info("echo: " + p);
      return p;
   }

   @WebMethod(operationName = "PingOneWay")
   @Oneway
   public void ping(
         @WebParam(name = "Ping") PingDocument p)
   {
      log.info("ping: " + p);
   }

   @WebMethod(operationName = "PingTwoWay")
   public void ping(
         @WebParam(name = "Ping", mode = WebParam.Mode.INOUT) PingDocumentHolder p)
   {
      log.info("ping: " + p.value);
      p.value = new PingDocument(p.value.getContent() + " Response");
   }

   @WebMethod(operationName = "SecurePing")
   @Oneway
   public void ping(
         @WebParam(name = "Ping") PingDocument p, 
         @WebParam(name = "SecHeader", header = true) SecurityHeader secHdr)
   {
      log.info("ping: " + p + "," + secHdr);
   }
}