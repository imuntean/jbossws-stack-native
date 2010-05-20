// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.ws.interop.microsoft.soapwsdl.BaseDataTypesRpcLit_Service;

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

public class IBaseDataTypesRpcLit_RetSByte_ResponseStruct_LiteralSerializer extends LiteralObjectSerializerBase implements Initializable  {
    private static final javax.xml.namespace.QName ns1_RetSByteResult_QNAME = new QName("", "RetSByteResult");
    private static final javax.xml.namespace.QName ns2_byte_TYPE_QNAME = SchemaConstants.QNAME_TYPE_BYTE;
    private CombinedSerializer ns2_myns2__byte__byte_Byte_Serializer;
    
    public IBaseDataTypesRpcLit_RetSByte_ResponseStruct_LiteralSerializer(javax.xml.namespace.QName type, java.lang.String encodingStyle) {
        this(type, encodingStyle, false);
    }
    
    public IBaseDataTypesRpcLit_RetSByte_ResponseStruct_LiteralSerializer(javax.xml.namespace.QName type, java.lang.String encodingStyle, boolean encodeType) {
        super(type, true, encodingStyle, encodeType);
    }
    
    public void initialize(InternalTypeMappingRegistry registry) throws Exception {
        ns2_myns2__byte__byte_Byte_Serializer = (CombinedSerializer)registry.getSerializer("", byte.class, ns2_byte_TYPE_QNAME);
    }
    
    public java.lang.Object doDeserialize(XMLReader reader,
        SOAPDeserializationContext context) throws java.lang.Exception {
        org.jboss.test.ws.interop.microsoft.soapwsdl.BaseDataTypesRpcLit_Service.IBaseDataTypesRpcLit_RetSByte_ResponseStruct instance = new org.jboss.test.ws.interop.microsoft.soapwsdl.BaseDataTypesRpcLit_Service.IBaseDataTypesRpcLit_RetSByte_ResponseStruct();
        java.lang.Object member=null;
        javax.xml.namespace.QName elementName;
        java.util.List values;
        java.lang.Object value;
        
        reader.nextElementContent();
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_RetSByteResult_QNAME)) {
                member = ns2_myns2__byte__byte_Byte_Serializer.deserialize(ns1_RetSByteResult_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setRetSByteResult(((Byte)member).byteValue());
                reader.nextElementContent();
            } else {
                throw new DeserializationException("literal.unexpectedElementName", new Object[] { ns1_RetSByteResult_QNAME, reader.getName() });
            }
        }
        else {
            throw new DeserializationException("literal.expectedElementName", reader.getName().toString());
        }
        
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (java.lang.Object)instance;
    }
    
    public void doSerializeAttributes(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        org.jboss.test.ws.interop.microsoft.soapwsdl.BaseDataTypesRpcLit_Service.IBaseDataTypesRpcLit_RetSByte_ResponseStruct instance = (org.jboss.test.ws.interop.microsoft.soapwsdl.BaseDataTypesRpcLit_Service.IBaseDataTypesRpcLit_RetSByte_ResponseStruct)obj;
        
    }
    public void doSerialize(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        org.jboss.test.ws.interop.microsoft.soapwsdl.BaseDataTypesRpcLit_Service.IBaseDataTypesRpcLit_RetSByte_ResponseStruct instance = (org.jboss.test.ws.interop.microsoft.soapwsdl.BaseDataTypesRpcLit_Service.IBaseDataTypesRpcLit_RetSByte_ResponseStruct)obj;
        
        if (new Byte(instance.getRetSByteResult()) == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns2_myns2__byte__byte_Byte_Serializer.serialize(new Byte(instance.getRetSByteResult()), ns1_RetSByteResult_QNAME, null, writer, context);
    }
}
