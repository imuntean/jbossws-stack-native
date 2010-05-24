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
package org.jboss.ws.metadata.builder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.ObjectName;
import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.Port;
import javax.wsdl.extensions.http.HTTPAddress;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap12.SOAP12Address;
import javax.xml.namespace.QName;
import javax.xml.ws.addressing.AddressingProperties;

import org.jboss.logging.Logger;
import org.jboss.ws.Constants;
import org.jboss.ws.WSException;
import org.jboss.ws.core.jaxrpc.UnqualifiedFaultException;
import org.jboss.ws.core.soap.Use;
import org.jboss.ws.extensions.addressing.AddressingPropertiesImpl;
import org.jboss.ws.extensions.addressing.metadata.AddressingOpMetaExt;
import org.jboss.ws.extensions.eventing.EventingConstants;
import org.jboss.ws.extensions.eventing.EventingUtils;
import org.jboss.ws.extensions.eventing.metadata.EventingEpMetaExt;
import org.jboss.ws.metadata.umdm.EndpointMetaData;
import org.jboss.ws.metadata.umdm.FaultMetaData;
import org.jboss.ws.metadata.umdm.OperationMetaData;
import org.jboss.ws.metadata.umdm.ServerEndpointMetaData;
import org.jboss.ws.metadata.umdm.TypeMappingMetaData;
import org.jboss.ws.metadata.umdm.TypesMetaData;
import org.jboss.ws.metadata.wsdl.WSDLBinding;
import org.jboss.ws.metadata.wsdl.WSDLBindingOperation;
import org.jboss.ws.metadata.wsdl.WSDLDefinitions;
import org.jboss.ws.metadata.wsdl.WSDLEndpoint;
import org.jboss.ws.metadata.wsdl.WSDLInterface;
import org.jboss.ws.metadata.wsdl.WSDLInterfaceFault;
import org.jboss.ws.metadata.wsdl.WSDLInterfaceOperation;
import org.jboss.ws.metadata.wsdl.WSDLInterfaceOperationOutfault;
import org.jboss.ws.metadata.wsdl.WSDLInterfaceOperationOutput;
import org.jboss.ws.metadata.wsdl.WSDLProperty;
import org.jboss.ws.metadata.wsdl.WSDLService;
import org.jboss.ws.metadata.wsdl.WSDLUtils;
import org.jboss.ws.metadata.wsdl.xmlschema.JBossXSModel;
import org.jboss.wsf.common.ObjectNameFactory;
import org.jboss.wsf.spi.SPIProvider;
import org.jboss.wsf.spi.SPIProviderResolver;
import org.jboss.wsf.spi.deployment.ArchiveDeployment;
import org.jboss.wsf.spi.deployment.Deployment;
import org.jboss.wsf.spi.deployment.Endpoint;
import org.jboss.wsf.spi.management.ServerConfig;
import org.jboss.wsf.spi.management.ServerConfigFactory;
import org.jboss.wsf.spi.metadata.j2ee.EJBArchiveMetaData;
import org.jboss.wsf.spi.metadata.j2ee.EJBMetaData;
import org.jboss.wsf.spi.metadata.j2ee.MDBMetaData;
import org.jboss.wsf.spi.metadata.j2ee.JSEArchiveMetaData;
import org.jboss.wsf.spi.metadata.j2ee.JSESecurityMetaData;
import org.jboss.wsf.spi.metadata.j2ee.JSESecurityMetaData.JSEResourceCollection;

/** An abstract meta data builder.
 *
 * @author Thomas.Diesler@jboss.org
 * @since 19-May-2005
 */
public abstract class MetaDataBuilder
{
   // provide logging
   private final static Logger log = Logger.getLogger(MetaDataBuilder.class);

   /** Inititialize the endpoint binding */
   protected void initEndpointBinding(WSDLEndpoint wsdlEndpoint, EndpointMetaData epMetaData)
   {
      WSDLDefinitions wsdlDefinitions = wsdlEndpoint.getWsdlService().getWsdlDefinitions();
      WSDLInterface wsdlInterface = wsdlEndpoint.getInterface();
      WSDLBinding wsdlBinding = wsdlDefinitions.getBindingByInterfaceName(wsdlInterface.getName());
      initEndpointBinding(wsdlBinding, epMetaData);
   }

