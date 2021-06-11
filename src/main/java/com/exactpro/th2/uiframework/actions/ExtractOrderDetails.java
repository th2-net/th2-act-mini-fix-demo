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
import com.exactpro.th2.uiframework.framework.components.OrdersGrid;
import com.exactpro.th2.uiframework.grpc.ActResponse;
import com.exactpro.th2.uiframework.grpc.ExtractOrderDetailsRequest;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

public class ExtractOrderDetails extends BaseAction<ExtractOrderDetailsRequest> {
	private static final Logger logger = LoggerFactory.getLogger(ExtractOrderDetails.class);


	public ExtractOrderDetails(UIFrameworkDemo framework, StreamObserver<ActResponse> responseObserver) {
		super(framework, responseObserver);
	}


	@Override
	protected Map<String, String> convertRequestParams(ExtractOrderDetailsRequest details) {
		return Collections.singletonMap("extractedFields", details.getExtractionFieldsList().toString());
	}

	@Override
	protected String getName() {
		return "extractOrderDetails";
	}

	@Override
	protected RhSessionID getSessionID(ExtractOrderDetailsRequest request) {
		return request.getBase().getSessionId();
	}

	@Override
	protected EventID getParentEventId(ExtractOrderDetailsRequest request) {
		return request.getBase().getParentEventId();
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected void collectActions(ExtractOrderDetailsRequest request, UIFrameworkDemoContext context, ActResult result) throws UIFrameworkException {
		OrdersGrid orders = context.getMainWindow().getOrders();
		result.setData(orders.extractLastOrderFields(request.getExtractionFieldsList()));
	}

	@Override
	protected String getStatusInfo() {
		return "Order details received";
	}
}
