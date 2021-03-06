/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.ws.core.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import javax.xml.namespace.QName;
import javax.xml.rpc.server.ServiceLifecycle;
import javax.xml.rpc.server.ServletEndpointContext;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import org.jboss.logging.Logger;
import org.jboss.ws.NativeLoggers;
import org.jboss.ws.NativeMessages;
import org.jboss.ws.common.Constants;
import org.jboss.ws.common.JavaUtils;
import org.jboss.ws.core.CommonBinding;
import org.jboss.ws.core.CommonBindingProvider;
import org.jboss.ws.core.CommonMessageContext;
import org.jboss.ws.core.CommonSOAPBinding;
import org.jboss.ws.core.CommonSOAPFaultException;
import org.jboss.ws.core.DirectionHolder;
import org.jboss.ws.core.DirectionHolder.Direction;
import org.jboss.ws.core.EndpointInvocation;
import org.jboss.ws.core.jaxrpc.ServletEndpointContextImpl;
import org.jboss.ws.core.jaxrpc.handler.HandlerDelegateJAXRPC;
import org.jboss.ws.core.jaxrpc.handler.MessageContextJAXRPC;
import org.jboss.ws.core.jaxrpc.handler.SOAPMessageContextJAXRPC;
import org.jboss.ws.core.soap.SOAPBodyImpl;
import org.jboss.ws.core.soap.SOAPMessageDispatcher;
import org.jboss.ws.core.soap.utils.MessageContextAssociation;
import org.jboss.ws.metadata.umdm.EndpointMetaData;
import org.jboss.ws.metadata.umdm.OperationMetaData;
import org.jboss.ws.metadata.umdm.ServerEndpointMetaData;
import org.jboss.wsf.spi.deployment.Endpoint;
import org.jboss.wsf.spi.invocation.Invocation;
import org.jboss.wsf.spi.invocation.InvocationContext;
import org.jboss.wsf.spi.invocation.InvocationHandler;
import org.jboss.wsf.spi.metadata.j2ee.serviceref.UnifiedHandlerMetaData.HandlerType;

/** An implementation handles invocations on the endpoint
 *
 * @author Thomas.Diesler@jboss.org
 * @since 25-Apr-2007
 */
public class ServiceEndpointInvoker
{
   // provide logging
   private static final Logger log = Logger.getLogger(ServiceEndpointInvoker.class);

   protected Endpoint endpoint;
   protected CommonBindingProvider bindingProvider;
   protected ServerHandlerDelegate delegate;

   /** Initialize the service endpoint */
   public void init(Endpoint endpoint)
   {
      this.endpoint = endpoint;

      ServerEndpointMetaData sepMetaData = endpoint.getAttachment(ServerEndpointMetaData.class);
      if (sepMetaData == null)
         throw NativeMessages.MESSAGES.cannotObtainEndpointMetaData(endpoint);

      bindingProvider = new CommonBindingProvider(sepMetaData);
      delegate = new HandlerDelegateJAXRPC(sepMetaData);
   }

   public boolean callRequestHandlerChain(ServerEndpointMetaData sepMetaData, HandlerType type)
   {
      return delegate.callRequestHandlerChain(sepMetaData, type);
   }

   public boolean callResponseHandlerChain(ServerEndpointMetaData sepMetaData, HandlerType type)
   {
      return delegate.callResponseHandlerChain(sepMetaData, type);
   }

   public void closeHandlerChain(ServerEndpointMetaData sepMetaData, HandlerType type)
   {
      delegate.closeHandlerChain(sepMetaData, type);
   }

   public boolean callFaultHandlerChain(ServerEndpointMetaData sepMetaData, HandlerType type, Exception ex)
   {
      return delegate.callFaultHandlerChain(sepMetaData, type, ex);
   }

