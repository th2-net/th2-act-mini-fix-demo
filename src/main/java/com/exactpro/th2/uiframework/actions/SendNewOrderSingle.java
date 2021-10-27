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
import com.exactpro.th2.uiframework.CustomActResult;
import com.exactpro.th2.uiframework.UIFrameworkDemo;
import com.exactpro.th2.uiframework.UIFrameworkDemoContext;
import com.exactpro.th2.uiframework.framework.components.MainWindow;
import com.exactpro.th2.uiframework.demo.win.grpc.ActResponse;
import com.exactpro.th2.uiframework.demo.win.grpc.SendNewOrderSingleRequest;
import io.grpc.stub.StreamObserver;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
		Map<String, String> params = new LinkedHashMap<>();
		for (SendNewOrderSingleRequest.Tags tags : request.getTagsList()) {
			var tagNumber = tags.getTagNumber();
			if (!params.containsKey(tagNumber)) {
				params.put(tagNumber, tags.getTagValue());
			} else {
				int index = 2;
				String newInd;
				//if this tag number already exists in maps params, search unique postfix to keep all values in map.
				//noinspection StatementWithEmptyBody
				while (params.containsKey(newInd = tagNumber + " [" + index++ + "]"));
				params.put(newInd, tags.getTagValue());
			}
		}
		return params;
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
	protected void collectActions(SendNewOrderSingleRequest request, UIFrameworkDemoContext context, CustomActResult result) throws UIFrameworkException {
		MainWindow mainWindow = context.getMainWindow();
		var transactionTab = mainWindow.openTransactionsTab();
		var transactionName = "DemoTransaction";
		transactionTab.createTransaction(transactionName);
		transactionTab.selectTransaction(transactionName);
		transactionTab.setTags(convertTags(request));
		transactionTab.send();
		transactionTab.deleteTransaction(transactionName);
	}
	
	private List<Pair<String, String>> convertTags(SendNewOrderSingleRequest request) {
		return request.getTagsList().stream().map(tags -> new ImmutablePair<>(tags.getTagNumber(), tags.getTagValue()))
				.collect(Collectors.toList());
	}

	@Override
	protected String getDescription() {
		return "Sending NewOrderSingle via MiniFix application";
	}

	@Override
	protected String getStatusInfo() {
		return "Order has been sent";
	}
}
