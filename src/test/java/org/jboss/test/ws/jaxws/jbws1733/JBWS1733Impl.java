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
package org.jboss.test.ws.jaxws.jbws1733;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

@WebService(name="JBWS1733", serviceName="JBWS1733Service", endpointInterface="org.jboss.test.ws.jaxws.jbws1733.JBWS1733")
public class JBWS1733Impl implements JBWS1733
{
   
   @Resource
   private WebServiceContext wsContext;
   
   public int getCounter()
   {
       MessageContext mc = wsContext.getMessageContext();
       HttpSession session = ((HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST)).getSession();
       // Get a session property "counter" from context
       if (session == null)
           throw new WebServiceException("No session in WebServiceContext");
       Integer counter = (Integer)session.getAttribute("counter");
       if (counter == null) {
           counter = new Integer(0);
           System.out.println("Starting the Session");
       }
       counter = new Integer(counter.intValue() + 1);
       session.setAttribute("counter", counter);
       return counter;
   }

}
