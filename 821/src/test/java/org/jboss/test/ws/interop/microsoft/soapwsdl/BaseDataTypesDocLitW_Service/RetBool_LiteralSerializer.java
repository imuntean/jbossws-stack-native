// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.ws.interop.microsoft.soapwsdl.BaseDataTypesDocLitW_Service;

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

public class RetBool_LiteralSerializer extends LiteralObjectSerializerBase implements Initializable  {
    private static final javax.xml.namespace.QName ns1_inBool_QNAME = new QName("http://tempuri.org/", "inBool");
    private static final javax.xml.namespace.QName ns2_boolean_TYPE_QNAME = SchemaConstants.QNAME_TYPE_BOOLEAN;
    private CombinedSerializer ns2_myns2__boolean__java_lang_Boolean_Boolean_Serializer;
    
    public RetBool_LiteralSerializer(javax.xml.namespace.QName type, java.lang.String encodingStyle) {
        this(type, encodingStyle, false);
    }
    
    public RetBool_LiteralSerializer(javax.xml.namespace.QName type, java.lang.String encodingStyle, boolean encodeType) {
        super(type, true, encodingStyle, encodeType);
    }
    
    public void initialize(InternalTypeMappingRegistry registry) throws Exception {
        ns2_myns2__boolean__java_lang_Boolean_Boolean_Serializer = (CombinedSerializer)registry.getSerializer("", java.lang.Boolean.class, ns2_boolean_TYPE_QNAME);
    }
    
    public java.lang.Object doDeserialize(XMLReader reader,
        SOAPDeserializationContext context) throws java.lang.Exception {
        org.jboss.test.ws.interop.microsoft.soapwsdl.BaseDataTypesDocLitW_Service.RetBool instance = new org.jboss.test.ws.interop.microsoft.soapwsdl.BaseDataTypesDocLitW_Service.RetBool();
        java.lang.Object member=null;
        javax.xml.namespace.QName elementName;
        java.util.List values;
        java.lang.Object value;
        
        reader.nextElementContent();
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_inBool_QNAME)) {
                member = ns2_myns2__boolean__java_lang_Boolean_Boolean_Serializer.deserialize(ns1_inBool_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setInBool((java.lang.Boolean)member);
                reader.nextElementContent();
            }
        }
        
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (java.lang.Object)instance;
    }
    
    public void doSerializeAttributes(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        org.jboss.test.ws.interop.microsoft.soapwsdl.BaseDataTypesDocLitW_Service.RetBool instance = (org.jboss.test.ws.interop.microsoft.soapwsdl.BaseDataTypesDocLitW_Service.RetBool)obj;
        
    }
    public void doSerialize(java.lang.Object obj, XMLWriter writer, SOAPSerializationContext context) throws java.lang.Exception {
        org.jboss.test.ws.interop.microsoft.soapwsdl.BaseDataTypesDocLitW_Service.RetBool instance = (org.jboss.test.ws.interop.microsoft.soapwsdl.BaseDataTypesDocLitW_Service.RetBool)obj;
        
        if (instance.getInBool() != null) {
            ns2_myns2__boolean__java_lang_Boolean_Boolean_Serializer.serialize(instance.getInBool(), ns1_inBool_QNAME, null, writer, context);
        }
    }
}