   protected void initEndpointBinding(WSDLBinding wsdlBinding, EndpointMetaData epMetaData)
   {
      String bindingType = wsdlBinding.getType();
      if (Constants.NS_SOAP11.equals(bindingType))
         epMetaData.setBindingId(Constants.SOAP11HTTP_BINDING);
      else if (Constants.NS_SOAP12.equals(bindingType))
         epMetaData.setBindingId(Constants.SOAP12HTTP_BINDING);
   }

   /** Initialize the endpoint encoding style from the binding operations
    */
   protected void initEndpointEncodingStyle(EndpointMetaData epMetaData)
   {
      WSDLDefinitions wsdlDefinitions = epMetaData.getServiceMetaData().getWsdlDefinitions();
      for (WSDLService wsdlService : wsdlDefinitions.getServices())
      {
         for (WSDLEndpoint wsdlEndpoint : wsdlService.getEndpoints())
         {
            if (epMetaData.getPortName().equals(wsdlEndpoint.getName()))
            {
               QName bindQName = wsdlEndpoint.getBinding();
               WSDLBinding wsdlBinding = wsdlDefinitions.getBinding(bindQName);
               if (wsdlBinding == null)
                  throw new WSException("Cannot obtain binding: " + bindQName);

               for (WSDLBindingOperation wsdlBindingOperation : wsdlBinding.getOperations())
               {
                  String encStyle = wsdlBindingOperation.getEncodingStyle();
                  epMetaData.setEncodingStyle(Use.valueOf(encStyle));
               }
            }
         }
      }
   }

   protected void initEndpointAddress(Deployment dep, ServerEndpointMetaData sepMetaData)
   {
      String contextRoot = dep.getService().getContextRoot();
      String urlPattern = null;

      // Get the URL pattern from the endpoint
      String linkName = sepMetaData.getLinkName();
      if (linkName != null)
      {
         Endpoint endpoint = dep.getService().getEndpointByName(linkName);
         if (endpoint != null)
            urlPattern = endpoint.getURLPattern();
      }

      // Endpoint API hack
      Integer port = (Integer)dep.getService().getProperty("port");
      if (port == null)
      {
         port = -1;
      }

      // If not, derive the context root from the deployment
      if (contextRoot == null)
      {
         String simpleName = dep.getSimpleName();
         contextRoot = simpleName.substring(0, simpleName.indexOf('.'));
         if (dep instanceof ArchiveDeployment)
         {
            if (((ArchiveDeployment)dep).getParent() != null)
            {
               simpleName = ((ArchiveDeployment)dep).getParent().getSimpleName();
               simpleName = simpleName.substring(0, simpleName.indexOf('.'));
               contextRoot = simpleName + "-" + contextRoot;
            }
         }
      }

      // Default to "/*" 
      if (urlPattern == null)
         urlPattern = "/*";

      if (contextRoot.startsWith("/") == false)
         contextRoot = "/" + contextRoot;
      if (urlPattern.startsWith("/") == false)
         urlPattern = "/" + urlPattern;

      sepMetaData.setContextRoot(contextRoot);
      sepMetaData.setURLPattern(urlPattern);

      String servicePath = contextRoot + urlPattern;
      sepMetaData.setEndpointAddress(getServiceEndpointAddress(null, servicePath, port));
   }

