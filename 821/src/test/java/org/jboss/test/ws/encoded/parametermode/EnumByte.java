// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.2_01, build R40)
// Generated source version: 1.1.2

package org.jboss.test.ws.encoded.parametermode;


import java.util.Map;
import java.util.HashMap;

public class EnumByte {
    private byte value;
    private static Map valueMap = new HashMap();
    public static final byte _value1 = (byte)-128;
    public static final byte _value2 = (byte)127;
    
    public static final EnumByte value1 = new EnumByte(_value1);
    public static final EnumByte value2 = new EnumByte(_value2);
    
    protected EnumByte(byte value) {
        this.value = value;
        valueMap.put(this.toString(), this);
    }
    
    public byte getValue() {
        return value;
    }
    
    public static EnumByte fromValue(byte value)
        throws java.lang.IllegalStateException {
        if (value1.value == value) {
            return value1;
        } else if (value2.value == value) {
            return value2;
        }
        throw new IllegalArgumentException();
    }
    
    public static EnumByte fromString(String value)
        throws java.lang.IllegalStateException {
        EnumByte ret = (EnumByte)valueMap.get(value);
        if (ret != null) {
            return ret;
        }
        if (value.equals("-128")) {
            return value1;
        } else if (value.equals("127")) {
            return value2;
        }
        throw new IllegalArgumentException();
    }
    
    public String toString() {
        return new Byte(value).toString();
    }
    
    private Object readResolve()
        throws java.io.ObjectStreamException {
        return fromValue(getValue());
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof EnumByte)) {
            return false;
        }
        return ((EnumByte)obj).value == value;
    }
    
    public int hashCode() {
        return new Byte(value).toString().hashCode();
    }
}
