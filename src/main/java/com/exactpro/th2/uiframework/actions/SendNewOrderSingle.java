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
import com.exactpro.th2.uiframework.framework.components.MainWindow;
import com.exactpro.th2.uiframework.framework.components.tabs.TransactionTab;
import com.exactpro.th2.uiframework.grpc.ActResponse;
import com.exactpro.th2.uiframework.grpc.SendNewOrderSingleRequest;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

public class SendNewOrderSingle extends BaseAction<SendNewOrderSingleRequest> {
	private static final Logger logger = LoggerFactory.getLogger(SendNewOrderSingle.class);


	public SendNewOrderSingle(UIFrameworkDemo framework, StreamObserver<ActResponse> responseObserver) {
		super(framework, responseObserver);
	}


	@Override
	protected String getName() {
		return "sendOrder";
	}

	@Override
	protected Map<String, String> convertRequestParams(SendNewOrderSingleRequest request) {
		if (request.getToggleTagsCount() > 0) {
			return Collections.singletonMap("toggledTags", request.getToggleTagsList().toString());
		}
		return null;
	}

	@Override
	protected RhSessionID getSessionID(SendNewOrderSingleRequest request) {
		return request.getBase().getSessionId();
	}

	@Override
	protected EventID getParentEventId(SendNewOrderSingleRequest request) {
		return request.getBase().getParentEventId();
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected void collectActions(SendNewOrderSingleRequest request, UIFrameworkDemoContext context, ActResult result) throws UIFrameworkException {
		MainWindow mainWindow = context.getMainWindow();
		TransactionTab transactionTab = mainWindow.openTransactionsTab();
		transactionTab.selectNewOrderSingleTransaction();
		transactionTab.toggleTags(request.getToggleTagsList());
		transactionTab.send();
	}

	@Override
	protected String getStatusInfo() {
		return "Order has been sent";
	}
}