   public static ObjectName createServiceEndpointID(Deployment dep, ServerEndpointMetaData sepMetaData)
   {
      String linkName = sepMetaData.getLinkName();
      String context = sepMetaData.getContextRoot();
      if (context.startsWith("/"))
         context = context.substring(1);

      StringBuilder idstr = new StringBuilder(ServerEndpointMetaData.SEPID_DOMAIN + ":");
      idstr.append(ServerEndpointMetaData.SEPID_PROPERTY_CONTEXT + "=" + context);
      idstr.append("," + ServerEndpointMetaData.SEPID_PROPERTY_ENDPOINT + "=" + linkName);

      // Add JMS destination JNDI name for MDB endpoints
      EJBArchiveMetaData apMetaData = dep.getAttachment(EJBArchiveMetaData.class);
      if (apMetaData != null)
      {
         String ejbName = sepMetaData.getLinkName();
         if (ejbName == null)
            throw new WSException("Cannot obtain ejb-link from port component");

         EJBMetaData beanMetaData = (EJBMetaData)apMetaData.getBeanByEjbName(ejbName);
         if (beanMetaData == null)
            throw new WSException("Cannot obtain ejb meta data for: " + ejbName);

         if (beanMetaData instanceof MDBMetaData)
         {
            MDBMetaData mdMetaData = (MDBMetaData)beanMetaData;
            String jndiName = mdMetaData.getDestinationJndiName();
            idstr.append(",jms=" + jndiName);
         }
      }

      return ObjectNameFactory.create(idstr.toString());
   }

   /** Get the web service address for a given path
    */
   public static String getServiceEndpointAddress(String uriScheme, String servicePath, int servicePort)
   {
      if (servicePath == null || servicePath.length() == 0)
         throw new WSException("Service path cannot be null");

      if (servicePath.endsWith("/*"))
         servicePath = servicePath.substring(0, servicePath.length() - 2);

      if (uriScheme == null)
         uriScheme = "http";

      SPIProvider spiProvider = SPIProviderResolver.getInstance().getProvider();
      ServerConfig config = spiProvider.getSPI(ServerConfigFactory.class).getServerConfig();

      String host = config.getWebServiceHost();

      int port = servicePort;
      if (servicePort == -1)
      {
         if ("https".equals(uriScheme))
         {
            port = config.getWebServiceSecurePort();
         }
         else
         {
            port = config.getWebServicePort();
         }
      }

      // Reset port if using the default for the scheme.
      if (("http".equals(uriScheme) && port == 80) || ("https".equals(uriScheme) && port == 443))
      {
         port = -1;
      }

      URL url = null;
      try
      {
         if (port > -1)
         {
            url = new URL(uriScheme, host, port, servicePath);
         }
         else
         {
            url = new URL(uriScheme, host, servicePath);
         }
         return url.toExternalForm();
      }
      catch (MalformedURLException e)
      {
         throw new WSException("Malformed URL: uriScheme={" + uriScheme + "} host={" + host + "} port={" + port + "} servicePath={" + servicePath + "}", e);
      }
   }

   /**
    * Read the transport guarantee from web.xml
    */
   protected void initTransportGuaranteeJSE(Deployment dep, ServerEndpointMetaData sepMetaData, String servletLink) throws IOException
   {
      String transportGuarantee = null;
      JSEArchiveMetaData webMetaData = dep.getAttachment(JSEArchiveMetaData.class);
      if (webMetaData != null)
      {
         Map<String, String> servletMappings = webMetaData.getServletMappings();
         String urlPattern = servletMappings.get(servletLink);

         if (urlPattern == null)
            throw new WSException("Cannot find <url-pattern> for servlet-name: " + servletLink);

         List<JSESecurityMetaData> securityList = webMetaData.getSecurityMetaData();
         for (JSESecurityMetaData currentSecurity : securityList)
         {
            if (currentSecurity.getTransportGuarantee() != null && currentSecurity.getTransportGuarantee().length() > 0)
            {
               for (JSEResourceCollection currentCollection : currentSecurity.getWebResources())
               {
                  for (String currentUrlPattern : currentCollection.getUrlPatterns())
                  {
                     if (urlPattern.equals(currentUrlPattern) || "/*".equals(currentUrlPattern))
                     {
                        transportGuarantee = currentSecurity.getTransportGuarantee();
                     }
                  }
               }
            }
         }
      }
      sepMetaData.setTransportGuarantee(transportGuarantee);
   }

