/*
 * Copyright 2006-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.jms.config.xml;

import com.consol.citrus.config.util.BeanDefinitionParserUtils;
import com.consol.citrus.endpoint.Endpoint;
import com.consol.citrus.endpoint.EndpointConfiguration;
import com.consol.citrus.jms.endpoint.JmsSyncEndpoint;
import com.consol.citrus.jms.endpoint.JmsSyncEndpointConfiguration;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Bean definition parser for synchronous JMS endpoint components.
 *
 * @author Christoph Deppisch
 * @since 1.4
 */
public class JmsSyncEndpointParser extends AbstractJmsEndpointParser {

    @Override
    protected Class<? extends Endpoint> getEndpointClass() {
        return JmsSyncEndpoint.class;
    }

    @Override
    protected Class<? extends EndpointConfiguration> getEndpointConfigurationClass() {
        return JmsSyncEndpointConfiguration.class;
    }

    @Override
    protected void parseEndpointConfiguration(BeanDefinitionBuilder endpointConfiguration, Element element, ParserContext parserContext) {
        super.parseEndpointConfiguration(endpointConfiguration, element, parserContext);

        BeanDefinitionParserUtils.setPropertyReference(endpointConfiguration,
                element.getAttribute("reply-destination"), "replyDestination");

        BeanDefinitionParserUtils.setPropertyValue(endpointConfiguration,
                element.getAttribute("reply-destination-name"), "replyDestinationName");

        BeanDefinitionParserUtils.setPropertyReference(endpointConfiguration,
                element.getAttribute("message-correlator"), "correlator");

        BeanDefinitionParserUtils.setPropertyValue(endpointConfiguration,
                element.getAttribute("polling-interval"), "pollingInterval");
    }
}
