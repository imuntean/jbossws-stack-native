/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.ws.jaxws.jbpapp1844;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import junit.framework.Test;

import org.jboss.wsf.test.JBossWSTest;
import org.jboss.wsf.test.JBossWSTestSetup;

/**
 * Test case to test jboss.xml configuration.
 * 
 * @author darran.lofthouse@jboss.com
 * @since 31st March 2009
 * @see https://jira.jboss.org/jira/browse/JBPAPP-1844
 */
public class JBPAPP1844TestCase extends JBossWSTest
{

   private static Endpoint port;

   public static Test suite()
   {
      return new JBossWSTestSetup(JBPAPP1844TestCase.class, "jaxws-jbpapp1844.jar");
   }

   public void setUp() throws Exception
   {
      super.setUp();

      if (port == null)
      {
         URL wsdlURL = new URL("http://" + getServerHost() + ":8080/jaxws-jbpapp1844/EndpointImpl?wsdl");
         QName serviceName = new QName("http://ws.jboss.org/jbpapp1844", "EndpointService");

         Service service = Service.create(wsdlURL, serviceName);
         port = service.getPort(Endpoint.class);
      }
   }

   public void testCall() throws Exception
   {
      String response = port.echo("Hello !!");
      assertEquals("Response", "XXX", response);
   }

}
