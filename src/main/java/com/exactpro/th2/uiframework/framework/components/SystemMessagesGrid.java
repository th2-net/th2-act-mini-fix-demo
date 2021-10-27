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

package com.exactpro.th2.uiframework.framework.components;

import com.exactpro.th2.act.framework.ExecutionParams;
import com.exactpro.th2.act.framework.InternalMessageType;
import com.exactpro.th2.act.framework.builders.win.WinBuilderManager;
import com.exactpro.th2.act.framework.builders.win.WinLocator;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkExecutionException;
import com.exactpro.th2.act.framework.ui.WinUIElement;
import com.exactpro.th2.act.grpc.hand.RhBatchResponse;
import com.exactpro.th2.uiframework.framework.utils.ResponseUtils;

public class SystemMessagesGrid extends WinUIElement {
	private WinLocator gridLocator;


	public SystemMessagesGrid(WinBuilderManager builders) {
		super(builders);
	}


	public SystemMessagesGrid init(WinLocator winLocator) {
		gridLocator = winLocator;
		return this;
	}

	public String getLastSystemMessage() throws UIFrameworkBuildingException, UIFrameworkExecutionException {
		WinLocator messageLocator = gridLocator.byXpath("/List/ListItem[last()]/Text[3]");
		String id = "lastSystemMessage";
		builders.getElAttribute().id(id).winLocator(messageLocator).attributeName("Name").build();
		ExecutionParams executionParams = ExecutionParams.builder()
				.setEventName("extractingLastSystemMessage")
				.setStoreActionMessages(true)
				.setMessageType(InternalMessageType.FIX)
				.build();
		RhBatchResponse response = builders.getContext().submit(executionParams);
		return ResponseUtils.getResultByIdOrThrow(response, id);
	} 
}
