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
package org.jboss.test.ws.jaxws.samples.wsrm.generated;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.1-b03-
 * Generated source version: 2.0
 * 
 */
@WebService(name = "SimpleService", targetNamespace = "http://www.jboss.org/jbossws/ws-extensions/wsrm")
public interface SimpleService {


    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "echo", targetNamespace = "http://www.jboss.org/jbossws/ws-extensions/wsrm", className = "org.jboss.test.ws.jaxws.samples.wsrm.generated.Echo")
    @ResponseWrapper(localName = "echoResponse", targetNamespace = "http://www.jboss.org/jbossws/ws-extensions/wsrm", className = "org.jboss.test.ws.jaxws.samples.wsrm.generated.EchoResponse")
    public String echo(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     */
    @WebMethod
    @Oneway
    @RequestWrapper(localName = "ping", targetNamespace = "http://www.jboss.org/jbossws/ws-extensions/wsrm", className = "org.jboss.test.ws.jaxws.samples.wsrm.generated.Ping")
    public void ping();

}