   /** Invoke the the service endpoint */
   public void invoke(InvocationContext invContext) throws Exception
   {
      CommonMessageContext msgContext = MessageContextAssociation.peekMessageContext();
      ServerEndpointMetaData sepMetaData = (ServerEndpointMetaData)msgContext.getEndpointMetaData();
      SOAPMessage reqMessage = msgContext.getSOAPMessage();

      // The direction of the message
      DirectionHolder direction = new DirectionHolder(Direction.InBound);

      // Get the order of pre/post handlerchains 
      HandlerType[] handlerType = delegate.getHandlerTypeOrder();
      HandlerType[] faultType = delegate.getHandlerTypeOrder();

      try
      {
         boolean oneway = false;
         EndpointInvocation sepInv = null;
         OperationMetaData opMetaData = null;
         CommonBinding binding = bindingProvider.getCommonBinding();
         binding.setHeaderSource(delegate);

         // call the request handler chain
         boolean handlersPass = callRequestHandlerChain(sepMetaData, handlerType[0]);

         // Unbind the request message
         if (handlersPass)
         {
            // Get the operation meta data from the SOAP message
            opMetaData = getDispatchDestination(sepMetaData, reqMessage);
            msgContext.setOperationMetaData(opMetaData);
            oneway = opMetaData.isOneWay();

            /* 
             * From JAX-WS 10.2.1 - "7. If the node does not understand how to process
             * the message, then neither handlers nor the endpoint
             * are invoked and instead the binding generates a SOAP must
             * understand exception"
             *
             * Therefore, this must precede the ENDPOINT chain; however, The PRE
             * chain still must happen first since the message may be encrypted, in which
             * case the operation is still not known. Without knowing the operation, it 
             * is not possible to determine what headers are understood by the endpoint.
             */
            if (binding instanceof CommonSOAPBinding)
               ((CommonSOAPBinding)binding).checkMustUnderstand(opMetaData);

            // Unbind the request message
            sepInv = binding.unbindRequestMessage(opMetaData, reqMessage);
         }

         handlersPass = handlersPass && callRequestHandlerChain(sepMetaData, handlerType[1]);
         handlersPass = handlersPass && callRequestHandlerChain(sepMetaData, handlerType[2]);

         if (handlersPass)
         {
            // Check if protocol handlers modified the payload
            if (msgContext.isModified())
            {
               log.debug("Handler modified payload, unbind message again");
               reqMessage = msgContext.getSOAPMessage();
               sepInv = binding.unbindRequestMessage(opMetaData, reqMessage);
            }

            // Invoke an instance of the SEI implementation bean 
            Invocation inv = setupInvocation(endpoint, sepInv, invContext);
            InvocationHandler invHandler = endpoint.getInvocationHandler();

            try
            {
               invHandler.invoke(endpoint, inv);
            }
            catch (InvocationTargetException th)
            {
               //Unwrap the throwable raised by the service endpoint implementation
               Throwable targetEx = th.getTargetException();
               throw (targetEx instanceof Exception ? (Exception)targetEx : new UndeclaredThrowableException(targetEx));
            }

            // Handler processing might have replaced the endpoint invocation
            sepInv = inv.getInvocationContext().getAttachment(EndpointInvocation.class);

            // Reverse the message direction
            msgContext = processPivotInternal(msgContext, direction);

            // Bind the response message
            SOAPMessage resMessage = binding.bindResponseMessage(opMetaData, sepInv);
            msgContext.setSOAPMessage(resMessage);
         }
         else
         {
            // Reverse the message direction without calling the endpoint
            SOAPMessage resMessage = msgContext.getSOAPMessage();
            msgContext = processPivotInternal(msgContext, direction);
            msgContext.setSOAPMessage(resMessage);
         }

         if (oneway == false)
         {
            // call the  response handler chain, removing the fault type entry will not call handleFault for that chain 
            handlersPass = callResponseHandlerChain(sepMetaData, handlerType[2]);
            faultType[2] = null;
            handlersPass = handlersPass && callResponseHandlerChain(sepMetaData, handlerType[1]);
            faultType[1] = null;
            handlersPass = handlersPass && callResponseHandlerChain(sepMetaData, handlerType[0]);
            faultType[0] = null;
         }
      }
      catch (Exception ex)
      {
         // Reverse the message direction
         processPivotInternal(msgContext, direction);
         
         CommonBinding binding = bindingProvider.getCommonBinding();
         try
         {
            binding.bindFaultMessage(ex);

            // call the fault handler chain
            boolean handlersPass = true;
            if (faultType[2] != null)
               handlersPass = handlersPass && callFaultHandlerChain(sepMetaData, faultType[2], ex);
            if (faultType[1] != null)
               handlersPass = handlersPass && callFaultHandlerChain(sepMetaData, faultType[1], ex);
            if (faultType[0] != null)
               handlersPass = handlersPass && callFaultHandlerChain(sepMetaData, faultType[0], ex);
         }
         catch (RuntimeException subEx)
         {
            NativeLoggers.ROOT_LOGGER.exceptionProcessingHandleFault(ex);
            binding.bindFaultMessage(subEx);
            ex = subEx;
         }
         throw ex;
      }
      finally
      {
         closeHandlerChain(sepMetaData, handlerType[2]);
         closeHandlerChain(sepMetaData, handlerType[1]);
         closeHandlerChain(sepMetaData, handlerType[0]);
      }
   }

