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
package org.jboss.test.ws.jaxrpc.jbws463;

import javax.naming.InitialContext;
import javax.xml.rpc.Service;

import junit.framework.Test;

import org.jboss.wsf.test.CleanupOperation;
import org.jboss.wsf.test.JBossWSTest;
import org.jboss.wsf.test.JBossWSTestSetup;



/**
 * JBoss doesn't support overloaded methods in SEIs
 *
 * http://jira.jboss.com/jira/browse/JBWS-463
 *
 * @author Thomas.Diesler@jboss.org
 * @since 21-Oct-2005
 */
public class JBWS463TestCase extends JBossWSTest
{
   private static TestSEI port;
   private static InitialContext iniCtx;

   public static Test suite() throws Exception
   {
      return new JBossWSTestSetup(JBWS463TestCase.class, "jaxrpc-jbws463.war, jaxrpc-jbws463-appclient.ear#jaxrpc-jbws463-appclient.jar", new CleanupOperation() {
         @Override
         public void cleanUp() {
            port = null;
         }
      });
   }

   protected void setUp() throws Exception
   {
      super.setUp();
      if (port == null)
      {
         iniCtx = getAppclientInitialContext();
         Service service = (Service)iniCtx.lookup("java:service/TestService");
         port = (TestSEI)service.getPort(TestSEI.class);
      }
   }

   protected void tearDown() throws Exception
   {
      if (iniCtx != null)
      {
         iniCtx.close();
         iniCtx = null;
      }
      super.tearDown();
   }

   public void testInteger() throws Exception
   {
      String retObj = port.doStuff(new Integer(1));
      assertEquals("doStuff(Integer 1)", retObj);
   }

   public void testDouble() throws Exception
   {
      String retObj = port.doStuff(new Double(1));
      assertEquals("doStuff(Double 1.0)", retObj);
   }

   public void testDoubleBoolean() throws Exception
   {
      String retObj = port.doStuff(new Double(1), true);
      assertEquals("doStuff(Double 1.0, boolean true)", retObj);
   }
}
