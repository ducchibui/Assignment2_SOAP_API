/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *
 * @author CBui
 */
public class UploadMovieHandler implements SOAPHandler<SOAPMessageContext> {

    public boolean handleMessage(SOAPMessageContext context) {
        SOAPMessage message = context.getMessage();
        try {

            Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
            if (!outbound) {
                Node elementInbound = (Node) message.getSOAPBody().getFirstChild();
                String str = elementInbound.getNodeName();
                if (str.contains("uploadMovie")) {
                    Node childElementInbound = (Node) elementInbound.getFirstChild();
                    String childText = childElementInbound.getValue();
                    if (childText.contains("disney")) {
                        return false;
                    }
                    System.out.println(childText);
//                    childElementInbound.setTextContent("Humber");
                }
            }
        } catch (SOAPException ex) {
            Logger.getLogger(UploadMovieHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UploadMovieHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
//        try {
//            message.writeTo(System.out);
//        } catch (SOAPException ex) {
//            Logger.getLogger(UploadMovieHandler.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(UploadMovieHandler.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return true;
    }

    public Set<QName> getHeaders() {
        return Collections.EMPTY_SET;
    }

    public boolean handleFault(SOAPMessageContext messageContext) {
        return true;
    }

    public void close(MessageContext context) {
    }

}
