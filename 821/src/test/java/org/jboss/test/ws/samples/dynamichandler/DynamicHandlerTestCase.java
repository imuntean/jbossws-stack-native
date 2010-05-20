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
package org.jboss.test.ws.samples.dynamichandler;

import java.util.Iterator;
import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.HandlerRegistry;
import javax.xml.rpc.Stub;

import junit.framework.Test;

import org.jboss.mx.util.ObjectNameFactory;
import org.jboss.test.ws.JBossWSTest;
import org.jboss.test.ws.JBossWSTestSetup;
import org.jboss.ws.jaxrpc.ServiceExt;

/**
 * Test dynamic handlers
 *
 * @author Thomas.Diesler@jboss.org
 * @since 20-Jul-2005
 */
public class DynamicHandlerTestCase extends JBossWSTest
{
   public final String TARGET_ENDPOINT_ADDRESS = "http://" + getServerHost() + ":8080/jbossws-samples-dynamichandler";
   private static final String TARGET_NAMESPACE = "http://org.jboss.ws/samples/dynamichandler";

   private static ServiceExt service;
   private static HandlerTestService endpoint;

   public static Test suite()
   {
      return JBossWSTestSetup.newTestSetup(DynamicHandlerTestCase.class, "jbossws-samples-dynamichandler.war, jbossws-samples-dynamichandler-client.jar");
   }

   protected void setUp() throws Exception
   {
      super.setUp();

      if (endpoint == null)
      {
         InitialContext iniCtx = getInitialContext();
         service = (ServiceExt)iniCtx.lookup("java:comp/env/service/TestService");
         endpoint = (HandlerTestService)service.getPort(HandlerTestService.class);
      }
      //((Stub)endpoint)._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8081/jbossws-samples-dynamichandler");
   }

   public void testStaticHandlers() throws Exception
   {
      String res = endpoint.testHandlers("InitalMessage");
      assertEquals("InitalMessage|ClientRequest|ServerRequest|ServerResponse|ClientResponse", res);
   }
   
   public void testRemoveClientHandlers() throws Exception
   {
      HandlerRegistry registry = service.getDynamicHandlerRegistry();
      QName portName = new QName(TARGET_NAMESPACE, "HandlerTestServicePort");
      
      List infos = registry.getHandlerChain(portName);
      Iterator it = infos.iterator();
      while (it.hasNext())
      {
         HandlerInfo info = (HandlerInfo)it.next();
         if (info.getHandlerClass() == ClientSideHandler.class)
            it.remove();
      }
      registry.setHandlerChain(portName, infos);
      
      String res = endpoint.testHandlers("InitalMessage");
      assertEquals("InitalMessage|ServerRequest|ServerResponse", res);
   }
   
   public void testRemoveServerHandlers() throws Exception
   {
      MBeanServerConnection server = getServer();
      ObjectName oname = ObjectNameFactory.create("jboss.ws:service=ServiceEndpointManager");
      ObjectName serviceID = new ObjectName("jboss.ws:context=jbossws-samples-dynamichandler,endpoint=TestService");

      List infos = (List)server.invoke(oname, "getHandlerInfos", new Object[]{serviceID}, new String[]{"javax.management.ObjectName"});
      Iterator it = infos.iterator();
      while (it.hasNext())
      {
         HandlerInfo info = (HandlerInfo)it.next();
         if (info.getHandlerClass() == ServerSideHandler.class)
            it.remove();
      }
      server.invoke(oname, "stopServiceEndpoint", new Object[]{serviceID}, new String[]{"javax.management.ObjectName"});
      server.invoke(oname, "setHandlerInfos", new Object[]{serviceID, infos}, new String[]{"javax.management.ObjectName", "java.util.List"});
      server.invoke(oname, "startServiceEndpoint", new Object[]{serviceID}, new String[]{"javax.management.ObjectName"});
      
      String res = endpoint.testHandlers("InitalMessage");
      assertEquals("InitalMessage", res);
   }
}