   protected Invocation setupInvocation(Endpoint ep, EndpointInvocation epInv, InvocationContext invContext) throws Exception
   {
      CommonMessageContext msgContext = MessageContextAssociation.peekMessageContext();
      if (msgContext instanceof SOAPMessageContextJAXRPC)
      {
         invContext.addAttachment(javax.xml.rpc.handler.MessageContext.class, msgContext);
      }
      if (ServiceLifecycle.class.isAssignableFrom(ep.getTargetBeanClass()) && invContext instanceof ServletRequestContext)
      {
         ServletEndpointContext servletEndpointContext = new ServletEndpointContextImpl((ServletRequestContext)invContext);
         invContext.addAttachment(ServletEndpointContext.class, servletEndpointContext);
      }

      invContext.addAttachment(EndpointInvocation.class, epInv);

      Invocation wsInv = new DelegatingInvocation();
      wsInv.setInvocationContext(invContext);
      wsInv.setJavaMethod(getImplMethod(endpoint, epInv));
      wsInv.getInvocationContext().setTargetBean(getEndpointInstance());

      return wsInv;
   }
   
   // JBWS-2486 - Only one webservice endpoint instance can be created!
   private Object getEndpointInstance()
   {
      synchronized(endpoint) 
      {
          try
          {
              final String endpointClassName = endpoint.getTargetBeanName();
              return endpoint.getInstanceProvider().getInstance(endpointClassName).getValue();
          }
          catch (Exception ex)
          {
              throw new IllegalStateException(ex);
          }
      }
   }

   protected Method getImplMethod(Endpoint endpoint, EndpointInvocation sepInv) throws ClassNotFoundException, NoSuchMethodException
   {
      Class implClass = endpoint.getTargetBeanClass();
      Method seiMethod = sepInv.getJavaMethod();
      Method implMethod = null;

      if (seiMethod != null)
      {
         String methodName = seiMethod.getName();
         Class[] paramTypes = seiMethod.getParameterTypes();
         for (int i = 0; i < paramTypes.length; i++)
         {
            Class paramType = paramTypes[i];
            if (JavaUtils.isPrimitive(paramType) == false)
            {
               String paramTypeName = paramType.getName();
               paramType = JavaUtils.loadJavaType(paramTypeName);
               paramTypes[i] = paramType;
            }
         }

         try
         {
            implMethod = implClass.getMethod(methodName, paramTypes);
         }
         catch (NoSuchMethodException ex)
         {
            throw ex;
         }
      }
      else
      {
         log.debug("RM method returned as null");
      }
      
      return implMethod;
   }

   private CommonMessageContext processPivotInternal(CommonMessageContext msgContext, DirectionHolder direction)
   {
      if (direction.getDirection() == Direction.InBound)
      {
         msgContext = MessageContextJAXRPC.processPivot(msgContext);
         direction.setDirection(Direction.OutBound);
      }
      return msgContext;
   }

   private OperationMetaData getDispatchDestination(EndpointMetaData epMetaData, SOAPMessage reqMessage) throws SOAPException
   {
      OperationMetaData opMetaData;

      SOAPMessage soapMessage = reqMessage;
      opMetaData = getOperationMetaData(epMetaData, soapMessage);
      SOAPHeader soapHeader = soapMessage.getSOAPHeader();

      // Report a MustUnderstand fault
      if (opMetaData == null)
      {
    	  String faultString;

    	  SOAPBodyImpl soapBody = (SOAPBodyImpl)soapMessage.getSOAPBody();
    	  SOAPBodyElement soapBodyElement = soapBody.getBodyElement();
    	  if (soapBodyElement != null)
    	  {
    		  Name soapName = soapBodyElement.getElementName();
    		  faultString = "Endpoint " + epMetaData.getPortName() + " does not contain operation meta data for: " + soapName;
    	  }
    	  else
    	  {
    		  faultString = "Endpoint " + epMetaData.getPortName() + " does not contain operation meta data for empty soap body";
    	  }

    	  // R2724 If an INSTANCE receives a message that is inconsistent with its WSDL description, it SHOULD generate a soap:Fault
    	  // with a faultcode of "Client", unless a "MustUnderstand" or "VersionMismatch" fault is generated.
    	  if (soapHeader != null && soapHeader.examineMustUnderstandHeaderElements(Constants.URI_SOAP11_NEXT_ACTOR).hasNext())
    	  {
    		  QName faultCode = Constants.SOAP11_FAULT_CODE_MUST_UNDERSTAND;
    		  throw new CommonSOAPFaultException(faultCode, faultString);
    	  }
    	  else
    	  {
    		  QName faultCode = Constants.SOAP11_FAULT_CODE_CLIENT;
    		  throw new CommonSOAPFaultException(faultCode, faultString);
    	  }
      }
      return opMetaData;
   }

   /** Get the operation meta data for this SOAP message
    */
   public OperationMetaData getOperationMetaData(EndpointMetaData epMetaData, SOAPMessage soapMsg) throws SOAPException
   {
      SOAPMessageDispatcher dispatcher = new SOAPMessageDispatcher();
      return dispatcher.getDispatchDestination(epMetaData, soapMsg);
   }


}
