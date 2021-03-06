### Xml message validation

XML is a very common message format especially in the SOAP WebServices and JMS messaging world. Citrus provides XML message validator implementations that are able to compare XML message structures. The validator will notice differences in the XML message structure and supports XML namespaces, attributes and XML schema validation. The default XML message validator implementation is active by default and can be overwritten with a custom implementation using the bean id **defaultXmlMessageValidator** .

```xml
<bean id="defaultXmlMessageValidator" class="com.consol.citrus.validation.xml.DomXmlMessageValidator"/>
```

The default XML message validator is very powerful when it comes to compare XML structures. The validator supports namespaces with different prefixes and attributes als well as namespace qualified attributes. See the following sections for a detailed description of all capabilities.

### XML payload validation

Once Citrus has received a message the tester can validate the message contents in various ways. First of all the tester can compare the whole message payload to a predefined control message template.

The receiving action offers following elements for control message templates:

*  **<payload>** : Defines the message payload as nested XML message template. The whole message payload is defined inside the test case.

*  **<data>** : Defines an inline XML message template as nested CDATA. Slightly different to the payload variation as we define the whole message payload inside the test case as CDATA section.

*  **<resource>** : Defines an expected XML message template via external file resources. This time the payload is loaded at runtime from the external file.



Both ways inline payload definition or external file resource give us a control message template that the test case expects to arrive. Citrus uses this control template for extended message comparison. All elements, namespaces, attributes and node values are validated in this comparison. When using XML message payloads Citrus will navigate through the whole XML structure validating each element and its content. Same with JSON payloads.

Only in case received message and control message are equal to each other as expected the message validation will pass. In case differences occur Citrus gives detailed error messages and the test case fails.

The control message template is not necessarily very static. Citrus supports various ways to add dynamic message content on the one side and on the other side Citrus can ignore some elements that are not part of message comparison (e.g. when generated content or timestamps are part of the message content). The tester can enrich the expected message template with test variables or ignore expressions so we get a more robust validation mechanism. We will talk about this in the next sections to come.

When using the Citrus Java DSL you will face a verbose message payload definition. This is because Java does not support multiline character sequence values as Strings. We have to use verbose String concatenation when constructing XML message payload contents for instance. In addition to that reserved characters like quotes must be escaped and line breaks must be explicitly added. All these impediments let me suggest to use external file resources in Java DSL when dealing with large complex message payload data. Here is an example:

**Java DSL designer** 

```java
@CitrusTest
public void receiveMessageTest() {
    receive("helloServiceServer")
        .payload(new ClassPathResource("com/consol/citrus/message/data/TestRequest.xml"))
        .header("Operation", "sayHello")
        .header("MessageId", "${messageId}");
}
```

### XML header validation

Now that we have validated the message payload in various ways we are now interested in validating the message header. This is simple as you have to define the header name and the control value that you expect. Just add the following header validation to your receiving action.

**XML DSL** 

```xml
<header>
    <element name="Operation" value="GetCustomer"/>
    <element name="RequestTag" value="${requestTag}"/>
</header>
```

**Java DSL designer** 

```java
@CitrusTest
public void receiveMessageTest() {
    receive("helloServiceServer")
        .header("Operation", "sayHello")
        .header("MessageId", "${messageId}");
}
```

Message headers are represented as name-value pairs. Each expected header element identified by its name has to be present in the received message. In addition to that the header value is compared to the given control value. If a header entry is not found by its name or the value does not fit accordingly Citrus will raise validation errors and the test case will fail.

**Note**
Sometimes message headers may not apply to the name-value pair pattern. For example SOAP headers can also contain XML fragments. Citrus supports these kind of headers too. Please see the SOAP chapter for more details.### Ignore XML elements

Some elements in the message payload might not apply for validation at all. Just think of communication timestamps an dynamic values inside a message:

The timestamp value in our next example will dynamically change from test run to test run and is hardly predictable for the tester, so lets ignore it in validation.

**XML DSL** 

```xml
 <message>
    <payload>
        <TestMessage>
            <MessageId>${messageId}</MessageId>
            <Timestamp>2001-12-17T09:30:47.0Z</Timestamp>
            <VersionId>@ignore@</VersionId>
        </TestMessage>
    </payload>
    <ignore path="/TestMessage/Timestamp"/>
</message>
```

Although we have given a static timestamp value in the payload data the element is ignored during validation as the ignore XPath expression matches the element. In addition to that we also ignored the version id element in this example. This time with an inline **@ignore@** expression. This is for those of you that do not like XPath. As a result the ignored message elements are automatically skipped when Citrus compares and validates message contents and do not break the test case.

