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

// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.2_01, build R40)

package org.jboss.test.ws.common.jbossxb.docstyle;


public class EchoString_ResponseStruct
{
   protected String result;

   public EchoString_ResponseStruct()
   {
   }

   public EchoString_ResponseStruct(String result)
   {
      this.result = result;
   }

   public String getResult()
   {
      return result;
   }

   public void setResult(String result)
   {
      this.result = result;
   }

   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (!(o instanceof EchoString_ResponseStruct)) return false;

      final EchoString_ResponseStruct echoString_responseStruct = (EchoString_ResponseStruct)o;

      if (result != null ? !result.equals(echoString_responseStruct.result) : echoString_responseStruct.result != null) return false;

      return true;
   }

   public int hashCode()
   {
      return (result != null ? result.hashCode() : 0);
   }

   public String toString()
   {
      return "[result=" + result + "]";
   }
}
