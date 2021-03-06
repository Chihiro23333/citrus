/*
 * Copyright 2006-2013 the original author or authors.
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

package com.consol.citrus.javadsl.design;

import com.consol.citrus.dsl.testng.TestNGCitrusTestDesigner;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

/**
 * @author Christoph Deppisch
 */
@Test
public class JsonTextValidationJavaIT extends TestNGCitrusTestDesigner {
    
    @CitrusTest
    public void jsonTextValidation() {
        parallel(
            http().client("httpClient")
                .send()
                .post()
                .payload("{" +
                    "\"type\" : \"read\"," +
                    "\"mbean\" : \"java.lang:type=Memory\"," +
                    "\"attribute\" : \"HeapMemoryUsage\"," +
                    "\"path\" : \"used\"" +
                  "}"),
            sequential(
                http().server("httpServerRequestEndpoint")
                   .receive()
                   .post()
                   .messageType(MessageType.JSON)
                   .payload("{" +
                            "\"type\" : \"read\"," +
                            "\"mbean\" : \"java.lang:type=Memory\"," +
                            "\"attribute\" : \"HeapMemoryUsage\"," +
                            "\"path\" : \"@equalsIgnoreCase('USED')@\"" +
                          "}")
                   .extractFromHeader("citrus_jms_messageId", "correlation_id"),
                http().server("httpServerResponseEndpoint")
                   .send()
                   .response(HttpStatus.OK)
                   .payload("{" +
                        "\"timestamp\" : \"2011-01-01\"," +
                        "\"status\" : 200," +
                        "\"request\" : " +
                          "{" +
                            "\"mbean\" : \"java.lang:type=Memory\"," +
                            "\"path\" : \"used\"," +
                            "\"attribute\" : \"HeapMemoryUsage\"," +
                            "\"type\" : \"read\"" +
                          "}," +
                        "\"value\" : 512" +
                      "}")
                   .version("HTTP/1.1")
                   .header("citrus_jms_correlationId", "${correlation_id}")
            )
        );
        
        http().client("httpClient")
            .receive()
            .response(HttpStatus.OK)
            .messageType(MessageType.JSON)
            .payload("{" +
                        "\"timestamp\" : \"@matchesDatePattern('yyyy-MM-dd')@\"," +
                        "\"status\" : 200," +
                        "\"request\" : " +
                          "{" +
                            "\"mbean\" : \"java.lang:type=Memory\"," +
                            "\"path\" : \"@matches('u*s*e*d*')@\"," +
                            "\"attribute\" : \"HeapMemoryUsage\"," +
                            "\"type\" : \"read\"" +
                          "}," +
                        "\"value\" : \"@isNumber()@\"" +
                      "}")
            .version("HTTP/1.1");
        
        http().client("httpClient")
            .send()
            .post()
            .payload("{" +
                "\"type\" : \"read\"," +
                "\"mbean\" : \"java.lang:type=Memory\"," +
                "\"attribute\" : \"HeapMemoryUsage\"," +
                "\"path\" : \"used\"" +
              "}");
        
        sleep(2000);
        
        http().client("httpClient")
            .receive()
            .response()
            .messageType(MessageType.JSON)
            .status(HttpStatus.OK)
            .version("HTTP/1.1");
    }
}