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
package org.jboss.ws.core.jaxws.spi.http;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;
import org.jboss.netty.handler.codec.http.CookieEncoder;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.ws.Constants;
import org.jboss.wsf.spi.invocation.InvocationContext;

/**
 * TODO: javadoc
 *
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 */
final class NettyInvocationHandler extends SimpleChannelUpstreamHandler
{
   private static final Logger LOG = Logger.getLogger(NettyInvocationHandler.class);
   private final List<NettyCallbackHandler> callbacks = new LinkedList<NettyCallbackHandler>();
   private final Lock lock = new ReentrantLock();

   public NettyInvocationHandler()
   {
      super();
   }
   
   @Override
   public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
   {
      // HERE: Add all accepted channels to the group
      //       so that they are closed properly on shutdown
      //       If the added channel is closed before shutdown,
      //       it will be removed from the group automatically.
      RealNettyHttpServer.channelGroup.add(ctx.getChannel());
   } 

   public boolean hasMoreCallbacks()
   {
      return this.callbacks.size() > 0;
   }

   @Override
   public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
   {
      HttpRequest request = (HttpRequest)e.getMessage();
      ChannelBuffer content = request.getContent();
      OutputStream baos = new ByteArrayOutputStream();
      OutputStream outputStream = new BufferedOutputStream(baos);
      Integer statusCode = null;
      
      InvocationContext invCtx = new InvocationContext();
      Map<String, List<String>> requestHeaders = new HashMap<String, List<String>>();
      Map<String, List<String>> responseHeaders = new HashMap<String, List<String>>();
      invCtx.setProperty(Constants.NETTY_REQUEST_HEADERS, requestHeaders);
      invCtx.setProperty(Constants.NETTY_RESPONSE_HEADERS, responseHeaders);
      for (String headerName : request.getHeaderNames())
      {
         requestHeaders.put(headerName, request.getHeaders(headerName));
      }
      try
      {
         String requestPath = request.getUri();
         int paramIndex = requestPath.indexOf('?');
         if (paramIndex != -1)
         {
            requestPath = requestPath.substring(0, paramIndex);
         }
         String httpMethod = request.getMethod().getName();
         statusCode = handle(requestPath, httpMethod, getInputStream(content), outputStream, invCtx);
      }
      catch (Throwable t)
      {
         statusCode = 500;
         LOG.error(t);
      }
      finally
      {
         writeResponse(e, request, baos.toString(), statusCode, responseHeaders);
      }
   }
   
   private InputStream getInputStream(ChannelBuffer content)
   {
      return new ChannelBufferInputStream(content);
   }
   
   private int handle(String requestPath, String httpMethod, InputStream inputStream, OutputStream outputStream, 
      InvocationContext invCtx) throws IOException
   {
      boolean handlerExists = false;
      String handledPath = null;
      requestPath = truncateHostName(requestPath);
      for (NettyCallbackHandler handler : this.callbacks)
      {
         handledPath = truncateHostName(handler.getHandledPath());
         if (requestPath.equals(handledPath))
         {
            handlerExists = true;
            if (LOG.isDebugEnabled())
               LOG.debug("Handling request path: " + requestPath);
            return handler.handle(httpMethod, inputStream, outputStream, invCtx);
         }
      }
      if (handlerExists == false)
         LOG.warn("No callback handler registered for path: " + requestPath);
      
      return 500;
   }
   
   private String truncateHostName(String s)
   {
      String retVal = s;
      if (s.startsWith("http"))
      {
         try
         {
            retVal = new URL(s).getPath();
         }
         catch (MalformedURLException mue)
         {
            LOG.error(mue.getMessage(), mue);
         }
      }
      
      while (retVal.endsWith("/"))
      {
         retVal = retVal.substring(0, retVal.length() - 1);
      }
      return retVal;
   }
   
   private void writeResponse(MessageEvent e, HttpRequest request, String content, int statusCode, Map<String, List<String>> responseHeaders)
   {
      // Build the response object.
      HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, getResponseStatus(statusCode));
      
      Iterator<String> iterator = responseHeaders.keySet().iterator();
      String key = null;
      List<String> values = null;
      while (iterator.hasNext())
      {
         key = iterator.next();
         values = responseHeaders.get(key);
         response.setHeader(key, values);
      }
      response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/xml; charset=UTF-8");
      response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(content.length()));
      response.setContent(ChannelBuffers.copiedBuffer(content, "UTF-8"));

      String cookieString = request.getHeader(HttpHeaders.Names.COOKIE);
      if (cookieString != null)
      {
         CookieDecoder cookieDecoder = new CookieDecoder();
         Set<Cookie> cookies = cookieDecoder.decode(cookieString);
         if (!cookies.isEmpty())
         {
            // Reset the cookies if necessary.
            CookieEncoder cookieEncoder = new CookieEncoder(true);
            for (Cookie cookie : cookies)
            {
               cookieEncoder.addCookie(cookie);
            }
            response.addHeader(HttpHeaders.Names.SET_COOKIE, cookieEncoder.encode());
         }
      }

      // Write the response.
      ChannelFuture cf = e.getChannel().write(response);
      cf.awaitUninterruptibly();
   }

   private HttpResponseStatus getResponseStatus(int statusCode)
   {
      // TODO: https://jira.jboss.org/jira/browse/NETTY-233 
      if (statusCode == 500) return HttpResponseStatus.INTERNAL_SERVER_ERROR;
      if (statusCode == 202) return HttpResponseStatus.ACCEPTED;
      if (statusCode == 204) return HttpResponseStatus.NO_CONTENT;
      
      return HttpResponseStatus.OK;
   }

   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception
   {
      e.getCause().printStackTrace();
      e.getChannel().close();
   }

   public NettyCallbackHandler getCallback(String requestPath)
   {
      this.lock.lock();
      try
      {
         for (NettyCallbackHandler handler : this.callbacks)
         {
            if (handler.getHandledPath().equals(requestPath))
               return handler;
         }
      }
      finally
      {
         this.lock.unlock();
      }

      return null;
   }

   public void registerCallback(NettyCallbackHandler callbackHandler)
   {
      this.lock.lock();
      try
      {
         this.callbacks.add(callbackHandler);
      }
      finally
      {
         this.lock.unlock();
      }
   }

   public void unregisterCallback(NettyCallbackHandler callbackHandler)
   {
      this.lock.lock();
      try
      {
         this.callbacks.remove(callbackHandler);
         callbackHandler.destroy();
      }
      finally
      {
         this.lock.unlock();
      }
   }
   
}
