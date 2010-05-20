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
package org.jboss.test.ws.samples.holder;

import javax.xml.rpc.holders.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * A service endpoint interface for the HolderTestCase
 *
 * @author Thomas.Diesler@jboss.org
 * @since 22-Dec-2004
 */
public interface HolderTestService extends Remote
{
   void echoBigDecimal(BigDecimalHolder val) throws RemoteException;

   void echoBigInteger(BigIntegerHolder val) throws RemoteException;

   void echoBoolean(BooleanHolder val) throws RemoteException;

   void echoBooleanWrapper(BooleanWrapperHolder val) throws RemoteException;

   void echoByteArray(ByteArrayHolder val) throws RemoteException;

   void echoByte(ByteHolder val) throws RemoteException;

   void echoByteWrapper(ByteWrapperHolder val) throws RemoteException;

   void echoCalendar(CalendarHolder val) throws RemoteException;

   void echoDouble(DoubleHolder val) throws RemoteException;

   void echoDoubleWrapper(DoubleWrapperHolder val) throws RemoteException;

   void echoFloat(FloatHolder val) throws RemoteException;

   void echoFloatWrapper(FloatWrapperHolder val) throws RemoteException;

   void echoIntegerWrapper(IntegerWrapperHolder val) throws RemoteException;

   void echoInt(IntHolder val) throws RemoteException;

   void echoLong(LongHolder val) throws RemoteException;

   void echoLongWrapper(LongWrapperHolder val) throws RemoteException;

   /*
   void echoObject(ObjectHolder val) throws RemoteException;
   */

   void echoQName(QNameHolder val) throws RemoteException;

   void echoShort(ShortHolder val) throws RemoteException;

   void echoShortWrapper(ShortWrapperHolder val) throws RemoteException;

   void echoString(StringHolder val) throws RemoteException;
}
