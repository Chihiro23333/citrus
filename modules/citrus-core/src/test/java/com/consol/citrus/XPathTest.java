package com.consol.citrus;

import static org.easymock.EasyMock.*;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.core.Message;
import org.springframework.integration.message.MessageBuilder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.consol.citrus.actions.ReceiveMessageAction;
import com.consol.citrus.message.MessageReceiver;
import com.consol.citrus.validation.XMLMessageValidator;

public class XPathTest extends AbstractBaseTest {
    @Autowired
    XMLMessageValidator validator;
    
    MessageReceiver messageReceiver = EasyMock.createMock(MessageReceiver.class);
    
    ReceiveMessageAction receiveMessageBean;
    
    @Override
    @BeforeMethod
    public void setup() {
        super.setup();
        
        receiveMessageBean = new ReceiveMessageAction();
        receiveMessageBean.setMessageReceiver(messageReceiver);
        receiveMessageBean.setValidator(validator);
        receiveMessageBean.setSchemaValidation(false);
    }
    
    @Test
    public void testUsingXPath() {
        reset(messageReceiver);
        
        Message message = MessageBuilder.withPayload("<ns1:root xmlns='http://test' xmlns:ns1='http://testsuite'>"
                            + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                                + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                                + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                                + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                            + "</element>"
                            + "<ns1:ns-element>namespace</ns1:ns-element>"
                            + "<search-element>search-for</search-element>"
                        + "</ns1:root>")
                        .build();
        
        expect(messageReceiver.receive(anyLong())).andReturn(message);
        replay(messageReceiver);
        
        HashMap<String, String> validateMessageElements = new HashMap<String, String>();
        validateMessageElements.put("//:element/:sub-elementA", "text-value");
        validateMessageElements.put("//:element/:sub-elementA[@attribute='A']", "text-value");
        validateMessageElements.put("//:element/:sub-elementB", "text-value");
        validateMessageElements.put("//:element/:sub-elementB/@attribute", "B");
        validateMessageElements.put("//ns1:ns-element", "namespace");
        validateMessageElements.put("//*[.='search-for']", "search-for");
        
        receiveMessageBean.setValidateMessageElements(validateMessageElements);
        
        receiveMessageBean.execute(context);
    }
    
    @Test
    public void testUsingXPathWithDefaultNamespace() {
        reset(messageReceiver);
        
        Message message = MessageBuilder.withPayload("<root xmlns='http://test'>"
                            + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                                + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                                + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                                + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                            + "</element>"
                            + "<ns-element>namespace</ns-element>"
                            + "<search-element>search-for</search-element>"
                        + "</root>")
                        .build();
        
        expect(messageReceiver.receive(anyLong())).andReturn(message);
        replay(messageReceiver);
        
        HashMap<String, String> validateMessageElements = new HashMap<String, String>();
        validateMessageElements.put("//:element/:sub-elementA", "text-value");
        validateMessageElements.put("//:element/:sub-elementA[@attribute='A']", "text-value");
        validateMessageElements.put("//:element/:sub-elementB", "text-value");
        validateMessageElements.put("//:element/:sub-elementB/@attribute", "B");
        validateMessageElements.put("//:ns-element", "namespace");
        validateMessageElements.put("//*[.='search-for']", "search-for");
        
        receiveMessageBean.setValidateMessageElements(validateMessageElements);
        
        receiveMessageBean.execute(context);
    }
    
    @Test
    public void testUsingXPathWithExplicitNamespace() {
        reset(messageReceiver);
        
        Message message = MessageBuilder.withPayload("<root xmlns='http://test' xmlns:ns1='http://testsuite'>"
                            + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                                + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                                + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                                + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                            + "</element>"
                            + "<ns1:ns-element>namespace</ns1:ns-element>"
                            + "<search-element>search-for</search-element>"
                        + "</root>")
                        .build();
        
        expect(messageReceiver.receive(anyLong())).andReturn(message);
        replay(messageReceiver);
        
        HashMap<String, String> validateMessageElements = new HashMap<String, String>();
        validateMessageElements.put("//:element/:sub-elementA", "text-value");
        validateMessageElements.put("//ns1:ns-element", "namespace");
        
        receiveMessageBean.setValidateMessageElements(validateMessageElements);
        
        receiveMessageBean.execute(context);
    }
    
    @Test
    public void testUsingXPathWithExplicitNamespaceInElementDefinition() {
        reset(messageReceiver);
        
        Message message = MessageBuilder.withPayload("<root xmlns='http://test'>"
                            + "<element attributeA='attribute-value' attributeB='attribute-value'>"
                                + "<sub-elementA attribute='A'>text-value</sub-elementA>"
                                + "<sub-elementB attribute='B'>text-value</sub-elementB>"
                                + "<sub-elementC attribute='C'>text-value</sub-elementC>"
                            + "</element>"
                            + "<ns1:ns-element xmlns:ns1='http://testsuite'>namespace</ns1:ns-element>"
                            + "<search-element>search-for</search-element>"
                        + "</root>")
                        .build();
        
        expect(messageReceiver.receive(anyLong())).andReturn(message);
        replay(messageReceiver);
        
        HashMap<String, String> validateMessageElements = new HashMap<String, String>();
        validateMessageElements.put("//:element/:sub-elementA", "text-value");
        validateMessageElements.put("//ns1:ns-element", "namespace");
        
        receiveMessageBean.setValidateMessageElements(validateMessageElements);
        
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("ns1", "http://testsuite");
        
        receiveMessageBean.setNamespaces(namespaces);
        
        receiveMessageBean.execute(context);
    }
}