   /** Replace the address locations for a given port component.
    */
   public static void replaceAddressLocation(ServerEndpointMetaData sepMetaData)
   {
      WSDLDefinitions wsdlDefinitions = sepMetaData.getServiceMetaData().getWsdlDefinitions();
      QName portName = sepMetaData.getPortName();

      boolean endpointFound = false;
      for (WSDLService wsdlService : wsdlDefinitions.getServices())
      {
         for (WSDLEndpoint wsdlEndpoint : wsdlService.getEndpoints())
         {
            QName wsdlPortName = wsdlEndpoint.getName();
            if (wsdlPortName.equals(portName))
            {
               endpointFound = true;

               String orgAddress = wsdlEndpoint.getAddress();
               String uriScheme = getUriScheme(orgAddress);

               String transportGuarantee = sepMetaData.getTransportGuarantee();
               if ("CONFIDENTIAL".equals(transportGuarantee))
                  uriScheme = "https";

               if (requiresRewrite(orgAddress, uriScheme))
               {
                  String servicePath = sepMetaData.getContextRoot() + sepMetaData.getURLPattern();
                  String serviceEndpointURL = getServiceEndpointAddress(uriScheme, servicePath, -1);

                  if (log.isDebugEnabled())
                     log.debug("Replace service endpoint address '" + orgAddress + "' with '" + serviceEndpointURL + "'");
                  wsdlEndpoint.setAddress(serviceEndpointURL);
                  sepMetaData.setEndpointAddress(serviceEndpointURL);

                  // modify the wsdl-1.1 definition
                  if (wsdlDefinitions.getWsdlOneOneDefinition() != null)
                     replaceWSDL11PortAddress(wsdlDefinitions, portName, serviceEndpointURL);
               }
               else
               {
                  if (log.isDebugEnabled())
                     log.debug("Don't replace service endpoint address '" + orgAddress + "'");
                  try
                  {
                     sepMetaData.setEndpointAddress(new URL(orgAddress).toExternalForm());
                  }
                  catch (MalformedURLException e)
                  {
                     log.warn("Malformed URL: " + orgAddress);
                     sepMetaData.setEndpointAddress(orgAddress);
                  }
               }
            }
         }
      }

      if (endpointFound == false)
         throw new WSException("Cannot find port in wsdl: " + portName);
   }
   
   private static boolean requiresRewrite(String orgAddress, String uriScheme)
   {
      if (uriScheme != null)
      {
         if (!uriScheme.toLowerCase().startsWith("http"))
         {
            //perform rewrite on http/https addresses only
            return false;
         }
      }
      SPIProvider spiProvider = SPIProviderResolver.getInstance().getProvider();
      ServerConfig config = spiProvider.getSPI(ServerConfigFactory.class).getServerConfig();
      boolean alwaysModify = config.isModifySOAPAddress();
      
      return (alwaysModify || uriScheme == null || orgAddress.indexOf("REPLACE_WITH_ACTUAL_URL") >= 0);
   }

   private static void replaceWSDL11PortAddress(WSDLDefinitions wsdlDefinitions, QName portQName, String serviceEndpointURL)
   {
      Definition wsdlOneOneDefinition = wsdlDefinitions.getWsdlOneOneDefinition();
      String tnsURI = wsdlOneOneDefinition.getTargetNamespace();

      // search for matching portElement and replace the address URI
      if (modifyPortAddress(tnsURI, portQName, serviceEndpointURL, wsdlOneOneDefinition.getServices()))
      {
         return;
      }

      // recursively process imports if none can be found
      if (!wsdlOneOneDefinition.getImports().isEmpty())
      {

         Iterator imports = wsdlOneOneDefinition.getImports().values().iterator();
         while (imports.hasNext())
         {
            List l = (List)imports.next();
            Iterator importsByNS = l.iterator();
            while (importsByNS.hasNext())
            {
               Import anImport = (Import)importsByNS.next();
               if (modifyPortAddress(anImport.getNamespaceURI(), portQName, serviceEndpointURL, anImport.getDefinition().getServices()))
               {
                  return;
               }
            }
         }
      }
      
      throw new IllegalArgumentException("Cannot find port with name '" + portQName + "' in wsdl document");
   }

