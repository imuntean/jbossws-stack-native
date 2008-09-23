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
// Generated source version: 1.1.2

package org.jboss.test.ws.jaxrpc.encoded.parametermode;


import java.util.HashMap;
import java.util.Map;

public class EnumString {
    private java.lang.String value;
    private static Map valueMap = new HashMap();
    public static final String _String1String = "String1";
    public static final String _String2String = "String2";
    
    public static final java.lang.String _String1 = new java.lang.String(_String1String);
    public static final java.lang.String _String2 = new java.lang.String(_String2String);
    
    public static final EnumString String1 = new EnumString(_String1);
    public static final EnumString String2 = new EnumString(_String2);
    
    protected EnumString(java.lang.String value) {
        this.value = value;
        valueMap.put(this.toString(), this);
    }
    
    public java.lang.String getValue() {
        return value;
    }
    
    public static EnumString fromValue(java.lang.String value)
        throws java.lang.IllegalStateException {
        if (String1.value.equals(value)) {
            return String1;
        } else if (String2.value.equals(value)) {
            return String2;
        }
        throw new IllegalArgumentException();
    }
    
    public static EnumString fromString(String value)
        throws java.lang.IllegalStateException {
        EnumString ret = (EnumString)valueMap.get(value);
        if (ret != null) {
            return ret;
        }
        if (value.equals(_String1String)) {
            return String1;
        } else if (value.equals(_String2String)) {
            return String2;
        }
        throw new IllegalArgumentException();
    }
    
    public String toString() {
        return value.toString();
    }
    
    private Object readResolve()
        throws java.io.ObjectStreamException {
        return fromValue(getValue());
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof EnumString)) {
            return false;
        }
        return ((EnumString)obj).value.equals(value);
    }
    
    public int hashCode() {
        return value.hashCode();
    }
}
