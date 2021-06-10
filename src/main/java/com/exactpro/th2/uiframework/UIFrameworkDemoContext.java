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

package com.exactpro.th2.uiframework;

import com.exactpro.th2.act.framework.HandExecutor;
import com.exactpro.th2.act.framework.UIFrameworkContext;
import com.exactpro.th2.act.framework.builders.win.WinBuilderManager;
import com.exactpro.th2.act.grpc.hand.RhSessionID;
import com.exactpro.th2.uiframework.framework.components.MainWindow;

public class UIFrameworkDemoContext extends UIFrameworkContext {
	private MainWindow mainWindow;


	public UIFrameworkDemoContext(RhSessionID sessionID, HandExecutor handExecutor) {
		super(sessionID, handExecutor);
	}


	public WinBuilderManager createBuilderManager() {
		return new WinBuilderManager(this);
	}

	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	public MainWindow getMainWindow() {
		return mainWindow;
	}
}
