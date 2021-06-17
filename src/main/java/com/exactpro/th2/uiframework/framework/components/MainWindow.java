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

import com.exactpro.th2.act.framework.builders.win.WinBuilderManager;
import com.exactpro.th2.act.framework.builders.win.WinLocator;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.uiframework.framework.components.tabs.SessionTab;
import com.exactpro.th2.uiframework.framework.components.tabs.TransactionTab;
import org.apache.commons.lang3.StringUtils;

public class MainWindow extends BaseWindow<MainWindow> {
	private OrdersGrid ordersGrid;
	private SystemMessagesGrid systemMessagesGrid;


	public MainWindow(WinBuilderManager builders) {
		super(builders);
	}

	@Override
	protected String getWindowId() {
		return "mainWindowElId";
	}

	@Override
	protected MainWindow getWindow() {
		return this;
	}

	public SessionTab openSessionTab() throws UIFrameworkBuildingException {
		builders.click().winLocator(windowLocator.byId("5004").byName("Session")).build();
		return new SessionTab(builders).init(windowLocator);
	}

	public TransactionTab openTransactionsTab() throws UIFrameworkBuildingException {
		builders.click().winLocator(windowLocator.byId("5004").byName("Transactions")).build();
		return new TransactionTab(builders).init(windowLocator);
	}

	public void connect(String host, String port) throws UIFrameworkBuildingException {
		connect(createConnectionString(host, port));
	}

	public void connect(String connectionString) throws UIFrameworkBuildingException {
		builders.click().winLocator(windowLocator.byId("5052").byId("5053").byName("Connect")).build();
		if (!connectionString.isEmpty())
			builders.sendText().winLocator(windowLocator.byId("1001")).text(connectionString).clearBefore(true)
					.isDirectText(true).build();
		builders.click().winLocator(windowLocator.byId("1")).build();
	}

	public void closeConnection() throws UIFrameworkBuildingException {
		builders.click().winLocator(windowLocator.byId("5052").byId("5053").byName("Disconnect")).build();
	}

	public OrdersGrid getOrders() throws UIFrameworkBuildingException {
		if (ordersGrid == null) {
			ordersGrid = new OrdersGrid(builders).init(WinLocator.root().byId("39"));
		}

		return ordersGrid;
	}

	public SystemMessagesGrid getSystemMessagesGrid() {
		if (systemMessagesGrid == null) {
			systemMessagesGrid = new SystemMessagesGrid(builders).init(WinLocator.root().byId("23"));
		}

		return systemMessagesGrid;
	}


	private String createConnectionString(String host, String port) {
		return createConnectionString(host, port, ":");
	}

	private String createConnectionString(String host, String port, String delimiter) {
		if (host.isEmpty() && port.isEmpty())
			return StringUtils.EMPTY;
		return host + delimiter + port;
	}
}
