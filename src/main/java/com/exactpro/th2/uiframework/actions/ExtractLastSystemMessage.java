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
import com.exactpro.th2.act.framework.exceptions.UIFrameworkException;
import com.exactpro.th2.act.grpc.hand.RhSessionID;
import com.exactpro.th2.common.grpc.EventID;
import com.exactpro.th2.uiframework.UIFrameworkDemo;
import com.exactpro.th2.uiframework.UIFrameworkDemoContext;
import com.exactpro.th2.uiframework.framework.components.SystemMessagesGrid;
import com.exactpro.th2.uiframework.demo.win.grpc.ActResponse;
import com.exactpro.th2.uiframework.demo.win.grpc.BaseMessage;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class ExtractLastSystemMessage extends BaseAction<BaseMessage> {
	private static final Logger logger = LoggerFactory.getLogger(ExtractLastSystemMessage.class);


	public ExtractLastSystemMessage(UIFrameworkDemo framework, StreamObserver<ActResponse> responseObserver) {
		super(framework, responseObserver);
	}


	@Override
	protected String getName() {
		return "extractingLastSystemMessage";
	}

	@Override
	protected RhSessionID getSessionID(BaseMessage request) {
		return request.getSessionId();
	}

	@Override
	protected EventID getParentEventId(BaseMessage request) {
		return request.getParentEventId();
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected void collectActions(BaseMessage request, UIFrameworkDemoContext context, ActResult result) throws UIFrameworkException {
		SystemMessagesGrid grid = context.getMainWindow().getSystemMessagesGrid();
		result.setData(Collections.singletonMap("lastSystemMessage", grid.getLastSystemMessage()));
	}

	@Override
	protected String getDescription() {
		return "Extracting from MiniFix application displayed raw fix message";
	}

	@Override
	protected String getStatusInfo() {
		return "Last system message extracted";
	}
}
