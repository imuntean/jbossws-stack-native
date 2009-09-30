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
package org.jboss.test.ws.jaxws.jbws2706;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.1-b03-
 * Generated source version: 2.0
 * 
 */
@WebService(name = "SwaTest", targetNamespace = "http://SwaTestService.org/wsdl")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface SwaTest {


    /**
     * 
     * @param attach1
     * @param attach2
     * @param request
     * @return
     *     returns org.jboss.test.ws.jaxws.jbws2706.OutputResponse
     */
    @WebMethod
    @WebResult(name = "OutputResponse", targetNamespace = "http://SwaTestService.org/xsd", partName = "response")
    public OutputResponse echoMultipleAttachments(
        @WebParam(name = "InputRequest", targetNamespace = "http://SwaTestService.org/xsd", partName = "request")
        InputRequest request,
        @WebParam(name = "attach1", targetNamespace = "", mode = WebParam.Mode.INOUT, partName = "attach1")
        Holder<String> attach1,
        @WebParam(name = "attach2", targetNamespace = "", mode = WebParam.Mode.INOUT, partName = "attach2")
        Holder<byte[]> attach2);

}