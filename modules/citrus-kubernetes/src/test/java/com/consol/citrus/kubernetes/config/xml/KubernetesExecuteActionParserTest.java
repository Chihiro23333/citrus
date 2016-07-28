/*
 * Copyright 2006-2015 the original author or authors.
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

package com.consol.citrus.kubernetes.config.xml;

import com.consol.citrus.kubernetes.actions.KubernetesExecuteAction;
import com.consol.citrus.kubernetes.client.KubernetesClient;
import com.consol.citrus.kubernetes.command.*;
import com.consol.citrus.testng.AbstractActionParserTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class KubernetesExecuteActionParserTest extends AbstractActionParserTest<KubernetesExecuteAction> {

    @Test
    public void testKubernetesExecuteActionParser() {
        assertActionCount(9);
        assertActionClassAndName(KubernetesExecuteAction.class, "kubernetes-execute");

        KubernetesExecuteAction action = getNextTestActionFromTest();
        Assert.assertNotNull(action.getCommand());
        Assert.assertEquals(action.getCommand().getClass(), Info.class);
        Assert.assertEquals(action.getKubernetesClient().getClass(), KubernetesClient.class);
        Assert.assertEquals(action.getCommand().getParameters().size(), 0);

        action = getNextTestActionFromTest();
        Assert.assertNotNull(action.getCommand());
        Assert.assertEquals(action.getCommand().getClass(), Info.class);
        Assert.assertEquals(action.getKubernetesClient(), beanDefinitionContext.getBean("myKubernetesClient", KubernetesClient.class));
        Assert.assertEquals(action.getCommand().getParameters().size(), 0);

        action = getNextTestActionFromTest();
        Assert.assertNotNull(action.getCommand());
        Assert.assertEquals(action.getCommand().getClass(), ListPods.class);
        Assert.assertEquals(action.getCommand().getParameters().size(), 0);

        action = getNextTestActionFromTest();
        Assert.assertNotNull(action.getCommand());
        Assert.assertEquals(action.getCommand().getClass(), ListEvents.class);
        Assert.assertEquals(action.getCommand().getParameters().size(), 0);

        action = getNextTestActionFromTest();
        Assert.assertNotNull(action.getCommand());
        Assert.assertEquals(action.getCommand().getClass(), ListPods.class);
        Assert.assertEquals(action.getCommand().getParameters().size(), 1);
        Assert.assertEquals(action.getCommand().getParameters().get("label").toString(), "pod_label");

        action = getNextTestActionFromTest();
        Assert.assertNotNull(action.getCommand());
        Assert.assertEquals(action.getCommand().getClass(), ListServices.class);
        Assert.assertEquals(action.getCommand().getParameters().size(), 1);
        Assert.assertEquals(action.getCommand().getParameters().get("label").toString(), "!pod_label");

        action = getNextTestActionFromTest();
        Assert.assertNotNull(action.getCommand());
        Assert.assertEquals(action.getCommand().getClass(), ListNodes.class);
        Assert.assertEquals(action.getCommand().getParameters().size(), 1);
        Assert.assertEquals(action.getCommand().getParameters().get("label").toString(), "pod_label=active");

        action = getNextTestActionFromTest();
        Assert.assertNotNull(action.getCommand());
        Assert.assertEquals(action.getCommand().getClass(), ListEndpoints.class);
        Assert.assertEquals(action.getCommand().getParameters().size(), 1);
        Assert.assertEquals(action.getCommand().getParameters().get("label").toString(), "pod_label!=active");

        action = getNextTestActionFromTest();
        Assert.assertNotNull(action.getCommand());
        Assert.assertEquals(action.getCommand().getClass(), ListNamespaces.class);
        Assert.assertEquals(action.getCommand().getParameters().size(), 1);
        Assert.assertEquals(action.getCommand().getParameters().get("label").toString(), "pod_label1!=active,pod_label2=active");
    }
}