// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.ws.interop.microsoft.soapwsdl.ComplexDataTypesShared;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.xsd.XSDConstants;
import com.sun.xml.rpc.encoding.literal.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;
import java.util.List;
import java.util.ArrayList;

public class ArrayOfArrayOfstring_LiteralSerializer extends LiteralObjectSerializerBase implements Initializable  {
    private static final javax.xml.namespace.QName ns1_ArrayOfstring_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/Arrays", "ArrayOfstring");
    private static final javax.xml.namespace.QName ns1_ArrayOfstring_TYPE_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/Arrays", "ArrayOfstring");
    private CombinedSerializer ns1_myArrayOfstring_LiteralSerializer;
    
    public ArrayOfArrayOfstring_LiteralSerializer(javax.xml.namespace.QName type, java.lang.String encodingStyle) {
        this(type, encodingStyle, false);
    }
    
    public ArrayOfArrayOfstring_LiteralSerializer(javax.xml.namespace.QName type, java.lang.String encodingStyle, boolean encodeType) {
        super(type, true, encodingStyle, encodeType);
    }
    
    public void initialize(InternalTypeMappingRegistry registry) throws Exception {
        ns1_myArrayOfstring_LiteralSerializer = (CombinedSerializer)registry.getSerializer("", org.jboss.test.ws.interop.microsoft.soapwsdl.ComplexDataTypesShared.ArrayOfstring.class, ns1_ArrayOfstring_TYPE_QNAME);
    }
    
    public java.lang.Object doDeserialize(XMLReader reader,
        SOAPDeserializationContext context) throws java.lang.Exception {
        org.jboss.test.ws.interop.microsoft.soapwsdl.ComplexDataTypesShared.ArrayOfArrayOfstring instance = new org.jboss.test.ws.interop.microsoft.soapwsdl.ComplexDataTypesShared.ArrayOfArrayOfstring();
        java.lang.Object member=null;
        javax.xml.namespace.QName elementName;
        java.util.List values;
        java.lang.Object value;
        
        reader.nextElementContent();
        elementName = reader.getName();
        if ((reader.getState() == XMLReader.START) && (elementName.equals(ns1_ArrayOfstring_QNAME))) {
            values = new ArrayList();
            for(;;) {
                elementName = reader.getName();
                if ((reader.getState() == XMLReader.START) && (elementName.equals(ns1_ArrayOfstring_QNAME))) {
                    value = ns1_myArrayOfstring_LiteralSerializer.deserialize(ns1_ArrayOfstring_QNAME, reader, context);
                    values.add(value);
                    reader.nextElementContent();
                } else {
                    break;
                }
            }
            member = new org.jboss.test.ws.interop.microsoft.soapwsdl.ComplexDataTypesShared.ArrayOfstring[values.size()];
            member = values.toArray((Object[]) member);
            instance.setArrayOfstring((org.jboss.test.ws.interop.microsoft.soapwsdl.ComplexDataTypesShared.ArrayOfstring[])member);
        }
        else {
            instance.setArrayOfstring(new org.jboss.test.ws.interop.microsoft.soapwsdl.ComplexDataTypesShared.ArrayOfstring[0]);
        }
        
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (java.lang.Object)instance;
    }
    
    public void doSerializeAttributes(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        org.jboss.test.ws.interop.microsoft.soapwsdl.ComplexDataTypesShared.ArrayOfArrayOfstring instance = (org.jboss.test.ws.interop.microsoft.soapwsdl.ComplexDataTypesShared.ArrayOfArrayOfstring)obj;
        
    }
    public void doSerialize(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        org.jboss.test.ws.interop.microsoft.soapwsdl.ComplexDataTypesShared.ArrayOfArrayOfstring instance = (org.jboss.test.ws.interop.microsoft.soapwsdl.ComplexDataTypesShared.ArrayOfArrayOfstring)obj;
        
        if (instance.getArrayOfstring() != null) {
            for (int i = 0; i < instance.getArrayOfstring().length; ++i) {
                ns1_myArrayOfstring_LiteralSerializer.serialize(instance.getArrayOfstring()[i], ns1_ArrayOfstring_QNAME, null, writer, context);
            }
        }
    }
}
