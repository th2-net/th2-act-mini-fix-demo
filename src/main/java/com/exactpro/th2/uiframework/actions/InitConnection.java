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
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkException;
import com.exactpro.th2.act.grpc.hand.RhSessionID;
import com.exactpro.th2.common.grpc.EventID;
import com.exactpro.th2.uiframework.UIFrameworkDemo;
import com.exactpro.th2.uiframework.UIFrameworkDemoContext;
import com.exactpro.th2.uiframework.framework.components.MainWindow;
import com.exactpro.th2.uiframework.framework.components.tabs.SessionTab;
import com.exactpro.th2.uiframework.grpc.ActResponse;
import com.exactpro.th2.uiframework.grpc.InitConnectionRequest;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class InitConnection extends BaseAction<InitConnectionRequest> {
	private static final Logger logger = LoggerFactory.getLogger(InitConnection.class);


	public InitConnection(UIFrameworkDemo framework, StreamObserver<ActResponse> responseObserver) {
		super(framework, responseObserver);
	}

	@Override
	protected String getName() {
		return "initConnection";
	}

	@Override
	protected Map<String, String> convertRequestParams(InitConnectionRequest request) {
		Map<String, String> params = new LinkedHashMap<>(2);
		params.put("host", request.getHost());
		params.put("port", request.getPort());
		return params;
	}

	@Override
	protected RhSessionID getSessionID(InitConnectionRequest request) {
		return request.getBase().getSessionId();
	}

	@Override
	protected EventID getParentEventId(InitConnectionRequest request) {
		return request.getBase().getParentEventId();
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected void collectActions(InitConnectionRequest request, UIFrameworkDemoContext context, ActResult result) throws UIFrameworkException {
		MainWindow mainWindow = context.getMainWindow();
		processSessionSettings(mainWindow, request.getSessionSettings());
		mainWindow.connect(request.getHost(), request.getPort());
	}

	@Override
	protected String getStatusInfo() {
		return "Connection initialized";
	}


	private void processSessionSettings(MainWindow mainWindow, InitConnectionRequest.SessionSettings sessionSettings) throws UIFrameworkBuildingException {
		SessionTab sessionTab = mainWindow.openSessionTab();
		sessionTab.setSenderCompId(sessionSettings.getSenderCompId());
		sessionTab.setTargetCompId(sessionSettings.getTargetCompId());
		sessionTab.setInMsgSeq(sessionSettings.getInMsgSeqNo());
		sessionTab.setOutMsgSeq(sessionSettings.getOutMsgSeqNo());
		sessionTab.setHearthBeatInterval(sessionSettings.getHearthBeatInterval());
		sessionTab.setFixVersion(sessionSettings.getFixVersion());
		if (sessionSettings.getResetSession())
			sessionTab.resetSession();
	}
}
