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

package com.exactpro.th2.uiframework.framework.components.tabs;

import com.exactpro.th2.act.framework.builders.win.WinBuilderManager;
import com.exactpro.th2.act.framework.builders.win.WinLocator;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.framework.ui.constants.SendTextExtraButtons;

import java.util.List;

public class TransactionTab extends BaseTab<TransactionTab> {
	public TransactionTab(WinBuilderManager builders) {
		super(builders);
	}


	public void selectNewOrderSingleTransaction() throws UIFrameworkBuildingException {
		selectTransaction("NewOrderSingle");
	}

	public void toggleTags(List<String> tags) throws UIFrameworkBuildingException {
		for (String tag : tags) {
			builders.sendText().winLocator(WinLocator.root().byId("27").byName(tag).byName(tag))
					.isDirectText(true).text(SendTextExtraButtons.SPACE.handCommand()).build();
		}
	}

	public void send() throws UIFrameworkBuildingException {
		builders.click().winLocator(WinLocator.root().byId("5007")).build();
	}


	@Override
	protected TransactionTab getTab() {
		return this;
	}


	private void selectTransaction(String transactionName) throws UIFrameworkBuildingException {
		builders.sendText().winLocator(WinLocator.root().byId("25")).isDirectText(transactionName)
				.text(transactionName).build();
	}
}
