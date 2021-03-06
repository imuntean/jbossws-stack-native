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
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.ws.jaxrpc.encoded.marshalltest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;

import org.jboss.ws.common.JavaUtils;

public class JavaBean
{
   protected BigDecimal myBigDecimal;
   protected BigDecimal[] myBigDecimalArray;
   protected BigInteger myBigInteger;
   protected BigInteger[] myBigIntegerArray;
   protected boolean myBoolean;
   protected Boolean myBoolean1;
   protected Boolean[] myBoolean1Array;
   protected boolean[] myBooleanArray;
   protected byte myByte;
   protected Byte myByte1;
   protected byte[] myByteArray;
   protected Calendar myCalendar;
   protected Calendar[] myCalendarArray;
   protected double myDouble;
   protected Double myDouble1;
   protected Double[] myDouble1Array;
   protected double[] myDoubleArray;
   protected float myFloat;
   protected Float myFloat1;
   protected Float[] myFloat1Array;
   protected float[] myFloatArray;
   protected int myInt;
   protected Integer myInt1;
   protected Integer[] myInt1Array;
   protected int[] myIntArray;
   protected long myLong;
   protected Long myLong1;
   protected Long[] myLong1Array;
   protected long[] myLongArray;
   protected short myShort;
   protected Short myShort1;
   protected Short[] myShort1Array;
   protected short[] myShortArray;
   protected String myString;
   protected String[] myStringArray;

   public JavaBean()
   {
   }

   public JavaBean(BigDecimal myBigDecimal, BigDecimal[] myBigDecimalArray, BigInteger myBigInteger, BigInteger[] myBigIntegerArray, boolean myBoolean,
                   Boolean myBoolean1, Boolean[] myBoolean1Array, boolean[] myBooleanArray, byte myByte, Byte myByte1, byte[] myByteArray, Calendar myCalendar,
                   Calendar[] myCalendarArray, double myDouble, Double myDouble1, Double[] myDouble1Array, double[] myDoubleArray, float myFloat, Float myFloat1,
                   Float[] myFloat1Array, float[] myFloatArray, int myInt, Integer myInt1, Integer[] myInt1Array, int[] myIntArray, long myLong, Long myLong1,
                   Long[] myLong1Array, long[] myLongArray, short myShort, Short myShort1, Short[] myShort1Array, short[] myShortArray, String myString, String[] myStringArray)
   {
      this.myBigDecimal = myBigDecimal;
      this.myBigDecimalArray = myBigDecimalArray;
      this.myBigInteger = myBigInteger;
      this.myBigIntegerArray = myBigIntegerArray;
      this.myBoolean = myBoolean;
      this.myBoolean1 = myBoolean1;
      this.myBoolean1Array = myBoolean1Array;
      this.myBooleanArray = myBooleanArray;
      this.myByte = myByte;
      this.myByte1 = myByte1;
      this.myByteArray = myByteArray;
      this.myCalendar = myCalendar;
      this.myCalendarArray = myCalendarArray;
      this.myDouble = myDouble;
      this.myDouble1 = myDouble1;
      this.myDouble1Array = myDouble1Array;
      this.myDoubleArray = myDoubleArray;
      this.myFloat = myFloat;
      this.myFloat1 = myFloat1;
      this.myFloat1Array = myFloat1Array;
      this.myFloatArray = myFloatArray;
      this.myInt = myInt;
      this.myInt1 = myInt1;
      this.myInt1Array = myInt1Array;
      this.myIntArray = myIntArray;
      this.myLong = myLong;
      this.myLong1 = myLong1;
      this.myLong1Array = myLong1Array;
      this.myLongArray = myLongArray;
      this.myShort = myShort;
      this.myShort1 = myShort1;
      this.myShort1Array = myShort1Array;
      this.myShortArray = myShortArray;
      this.myString = myString;
      this.myStringArray = myStringArray;
   }

   public BigDecimal getMyBigDecimal()
   {
      return myBigDecimal;
   }

   public void setMyBigDecimal(BigDecimal myBigDecimal)
   {
      this.myBigDecimal = myBigDecimal;
   }

   public BigDecimal[] getMyBigDecimalArray()
   {
      return myBigDecimalArray;
   }

   public void setMyBigDecimalArray(BigDecimal[] myBigDecimalArray)
   {
      this.myBigDecimalArray = myBigDecimalArray;
   }

   public BigInteger getMyBigInteger()
   {
      return myBigInteger;
   }

   public void setMyBigInteger(BigInteger myBigInteger)
   {
      this.myBigInteger = myBigInteger;
   }

   public BigInteger[] getMyBigIntegerArray()
   {
      return myBigIntegerArray;
   }

