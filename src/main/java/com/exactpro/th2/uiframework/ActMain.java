/*
 * Copyright 2020-2021 Exactpro (Exactpro Systems Limited)
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

package com.exactpro.th2.uiframework;

import com.exactpro.th2.act.ActServer;
import com.exactpro.th2.common.schema.factory.CommonFactory;
import com.exactpro.th2.common.schema.grpc.router.GrpcRouter;
import com.exactpro.th2.uiframework.services.UIFrameworkDemoServiceImplementation;
import io.grpc.BindableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActMain {
	private final static Logger logger = LoggerFactory.getLogger(ActMain.class);

	public static void main(String[] args) {
		try {
			final CommonFactory factory = CommonFactory.createFromArguments(args);
			GrpcRouter grpcRouter = factory.getGrpcRouter();
			UIFrameworkDemo framework = new UIFrameworkDemo(new UIFrameworkDemoActConnections(factory));
			final ActServer actServer = new ActServer(grpcRouter.startServer(createService(framework)));
			addShutdownHook(factory, actServer::stop, actServer::blockUntilShutdown);
		}
		catch (Exception e) {
			logger.error("An error occurred", e);
			System.exit(1);
		}
	}

	private static void addShutdownHook(AutoCloseable... resources) {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			for (AutoCloseable resource : resources) {
				try {
					resource.close();
				} catch (Exception e) {
					logger.error("Unable to close resource", e);
				}
			}
		}));
	}

	private static BindableService createService(UIFrameworkDemo framework) {
		return new UIFrameworkDemoServiceImplementation(framework);
	}
}
