// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.2_01, build R40)
// Generated source version: 1.1.2

package org.jboss.test.ws.encoded.parametermode;


import java.util.Map;
import java.util.HashMap;

public class EnumShort {
    private short value;
    private static Map valueMap = new HashMap();
    public static final short _value1 = (short)-32768;
    public static final short _value2 = (short)32767;
    
    public static final EnumShort value1 = new EnumShort(_value1);
    public static final EnumShort value2 = new EnumShort(_value2);
    
    protected EnumShort(short value) {
        this.value = value;
        valueMap.put(this.toString(), this);
    }
    
    public short getValue() {
        return value;
    }
    
    public static EnumShort fromValue(short value)
        throws java.lang.IllegalStateException {
        if (value1.value == value) {
            return value1;
        } else if (value2.value == value) {
            return value2;
        }
        throw new IllegalArgumentException();
    }
    
    public static EnumShort fromString(String value)
        throws java.lang.IllegalStateException {
        EnumShort ret = (EnumShort)valueMap.get(value);
        if (ret != null) {
            return ret;
        }
        if (value.equals("-32768")) {
            return value1;
        } else if (value.equals("32767")) {
            return value2;
        }
        throw new IllegalArgumentException();
    }
    
    public String toString() {
        return new Short(value).toString();
    }
    
    private Object readResolve()
        throws java.io.ObjectStreamException {
        return fromValue(getValue());
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof EnumShort)) {
            return false;
        }
        return ((EnumShort)obj).value == value;
    }
    
    public int hashCode() {
        return new Short(value).toString().hashCode();
    }
}