   public void setMyBigIntegerArray(BigInteger[] myBigIntegerArray)
   {
      this.myBigIntegerArray = myBigIntegerArray;
   }

   public boolean isMyBoolean()
   {
      return myBoolean;
   }

   public void setMyBoolean(boolean myBoolean)
   {
      this.myBoolean = myBoolean;
   }

   public Boolean getMyBoolean1()
   {
      return myBoolean1;
   }

   public void setMyBoolean1(Boolean myBoolean1)
   {
      this.myBoolean1 = myBoolean1;
   }

   public Boolean[] getMyBoolean1Array()
   {
      return myBoolean1Array;
   }

   public void setMyBoolean1Array(Boolean[] myBoolean1Array)
   {
      this.myBoolean1Array = myBoolean1Array;
   }

   public boolean[] getMyBooleanArray()
   {
      return myBooleanArray;
   }

   public void setMyBooleanArray(boolean[] myBooleanArray)
   {
      this.myBooleanArray = myBooleanArray;
   }

   public byte getMyByte()
   {
      return myByte;
   }

   public void setMyByte(byte myByte)
   {
      this.myByte = myByte;
   }

   public Byte getMyByte1()
   {
      return myByte1;
   }

   public void setMyByte1(Byte myByte1)
   {
      this.myByte1 = myByte1;
   }

   public byte[] getMyByteArray()
   {
      return myByteArray;
   }

   public void setMyByteArray(byte[] myByteArray)
   {
      this.myByteArray = myByteArray;
   }

   public Calendar getMyCalendar()
   {
      return myCalendar;
   }

   public void setMyCalendar(Calendar myCalendar)
   {
      this.myCalendar = myCalendar;
   }

   public Calendar[] getMyCalendarArray()
   {
      return myCalendarArray;
   }

   public void setMyCalendarArray(Calendar[] myCalendarArray)
   {
      this.myCalendarArray = myCalendarArray;
   }

   public double getMyDouble()
   {
      return myDouble;
   }

   public void setMyDouble(double myDouble)
   {
      this.myDouble = myDouble;
   }

   public Double getMyDouble1()
   {
      return myDouble1;
   }

   public void setMyDouble1(Double myDouble1)
   {
      this.myDouble1 = myDouble1;
   }

   public Double[] getMyDouble1Array()
   {
      return myDouble1Array;
   }

   public void setMyDouble1Array(Double[] myDouble1Array)
   {
      this.myDouble1Array = myDouble1Array;
   }

   public double[] getMyDoubleArray()
   {
      return myDoubleArray;
   }

   public void setMyDoubleArray(double[] myDoubleArray)
   {
      this.myDoubleArray = myDoubleArray;
   }

   public float getMyFloat()
   {
      return myFloat;
   }

   public void setMyFloat(float myFloat)
   {
      this.myFloat = myFloat;
   }

   public Float getMyFloat1()
   {
      return myFloat1;
   }

   public void setMyFloat1(Float myFloat1)
   {
      this.myFloat1 = myFloat1;
   }

   public Float[] getMyFloat1Array()
   {
      return myFloat1Array;
   }

   public void setMyFloat1Array(Float[] myFloat1Array)
   {
      this.myFloat1Array = myFloat1Array;
   }

   public float[] getMyFloatArray()
   {
      return myFloatArray;
   }

   public void setMyFloatArray(float[] myFloatArray)
   {
      this.myFloatArray = myFloatArray;
   }

   public int getMyInt()
   {
      return myInt;
   }

   public void setMyInt(int myInt)
   {
      this.myInt = myInt;
   }

   public Integer getMyInt1()
   {
      return myInt1;
   }

   public void setMyInt1(Integer myInt1)
   {
      this.myInt1 = myInt1;
   }

   public Integer[] getMyInt1Array()
   {
      return myInt1Array;
   }

   public void setMyInt1Array(Integer[] myInt1Array)
   {
      this.myInt1Array = myInt1Array;
   }

   public int[] getMyIntArray()
   {
      return myIntArray;
   }

   public void setMyIntArray(int[] myIntArray)
   {
      this.myIntArray = myIntArray;
   }

   public long getMyLong()
   {
      return myLong;
   }

   public void setMyLong(long myLong)
   {
      this.myLong = myLong;
   }

   public Long getMyLong1()
   {
      return myLong1;
   }

   public void setMyLong1(Long myLong1)
   {
      this.myLong1 = myLong1;
   }

   public Long[] getMyLong1Array()
   {
      return myLong1Array;
   }

   public void setMyLong1Array(Long[] myLong1Array)
   {
      this.myLong1Array = myLong1Array;
   }

   public long[] getMyLongArray()
   {
      return myLongArray;
   }

