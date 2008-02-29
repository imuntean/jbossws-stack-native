
package org.jboss.test.ws.jaxws.jbws1172.types;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.1-b03-
 * Generated source version: 2.0
 * 
 */
@WebServiceClient(name = "MyTestService", targetNamespace = "http://www.my-company.it/ws/my-test", wsdlLocation = "file:/home/tdiesler/svn/jbossws/stack/native/trunk/src/test/resources/jaxws/jbws1172/TestService.wsdl")
public class MyTestService
    extends Service
{

    private final static URL MYTESTSERVICE_WSDL_LOCATION;

    static {
        URL url = null;
        try {
            url = new URL("file:/home/tdiesler/svn/jbossws/stack/native/trunk/src/test/resources/jaxws/jbws1172/TestService.wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        MYTESTSERVICE_WSDL_LOCATION = url;
    }

    public MyTestService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public MyTestService() {
        super(MYTESTSERVICE_WSDL_LOCATION, new QName("http://www.my-company.it/ws/my-test", "MyTestService"));
    }

    /**
     * 
     * @return
     *     returns MyTest
     */
    @WebEndpoint(name = "MyTestPort")
    public MyTest getMyTestPort() {
        return (MyTest)super.getPort(new QName("http://www.my-company.it/ws/my-test", "MyTestPort"), MyTest.class);
    }

}
