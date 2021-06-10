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
import com.exactpro.th2.act.framework.ui.WinUIElement;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OrdersGrid extends WinUIElement {
	private WinLocator gridLocator;


	public OrdersGrid(WinBuilderManager builders) {
		super(builders);
	}


	public OrdersGrid init(WinLocator gridLocator) throws UIFrameworkBuildingException {
		this.gridLocator = findAndSaveLocators(gridLocator, "ordersGridElId", false);
		return this;
	}

	public Map<String, String> extractLastOrderFields(Map<String, String> extractionDetails) {
		if (extractionDetails.isEmpty())
			return Collections.emptyMap();

		WinLocator orderLocator = gridLocator.byId("ListViewItem-0");
		

		Map<String, String> result = new HashMap<>(extractionDetails.size());
		
		return result;
	}
}