   /**
    * Try matching the port and modify it. Return true if the port is actually matched.
    * 
    * @param tnsURI
    * @param portQName
    * @param serviceEndpointURL
    * @param services
    * @return
    */
   private static boolean modifyPortAddress(String tnsURI, QName portQName, String serviceEndpointURL, Map services)
   {
      Iterator itServices = services.values().iterator();
      while (itServices.hasNext())
      {
         javax.wsdl.Service wsdlOneOneService = (javax.wsdl.Service)itServices.next();
         Map wsdlOneOnePorts = wsdlOneOneService.getPorts();
         Iterator itPorts = wsdlOneOnePorts.keySet().iterator();
         while (itPorts.hasNext())
         {
            String portLocalName = (String)itPorts.next();
            if (portQName.equals(new QName(tnsURI, portLocalName)))
            {
               Port wsdlOneOnePort = (Port)wsdlOneOnePorts.get(portLocalName);
               List extElements = wsdlOneOnePort.getExtensibilityElements();
               for (Object extElement : extElements)
               {
                  if (extElement instanceof SOAPAddress)
                  {
                     SOAPAddress address = (SOAPAddress)extElement;
                     address.setLocationURI(serviceEndpointURL);
                  }
                  else if (extElement instanceof SOAP12Address)
                  {
                     SOAP12Address address = (SOAP12Address)extElement;
                     address.setLocationURI(serviceEndpointURL);
                  }
                  else if (extElement instanceof HTTPAddress)
                  {
                     HTTPAddress address = (HTTPAddress)extElement;
                     address.setLocationURI(serviceEndpointURL);
                  }
               }
               return true;
            }
         }
      }
      return false;
   }

   private static String getUriScheme(String addrStr)
   {
      try
      {
         URI addrURI = new URI(addrStr);
         String scheme = addrURI.getScheme();
         return scheme;
      }
      catch (URISyntaxException e)
      {
         return null;
      }
   }

   protected void processEndpointMetaDataExtensions(EndpointMetaData epMetaData, WSDLDefinitions wsdlDefinitions)
   {
      for (WSDLInterface wsdlInterface : wsdlDefinitions.getInterfaces())
      {
         WSDLProperty eventSourceProp = wsdlInterface.getProperty(Constants.WSDL_PROPERTY_EVENTSOURCE);
         if (eventSourceProp != null && epMetaData instanceof ServerEndpointMetaData)
         {
            ServerEndpointMetaData sepMetaData = (ServerEndpointMetaData)epMetaData;
            String eventSourceNS = wsdlInterface.getName().getNamespaceURI() + "/" + wsdlInterface.getName().getLocalPart();

            // extract the schema model
            JBossXSModel schemaModel = WSDLUtils.getSchemaModel(wsdlDefinitions.getWsdlTypes());
            String[] notificationSchema = EventingUtils.extractNotificationSchema(schemaModel);

            // extract the root element NS
            String notificationRootElementNS = null;
            WSDLInterfaceOperation wsdlInterfaceOperation = wsdlInterface.getOperations()[0];
            if (wsdlInterfaceOperation.getOutputs().length > 0)
            {
               WSDLInterfaceOperationOutput wsdlInterfaceOperationOutput = wsdlInterfaceOperation.getOutputs()[0];
               notificationRootElementNS = wsdlInterfaceOperationOutput.getElement().getNamespaceURI();
            }
            else
            {
               // WSDL operation of an WSDL interface that is marked as an event source
               // requires to carry an output message.
               throw new WSException("Unable to resolve eventing root element NS. No operation output found at " + wsdlInterfaceOperation.getName());
            }

            EventingEpMetaExt ext = new EventingEpMetaExt(EventingConstants.NS_EVENTING);
            ext.setEventSourceNS(eventSourceNS);
            ext.setNotificationSchema(notificationSchema);
            ext.setNotificationRootElementNS(notificationRootElementNS);
            sepMetaData.addExtension(ext);
         }
      }
   }

