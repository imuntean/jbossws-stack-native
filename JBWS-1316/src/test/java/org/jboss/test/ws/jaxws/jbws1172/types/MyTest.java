
package org.jboss.test.ws.jaxws.jbws1172.types;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.1-b03-
 * Generated source version: 2.0
 * 
 */
@WebService(name = "MyTest", targetNamespace = "http://www.my-company.it/ws/my-test")
public interface MyTest {


    /**
     * 
     * @param code
     * @throws MyWSException_Exception
     */
    @WebMethod(action = "urn:performTest")
    @RequestWrapper(localName = "performTest", targetNamespace = "http://www.my-company.it/ws/my-test", className = "org.jboss.test.ws.jaxws.jbws1172.types.PerformTest")
    @ResponseWrapper(localName = "performTestResponse", targetNamespace = "http://www.my-company.it/ws/my-test", className = "org.jboss.test.ws.jaxws.jbws1172.types.PerformTestResponse")
    public void performTest(
        @WebParam(name = "Code", targetNamespace = "http://www.my-company.it/ws/my-test")
        Long code)
        throws MyWSException_Exception
    ;

}
