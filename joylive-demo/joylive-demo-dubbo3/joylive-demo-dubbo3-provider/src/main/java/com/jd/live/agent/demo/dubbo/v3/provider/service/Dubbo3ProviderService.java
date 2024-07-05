/*
 * Copyright © ${year} ${owner} (${email})
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
package com.jd.live.agent.demo.dubbo.v3.provider.service;

import com.jd.live.agent.demo.response.LiveLocation;
import com.jd.live.agent.demo.response.LiveResponse;
import com.jd.live.agent.demo.response.LiveTrace;
import com.jd.live.agent.demo.response.LiveTransmission;
import com.jd.live.agent.demo.service.HelloService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcContextAttachment;
import org.springframework.beans.factory.annotation.Value;

@DubboService(group = "live-demo", interfaceClass = HelloService.class)
public class Dubbo3ProviderService implements HelloService {

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public LiveResponse echo(String str) {
        RpcContextAttachment attachment = RpcContext.getServerAttachment();
        return new LiveResponse(str).addFirst(new LiveTrace(applicationName, LiveLocation.build(),
                LiveTransmission.build("attachment", attachment::getAttachment)));
    }

    @Override
    public LiveResponse status(int code) {
        if (code >= 500) {
            throw new RuntimeException("Code:" + code);
        }
        RpcContextAttachment attachment = RpcContext.getServerAttachment();
        return new LiveResponse(code).addFirst(new LiveTrace(applicationName, LiveLocation.build(),
                LiveTransmission.build("attachment", attachment::getAttachment)));
    }
}
