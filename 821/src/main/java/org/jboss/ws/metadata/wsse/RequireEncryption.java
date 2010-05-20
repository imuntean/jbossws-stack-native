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
package org.jboss.ws.metadata.wsse;

/**
 * <code>Sign</code> represents the sign tag, which declares that a message
 * should be signed.
 *
 * @author <a href="mailto:jason.greene@jboss.com">Jason T. Greene</a>
 * @version $Revision$
 */
public class RequireEncryption extends Targetable
{
   private static final long serialVersionUID = 3765798680988205647L;

   private boolean includeFaults;

   public RequireEncryption(boolean includeFaults)
   {
      this.includeFaults = includeFaults;
   }

   public boolean isIncludeFaults()
   {
      return includeFaults;
   }

   public void setIncludeFaults(boolean includeFaults)
   {
      this.includeFaults = includeFaults;
   }
}