   /** Process operation meta data extensions. */
   protected void processOpMetaExtensions(OperationMetaData opMetaData, WSDLInterfaceOperation wsdlOperation)
   {
      final AddressingProperties ADDR = new AddressingPropertiesImpl();
      final AddressingOpMetaExt addrExt = new AddressingOpMetaExt(ADDR.getNamespaceURI());
      final boolean isOneWay = Constants.WSDL20_PATTERN_IN_ONLY.equals(wsdlOperation.getPattern());

      final String inputAction = this.getInputAction(wsdlOperation, isOneWay);
      addrExt.setInboundAction(inputAction);
      
      if (!isOneWay)
      {
         final String outputAction = this.getOutputAction(wsdlOperation, isOneWay);
         addrExt.setOutboundAction(outputAction);
         
         setFaultActions(opMetaData, wsdlOperation, addrExt);
      }

      opMetaData.addExtension(addrExt);
   }
   
   private void setFaultActions(final OperationMetaData opMetaData, final WSDLInterfaceOperation wsdlOperation, final AddressingOpMetaExt addrExt)
   {
      for (WSDLInterfaceOperationOutfault fault : wsdlOperation.getOutfaults())
      {
         final QName faultQName = wsdlOperation.getWsdlInterface().getFault(fault.getRef()).getElement();
         final String action = this.getFaultAction(wsdlOperation, fault);
         
         addrExt.setFaultAction(faultQName, action);
      }
   }
   
   /* 
   Copy/paste from http://www.w3.org/TR/wsdl#_names

   2.4.5 Names of Elements within an Operation

   The name attribute of the input and output elements provides a unique name among all
   input and output elements within the enclosing port type.

   In order to avoid having to name each input and output element within an operation,
   WSDL provides some default values based on the operation name. If the name attribute
   is not specified on a one-way or notification message, it defaults to the name of the operation.
   If the name attribute is not specified on the input or output messages of a
   request-response or solicit-response operation, the name defaults to the name of
   the operation with "Request"/"Solicit" or "Response" appended, respectively.

   Each fault element must be named to allow a binding to specify the concrete format of the fault message.
   The name of the fault element is unique within the set of faults defined for the operation.
   */

   private String getInputAction(final WSDLInterfaceOperation wsdlOperation, final boolean oneWay)
   {
      WSDLProperty wsaInAction = wsdlOperation.getProperty(Constants.WSDL_PROPERTY_ACTION_IN);
      if (wsaInAction != null && wsaInAction.getValue() != null && !"".equals(wsaInAction.getValue()))
      {
         return wsaInAction.getValue();
      }

      final String prefix = this.getActionPrefix(wsdlOperation);
      String operationName = wsdlOperation.getName().getLocalPart();

      WSDLProperty inputName = wsdlOperation.getProperty(Constants.WSDL_PROPERTY_MESSAGE_NAME_IN);
      if (inputName != null && inputName.getValue() != null && !"".equals(inputName.getValue()))
      {
         return prefix + inputName.getValue();
      }
      else
      {
         return prefix + operationName + (oneWay ? "" : "Request");
      }
   }
   
   private String getOutputAction(final WSDLInterfaceOperation wsdlOperation, final boolean oneWay)
   {
      WSDLProperty wsaOutAction = wsdlOperation.getProperty(Constants.WSDL_PROPERTY_ACTION_OUT);
      if (wsaOutAction != null && wsaOutAction.getValue() != null && !"".equals(wsaOutAction.getValue()))
      {
         return wsaOutAction.getValue();
      }

      final String prefix = this.getActionPrefix(wsdlOperation);
      String operationName = wsdlOperation.getName().getLocalPart();

      WSDLProperty outputName = wsdlOperation.getProperty(Constants.WSDL_PROPERTY_MESSAGE_NAME_OUT);
      if (outputName != null && outputName.getValue() != null && !"".equals(outputName.getValue()))
      {
         return prefix + outputName.getValue();
      }
      else
      {
         return prefix + operationName + (oneWay ? "" : "Response");
      }
   }
   
