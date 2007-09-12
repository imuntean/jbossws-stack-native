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
package org.jboss.ws.extensions.wsrm.spec200502;

import org.jboss.ws.extensions.wsrm.ReliableMessagingException;
import org.jboss.ws.extensions.wsrm.common.serialization.AbstractSerializable;
import org.jboss.ws.extensions.wsrm.spi.Provider;
import org.jboss.ws.extensions.wsrm.spi.protocol.SequenceFault;
import org.jboss.ws.extensions.wsrm.spi.protocol.SequenceFaultCode;

/*
 * @author richard.opalka@jboss.com
 * @see org.jboss.ws.extensions.wsrm.spi.protocol.SequenceFault
 */
final class SequenceFaultImpl extends AbstractSerializable implements SequenceFault
{
   
   // provider used by de/serialization framework
   private static final Provider PROVIDER = ProviderImpl.getInstance();
   // internal fields
   private SequenceFaultCode faultCode;
   private Exception detail;

   SequenceFaultImpl()
   {
      // allow inside package use only
   }
   
   /*
    * @see org.jboss.ws.extensions.wsrm.spi.protocol.SequenceFault#getDetail()
    */
   public Exception getDetail()
   {
      return this.detail;
   }

   /*
    * @see org.jboss.ws.extensions.wsrm.spi.protocol.SequenceFault#getFaultCode()
    */
   public SequenceFaultCode getFaultCode()
   {
      return this.faultCode;
   }

   /*
    * @see org.jboss.ws.extensions.wsrm.spi.protocol.SequenceFault#setDetail(java.lang.Exception)
    */
   public void setDetail(Exception detail)
   {
      if (detail == null)
         throw new IllegalArgumentException("Detail cannot be null");
      if (this.detail != null)
         throw new UnsupportedOperationException("Value already set, cannot be overriden");

      this.detail = detail;
   }

   /*
    * @see org.jboss.ws.extensions.wsrm.spi.protocol.SequenceFault#setFaultCode(org.jboss.ws.extensions.wsrm.spi.protocol.SequenceFaultCode)
    */
   public void setFaultCode(SequenceFaultCode faultCode)
   {
      if (faultCode == null)
         throw new IllegalArgumentException("Fault code cannot be null");
      if (this.faultCode != null)
         throw new UnsupportedOperationException("Value already set, cannot be overriden");

      this.faultCode = faultCode;
   }

   /*
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((detail == null) ? 0 : detail.getMessage().hashCode());
      result = prime * result + ((faultCode == null) ? 0 : faultCode.hashCode());
      return result;
   }

   /*
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (!(obj instanceof SequenceFaultImpl))
         return false;
      final SequenceFaultImpl other = (SequenceFaultImpl)obj;
      if (detail == null)
      {
         if (other.detail != null)
            return false;
      }
      else if (!detail.getMessage().equals(other.detail.getMessage()))
         return false;
      if (faultCode == null)
      {
         if (other.faultCode != null)
            return false;
      }
      else if (!faultCode.equals(other.faultCode))
         return false;
      return true;
   }
   
   public Provider getProvider()
   {
      return PROVIDER;
   }

   public void validate()
   {
      if (this.faultCode == null)
         throw new ReliableMessagingException("FaultCode must be set");
   }

}
