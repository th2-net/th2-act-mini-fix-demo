/*
 * Copyright 2020-2024 Exactpro (Exactpro Systems Limited)
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

import com.exactpro.th2.act.ActConnections;
import com.exactpro.th2.act.ActServer;
import com.exactpro.th2.common.schema.factory.CommonFactory;
import com.exactpro.th2.common.schema.grpc.router.GrpcRouter;
import com.exactpro.th2.hand.Config;
import com.exactpro.th2.hand.services.HandBaseService;
import com.exactpro.th2.hand.services.MessageHandler;
import com.exactpro.th2.uiframework.configuration.UIFrameworkDemoConfigurations;
import com.exactpro.th2.uiframework.services.UIFrameworkDemoServiceImplementation;
import io.grpc.BindableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ActMain {
	private final static Logger logger = LoggerFactory.getLogger(ActMain.class);

	public static void main(String[] args) {
		try {
			var resources = new ArrayList<AutoCloseable>(4);
			final CommonFactory factory = CommonFactory.createFromArguments(args);
			resources.add(factory);
			GrpcRouter grpcRouter = factory.getGrpcRouter();

			var embeddedHandConfig = factory.getCustomConfiguration(UIFrameworkDemoConfigurations.class).getEmbeddedHandConfig();

			ActConnections<UIFrameworkDemoConfigurations> connections;
			if (embeddedHandConfig != null) {
				logger.info("Use embedded hand with config {}", embeddedHandConfig);
				MessageHandler messageHandler = new MessageHandler(new Config(factory, embeddedHandConfig));
				resources.add(messageHandler);
				connections = new UIFrameworkDemoActConnections(factory, () -> {
					HandBaseService service = new HandBaseService();
					service.init(messageHandler);
					return service;
				});
			} else {
				connections = new UIFrameworkDemoActConnections(factory);
			}
			UIFrameworkDemo framework = new UIFrameworkDemo(connections);

			final ActServer actServer = new ActServer(grpcRouter.startServer(createService(framework)));
			resources.add(actServer::stop);
			resources.add(actServer::blockUntilShutdown);
			addShutdownHook(resources);
		}
		catch (Exception e) {
			logger.error("An error occurred", e);
			System.exit(1);
		}
	}

	private static void addShutdownHook(List<AutoCloseable> resources) {
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
