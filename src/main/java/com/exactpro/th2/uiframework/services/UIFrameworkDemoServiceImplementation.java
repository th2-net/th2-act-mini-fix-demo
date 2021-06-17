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

package com.exactpro.th2.uiframework.services;

import com.exactpro.th2.act.framework.exceptions.UIFrameworkException;
import com.exactpro.th2.act.grpc.hand.RhSessionID;
import com.exactpro.th2.act.grpc.hand.RhTargetServer;
import com.exactpro.th2.uiframework.UIFrameworkDemo;
import com.exactpro.th2.uiframework.actions.*;
import com.exactpro.th2.uiframework.grpc.*;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UIFrameworkDemoServiceImplementation extends UIFrameworkActGrpc.UIFrameworkActImplBase {
	private static final Logger logger = LoggerFactory.getLogger(UIFrameworkDemoServiceImplementation.class);

	private final UIFrameworkDemo framework;


	public UIFrameworkDemoServiceImplementation(UIFrameworkDemo framework) {
		this.framework = framework;
	}


	@Override
	public void register(RhTargetServer request, StreamObserver<RhSessionID> responseObserver) {
		RhSessionID sessionId = framework.getHandExecutor().register(request);

		try {
			framework.registerSession(sessionId);
		} catch (UIFrameworkException e) {
			logger.error("Cannot register framework session", e);
		}

		responseObserver.onNext(sessionId);
		responseObserver.onCompleted();
	}

	@Override
	public void unregister(RhSessionID request, StreamObserver<Empty> responseObserver) {
		framework.getHandExecutor().unregister(request);

		try {
			framework.unregisterSession(request);
		} catch (UIFrameworkException e) {
			logger.error("Cannot unregister framework session", e);
		}

		responseObserver.onNext(Empty.getDefaultInstance());
		responseObserver.onCompleted();
	}

	@Override
	public void openApplication(OpenApplicationRequest request, StreamObserver<ActResponse> responseObserver) {
		new OpenApplication(framework, responseObserver).run(request);
	}

	@Override
	public void closeApplication(BaseMessage request, StreamObserver<ActResponse> responseObserver) {
		new CloseApplication(framework, responseObserver).run(request);
	}

	@Override
	public void sendNewOrderSingle(SendNewOrderSingleRequest request, StreamObserver<ActResponse> responseObserver) {
		new SendNewOrderSingle(framework, responseObserver).run(request);
	}

	@Override
	public void initConnection(InitConnectionRequest request, StreamObserver<ActResponse> responseObserver) {
		new InitConnection(framework, responseObserver).run(request);
	}

	@Override
	public void closeConnection(BaseMessage request, StreamObserver<ActResponse> responseObserver) {
		new CloseConnection(framework, responseObserver).run(request);
	}

	@Override
	public void extractLastOrderDetails(ExtractLastOrderDetailsRequest request, StreamObserver<ActResponse> responseObserver) {
		new ExtractLastOrderDetails(framework, responseObserver).run(request);
	}

	@Override
	public void extractLastSystemMessage(BaseMessage request, StreamObserver<ActResponse> responseObserver) {
		new ExtractLastSystemMessage(framework, responseObserver).run(request);
	}
}