When using the Java DSL the **@ignore@** placeholder as well as XPath expressions can be used seamlessly. Here is an example of that:

**Java DSL designer** 

```java
@CitrusTest
public void receiveMessageTest() {
    receive("helloServiceServer")
        .payload(new ClassPathResource("com/consol/citrus/message/data/TestRequest.xml"))
        .header("Operation", "sayHello")
        .header("MessageId", "${messageId}")
        .ignore("/TestMessage/Timestamp");
}
```

Of course you can use the inline **@ignore@** placeholder in an external file resource, too.

### Groovy XML validation

With the Groovy XmlSlurper you can easily validate XML message payloads without having to deal directly with XML. People who do not want to deal with XPath may also like this validation alternative. The tester directly navigates through the message elements and uses simple code assertions in order to control the message content. Here is an example how to validate messages with Groovy script:

**XML DSL** 

```xml
<receive endpoint="helloServiceClient" timeout="5000">
    <message>
        <validate>
            <script type="groovy">
                assert root.children().size() == 4
                assert root.MessageId.text() == '${messageId}'
                assert root.CorrelationId.text() == '${correlationId}'
                assert root.User.text() == 'HelloService'
                assert root.Text.text() == 'Hello ' + context.getVariable("user")
            </script>
        </validate>
    </message>
    <header>
        <element name="Operation" value="sayHello"/>
        <element name="CorrelationId" value="${correlationId}"/>
    </header>
</receive>
```

**Java DSL designer** 

```java
@CitrusTest
public void receiveMessageTest() {
    receive("helloServiceClient")
        .validateScript("assert root.MessageId.text() == '${messageId}';" +
                        "assert root.CorrelationId.text() == '${correlationId}';")
        .header("Operation, "sayHello")
        .header("CorrelationId", "${correlationId}")
        .timeout(5000L);
}
```

The Groovy XmlSlurper validation script goes right into the message-tag instead of a XML control template or XPath validation. The Groovy script supports Java ***assert*** statements for message element validation. Citrus automatically injects the root element ***root*** to the validation script. This is the Groovy XmlSlurper object and the start of element navigation. Based on this root element you can access child elements and attributes with a dot notated syntax. Just use the element names separated by a simple dot. Very easy! If you need the list of child elements use the ***children()*** function on any element. With the ***text()*** function you get access to the element's text-value. The ***size()*** is very useful for validating the number of child elements which completes the basic validation statements.

As you can see from the example, we may use test variables within the validation script, too. Citrus has also injected the actual test context to the validation script. The test context object holds all test variables. So you can also access variables with ***context.getVariable("user")*** for instance. On the test context you can also set new variable values with ***context.setVariable("user", "newUserName")*** .

There is even more object injection for the validation script. With the automatically added object ***receivedMessage*** You have access to the Citrus message object for this receive action. This enables you to do whatever you want with the message payload or header.

**XML DSL** 

```xml
<receive endpoint="helloServiceClient" timeout="5000">
    <message>
        <validate>
            <script type="groovy">
                assert receivedMessage.getPayload(String.class).contains("Hello Citrus!")
                assert receivedMessage.getHeader("Operation") == 'sayHello'

                context.setVariable("request_payload", receivedMessage.getPayload(String.class))
            </script>
        </validate>
    </message>
</receive>
```

The listing above shows some power of the validation script. We can access the message payload, we can access the message header. With test context access we can also save the whole message payload as a new test variable for later usage in the test.

In general Groovy code inside the XML test case definition or as part of the Java DSL code is not very comfortable to maintain. You do not have code syntax assist or code completion. This is why we can also use external file resources for the validation scripts. The syntax looks like follows:

**XML DSL** 

```xml
<receive endpoint="helloServiceClient" timeout="5000">
    <message>
        <validate>
            <script type="groovy" file="classpath:validationScript.groovy"/>
        </validate>
    </message>
    <header>
        <element name="Operation" value="sayHello"/>
        <element name="CorrelationId" value="${correlationId}"/>
    </header>
</receive>
```

**Java DSL designer** 

```java
@CitrusTest
public void receiveMessageTest() {
    receive("helloServiceClient")
        .validateScript(new FileSystemResource("validationScript.groovy"))
        .header("Operation, "sayHello")
        .header("CorrelationId", "${correlationId}")
        .timeout(5000L);
}
```

We referenced some external file resource ***validationScript.groovy*** . This file content is loaded at runtime and is used as script body. Now that we have a normal groovy file we can use the code completion and syntax highlighting of our favorite Groovy editor.

**Note**
You can use the Groovy validation script in combination with other validation types like XML tree comparison and XPath validation.**Tip**
For further information on the Groovy XmlSlurper please see the official Groovy website and documentation