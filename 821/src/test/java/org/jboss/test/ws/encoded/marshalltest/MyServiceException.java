// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.ws.encoded.marshalltest;

public class MyServiceException extends Exception
{
   private String theMessage;

   public MyServiceException(String theMessage)
   {
      super(theMessage);
      this.theMessage = theMessage;
   }

   public String getTheMessage()
   {
      return theMessage;
   }
}
