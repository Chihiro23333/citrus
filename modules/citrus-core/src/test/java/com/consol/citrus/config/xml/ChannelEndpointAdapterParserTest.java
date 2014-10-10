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

package com.consol.citrus.config.xml;

import com.consol.citrus.channel.ChannelEndpointAdapter;
import com.consol.citrus.testng.AbstractBeanDefinitionParserTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * @author Christoph Deppisch
 * @since 1.4
 */
public class ChannelEndpointAdapterParserTest extends AbstractBeanDefinitionParserTest {

    @Test
    public void testParseBeanDefinition() throws Exception {
        Map<String, ChannelEndpointAdapter> adapters = beanDefinitionContext.getBeansOfType(ChannelEndpointAdapter.class);

        Assert.assertEquals(adapters.size(), 1);

        // 1st endpoint adapter
        ChannelEndpointAdapter adapter = adapters.get("endpointAdapter");
        Assert.assertEquals(adapter.getName(), "endpointAdapter");
        Assert.assertEquals(adapter.getEndpointConfiguration().getTimeout(), 10000L);
        Assert.assertEquals((adapter.getEndpointConfiguration()).getChannelName(), "serverChannel");
        Assert.assertEquals(adapter.getEndpointConfiguration().isUseObjectMessages(), false);

    }
}
