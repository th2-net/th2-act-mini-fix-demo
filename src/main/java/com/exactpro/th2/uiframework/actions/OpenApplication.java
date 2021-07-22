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
import com.exactpro.th2.act.framework.builders.win.WinBuilderManager;
import com.exactpro.th2.act.framework.builders.win.WinLocator;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkException;
import com.exactpro.th2.act.grpc.hand.RhSessionID;
import com.exactpro.th2.common.grpc.EventID;
import com.exactpro.th2.uiframework.UIFrameworkDemo;
import com.exactpro.th2.uiframework.UIFrameworkDemoContext;
import com.exactpro.th2.uiframework.framework.components.MainWindow;
import com.exactpro.th2.uiframework.demo.win.grpc.ActResponse;
import com.exactpro.th2.uiframework.demo.win.grpc.OpenApplicationRequest;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class OpenApplication extends BaseAction<OpenApplicationRequest> {
	private static final Logger logger = LoggerFactory.getLogger(OpenApplication.class);


	public OpenApplication(UIFrameworkDemo framework, StreamObserver<ActResponse> responseObserver) {
		super(framework, responseObserver);
	}

	@Override
	protected String getName() {
		return "openApplication";
	}

	@Override
	protected Map<String, String> convertRequestParams(OpenApplicationRequest request) {
		Map<String, String> params = new LinkedHashMap<>(2);
		params.put("workDir", request.getWorkDir());
		params.put("appFile", request.getAppFile());
		return params;
	}

	@Override
	protected RhSessionID getSessionID(OpenApplicationRequest request) {
		return request.getBase().getSessionId();
	}

	@Override
	protected EventID getParentEventId(OpenApplicationRequest request) {
		return request.getBase().getParentEventId();
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected void collectActions(OpenApplicationRequest request, UIFrameworkDemoContext context, ActResult result) throws UIFrameworkException {
		WinBuilderManager builderManager = context.createBuilderManager();
		builderManager.openWindow().workDir(request.getWorkDir()).appFile(request.getAppFile()).build();
		MainWindow mainWindow = new MainWindow(builderManager).init(WinLocator.root());
		context.setMainWindow(mainWindow);
	}

	@Override
	protected String getStatusInfo() {
		return "Application open";
	}
}