   /*
    Copy/paste from http://www.w3.org/TR/2007/REC-ws-addr-metadata-20070904/#defactionwsdl11

    4.4.4 Default Action Pattern for WSDL 1.1

    A default pattern is also defined for backwards compatibility with WSDL 1.1. In the absence of an explicitly specified value
    for the [action] property (see section 4.4.1 Explicit Association), the following pattern is used to construct a default action
    for inputs and outputs. The general form of an action IRI is as follows:

    Example 4-6. Structure of defaulted wsa:Action IRI.

    [target namespace][delimiter][port type name][delimiter][input|output name]

    For fault messages, the general form of an action IRI is as follows:

    Example 4-7. Structure of default wsa:Action IRI for faults

    [target namespace][delimiter][port type name][delimiter][operation name][delimiter]Fault[delimiter][fault name]
    */
   private String getFaultAction(final WSDLInterfaceOperation wsdlOperation, final WSDLInterfaceOperationOutfault fault)
   {
      final WSDLProperty wsaFaultAction = fault.getProperty(Constants.WSDL_PROPERTY_ACTION_FAULT);
      if (wsaFaultAction != null && wsaFaultAction.getValue() != null && !"".equals(wsaFaultAction.getValue()))
      {
         return wsaFaultAction.getValue();
      }

      final String prefix = this.getActionPrefix(wsdlOperation);
      String operationName = wsdlOperation.getName().getLocalPart();

      final WSDLProperty faultName = fault.getProperty(Constants.WSDL_PROPERTY_MESSAGE_NAME_FAULT);
      final String delimeter = this.getDelimeter(prefix);
      if (faultName != null && faultName.getValue() != null && !"".equals(faultName.getValue()))
      {
         return prefix + operationName + delimeter + "Fault" + delimeter + faultName.getValue();
      }
      
      throw new IllegalStateException();
   }
   
   private String getActionPrefix(final WSDLInterfaceOperation wsdlOperation)
   {
      final String portTypeName = wsdlOperation.getWsdlInterface().getName().getLocalPart();
      String namespace = wsdlOperation.getName().getNamespaceURI();
      final String delimeter = this.getDelimeter(namespace);

      if (!namespace.endsWith(delimeter))
         namespace += delimeter;
      
      return namespace + portTypeName + delimeter;
   }
   
   private String getDelimeter(final String namespace)
   {
      return namespace.toLowerCase().startsWith("urn:") ? ":" : "/";
   }
   
   protected void buildFaultMetaData(OperationMetaData opMetaData, WSDLInterfaceOperation wsdlOperation)
   {
      TypesMetaData typesMetaData = opMetaData.getEndpointMetaData().getServiceMetaData().getTypesMetaData();

      WSDLInterface wsdlInterface = wsdlOperation.getWsdlInterface();
      for (WSDLInterfaceOperationOutfault outFault : wsdlOperation.getOutfaults())
      {
         QName ref = outFault.getRef();

         WSDLInterfaceFault wsdlFault = wsdlInterface.getFault(ref);
         QName xmlName = wsdlFault.getElement();
         QName xmlType = wsdlFault.getXmlType();
         String javaTypeName = null;

         if (xmlType == null)
         {
            log.warn("Cannot obtain fault type for element: " + xmlName);
            xmlType = xmlName;
         }

         TypeMappingMetaData tmMetaData = typesMetaData.getTypeMappingByXMLType(xmlType);
         if (tmMetaData != null)
            javaTypeName = tmMetaData.getJavaTypeName();

         if (javaTypeName == null)
         {
            log.warn("Cannot obtain java type mapping for: " + xmlType);
            javaTypeName = new UnqualifiedFaultException(xmlType).getClass().getName();
         }

         FaultMetaData faultMetaData = new FaultMetaData(opMetaData, xmlName, xmlType, javaTypeName);
         opMetaData.addFault(faultMetaData);
      }
   }
}
