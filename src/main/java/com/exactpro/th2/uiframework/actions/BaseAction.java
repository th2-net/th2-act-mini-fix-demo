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

package com.exactpro.th2.uiframework.actions;

import com.exactpro.th2.act.ActResult;
import com.exactpro.th2.act.actions.ActAction;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkException;
import com.exactpro.th2.act.grpc.hand.RhSessionID;
import com.exactpro.th2.uiframework.UIFrameworkDemo;
import com.exactpro.th2.uiframework.UIFrameworkDemoContext;
import com.exactpro.th2.uiframework.UIFrameworkDemoSessionContext;
import com.exactpro.th2.uiframework.demo.win.grpc.ActResponse;
import com.google.protobuf.MessageOrBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Consumer;

public abstract class BaseAction<T extends MessageOrBuilder> extends ActAction<T, UIFrameworkDemoContext, UIFrameworkDemoSessionContext> {
	private static final Logger logger = LoggerFactory.getLogger(BaseAction.class);


	private final StreamObserver<ActResponse> responseObserver;

	public BaseAction(UIFrameworkDemo framework, StreamObserver<ActResponse> responseObserver) {
		super(framework);
		this.responseObserver = responseObserver;
	}


	@Override
	protected Map<String, String> convertRequestParams(T details) {
		return null;
	}

	protected boolean storeParentEvent() {
		return false;
	}

	@Override
	public void run(T request) {
		String requestName = request.getClass().getSimpleName();
		long startTime = System.currentTimeMillis();
		logger.info("Started executing request '{}'", requestName);
		super.run(request);
		logger.info("Request '{}' executed in {} millis", requestName, System.currentTimeMillis() - startTime);
	}

	@Override
	protected String getRequestTableHeader() {
		return "Input parameters";
	}

	@Override
	protected void processResult(ActResult result) throws UIFrameworkException {
		ActResponse.Builder responseBuilder = createResponseBuilder();
		responseBuilder.setSessionID(createOrGetSessionId(result))
				.setScriptStatus(convertStatus(result.getScriptStatus()));
		addIfNotEmpty(result.getErrorInfo(), responseBuilder::setErrorInfo);
		addIfNotEmpty(result.getStatusInfo(), responseBuilder::setStatusInfo);
		addIfNotEmpty(result.getData(), responseBuilder::putAllData);
		responseObserver.onNext(responseBuilder.build());
		responseObserver.onCompleted();
	}

	protected ActResponse.Builder createResponseBuilder() {
		return ActResponse.newBuilder();
	}

	protected void addIfNotEmpty(String field, Consumer<String> action) {
		if (StringUtils.isNotEmpty(field)) {
			action.accept(field);
		}
	}

	protected void addIfNotEmpty(Map<String, String> data, Consumer<Map<String, String>> action) {
		if (data != null && !data.isEmpty()) {
			action.accept(data);
		}
	}


	private static RhSessionID createOrGetSessionId(ActResult result) {
		Object sessionId = result.getSessionID();
		if (sessionId instanceof RhSessionID) {
			return (RhSessionID) sessionId;
		} else {
			return RhSessionID.newBuilder().setId(String.valueOf(sessionId)).build();
		}
	}

	private static ActResponse.ExecutionStatus convertStatus(ActResult.ActExecutionStatus status) {
		switch (status) {
			case SUCCESS: return ActResponse.ExecutionStatus.SUCCESS;
			case HAND_INTERNAL_ERROR: return ActResponse.ExecutionStatus.HAND_ERROR;
			case COMPILE_ERROR: return ActResponse.ExecutionStatus.COMPILE_ERROR;
			case EXECUTION_ERROR: return ActResponse.ExecutionStatus.EXECUTION_ERROR;
			default: return ActResponse.ExecutionStatus.ACT_ERROR;
		}
	}
}