   public void setMyLongArray(long[] myLongArray)
   {
      this.myLongArray = myLongArray;
   }

   public short getMyShort()
   {
      return myShort;
   }

   public void setMyShort(short myShort)
   {
      this.myShort = myShort;
   }

   public Short getMyShort1()
   {
      return myShort1;
   }

   public void setMyShort1(Short myShort1)
   {
      this.myShort1 = myShort1;
   }

   public Short[] getMyShort1Array()
   {
      return myShort1Array;
   }

   public void setMyShort1Array(Short[] myShort1Array)
   {
      this.myShort1Array = myShort1Array;
   }

   public short[] getMyShortArray()
   {
      return myShortArray;
   }

   public void setMyShortArray(short[] myShortArray)
   {
      this.myShortArray = myShortArray;
   }

   public String getMyString()
   {
      return myString;
   }

   public void setMyString(String myString)
   {
      this.myString = myString;
   }

   public String[] getMyStringArray()
   {
      return myStringArray;
   }

   public void setMyStringArray(String[] myStringArray)
   {
      this.myStringArray = myStringArray;
   }

   public boolean equals(Object obj)
   {
      return toString().equals("" + obj);
   }

   public String toString()
   {
      StringBuffer buf = new StringBuffer("[");
      buf.append("myBigDecimal=" + myBigDecimal);
      buf.append(",myBigDecimalArray=" + (myBigDecimalArray != null ? Arrays.asList(myBigDecimalArray) : null));
      buf.append(",myBigInteger=" + myBigInteger);
      buf.append(",myBigIntegerArray=" + (myBigIntegerArray != null ? Arrays.asList(myBigIntegerArray) : null));
      buf.append(",myBoolean=" + myBoolean);
      buf.append(",myBoolean1=" + myBoolean1);
      buf.append(",myBoolean1Array=" + (myBoolean1Array != null ? Arrays.asList(myBoolean1Array) : null));
      buf.append(",myBooleanArray=" + (myBooleanArray != null ? Arrays.asList((Object[])JavaUtils.getWrapperValueArray(myBooleanArray)) : null));
      buf.append(",myByte=" + myByte);
      buf.append(",myByte1=" + myByte1);
      buf.append(",myByteArray=" + (myByteArray != null ? Arrays.asList((Object[])JavaUtils.getWrapperValueArray(myByteArray)) : null));
      buf.append(",myCalendar=" + myCalendar);
      buf.append(",myCalendarArray=" + (myCalendarArray != null ? Arrays.asList(myCalendarArray) : null));
      buf.append(",myDouble=" + myDouble);
      buf.append(",myDouble1=" + myDouble1);
      buf.append(",myDouble1Array=" + (myDouble1Array != null ? Arrays.asList(myDouble1Array) : null));
      buf.append(",myDoubleArray=" + (myDoubleArray != null ? Arrays.asList((Object[])JavaUtils.getWrapperValueArray(myDoubleArray)) : null));
      buf.append(",myFloat=" + myFloat);
      buf.append(",myFloat1=" + myFloat1);
      buf.append(",myFloat1Array=" + (myFloat1Array != null ? Arrays.asList(myFloat1Array) : null));
      buf.append(",myFloatArray=" + (myFloatArray != null ? Arrays.asList((Object[])JavaUtils.getWrapperValueArray(myFloatArray)) : null));
      buf.append(",myInt=" + myInt);
      buf.append(",myInt1=" + myInt1);
      buf.append(",myInt1Array=" + (myInt1Array != null ? Arrays.asList(myInt1Array) : null));
      buf.append(",myIntArray=" + (myIntArray != null ? Arrays.asList((Object[])JavaUtils.getWrapperValueArray(myIntArray)) : null));
      buf.append(",myLong=" + myLong);
      buf.append(",myLong1=" + myLong1);
      buf.append(",myLong1Array=" + (myLong1Array != null ? Arrays.asList(myLong1Array) : null));
      buf.append(",myLongArray=" + (myLongArray != null ? Arrays.asList((Object[]) JavaUtils.getWrapperValueArray(myLongArray)) : null));
      buf.append(",myShort=" + myShort);
      buf.append(",myShort1=" + myShort1);
      buf.append(",myShort1Array=" + (myShort1Array != null ? Arrays.asList(myShort1Array) : null));
      buf.append(",myShortArray=" + (myShortArray != null ? Arrays.asList((Object[])JavaUtils.getWrapperValueArray(myShortArray)) : null));
      buf.append(",myString=" + myString);
      buf.append(",myStringArray=" + (myStringArray != null ? Arrays.asList(myStringArray) : null));
      buf.append("]");
      return buf.toString();
   }
}
