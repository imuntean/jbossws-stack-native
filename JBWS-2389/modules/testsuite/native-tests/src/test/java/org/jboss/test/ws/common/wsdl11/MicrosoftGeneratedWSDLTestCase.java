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
package org.jboss.test.ws.common.wsdl11;

import java.io.File;

import org.jboss.ws.metadata.wsdl.WSDLDefinitions;
import org.jboss.ws.metadata.wsdl.WSDLTypes;
import org.jboss.ws.metadata.wsdl.WSDLUtils;
import org.jboss.ws.tools.wsdl.WSDLDefinitionsFactory;
import org.jboss.wsf.test.JBossWSTest;

/**
 * Tests WSDL11Reader ability to parse WSDL Files
 * generated by the microsoft world
 *
 * @author <mailto:Anil.Saldhana@jboss.org>Anil Saldhana
 * @since November 10, 2005
 */
public class MicrosoftGeneratedWSDLTestCase extends JBossWSTest 
{
	public void testBaseRpcLit() throws Exception
	{
		File wsdlFile = getResourceFile("common/wsdl11/microsoft-interop/rpclit/BaseTypesRpcLit.wsdl");
		assertTrue(wsdlFile.exists());
		
		
		WSDLDefinitionsFactory factory = WSDLDefinitionsFactory.newInstance();
		WSDLDefinitions wsdlDefinitions = factory.parse(wsdlFile.toURL());
		 
		// check if the schema has been extracted
		WSDLTypes wsdlTypes = wsdlDefinitions.getWsdlTypes();
		assertNotNull(WSDLUtils.getSchemaModel(wsdlTypes));
	}
}
