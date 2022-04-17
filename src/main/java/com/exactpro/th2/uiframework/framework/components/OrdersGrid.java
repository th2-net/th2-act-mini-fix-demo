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
import com.exactpro.th2.act.framework.builders.win.WinBuilderManager;
import com.exactpro.th2.act.framework.builders.win.WinLocator;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkBuildingException;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkExecutionException;
import com.exactpro.th2.act.framework.ui.WinUIElement;
import com.exactpro.th2.act.grpc.hand.RhBatchResponse;
import com.exactpro.th2.uiframework.ResponseData;
import com.exactpro.th2.uiframework.framework.utils.ResponseUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrdersGrid extends WinUIElement {
	private static final String NAME_ATTRIBUTE = "Name";
	private static final String HEADER_ATTRIBUTE = "Header";
	private static final String AUTOMATION_ID_ATTRIBUTE = "AutomationId";
	private static final String ORDER_CELL_XPATH_FORMAT = "List/ListItem[%s]/Text[%s]";
	private static final Pattern CELL_INDEX_PATTERN = Pattern.compile("HeaderItem (\\d*)");

	private WinLocator gridLocator;
	private WinLocator headerLocator;


	public OrdersGrid(WinBuilderManager builders) {
		super(builders);
	}


	public OrdersGrid init(WinLocator gridLocator) throws UIFrameworkBuildingException {
		this.gridLocator = findAndSaveLocators(gridLocator, "ordersGridElId", false);
		this.headerLocator = findAndSaveLocators(gridLocator.byId(HEADER_ATTRIBUTE), "ordersHeaderElId", false);
		return this;
	}

	public Map<String, ResponseData> extractLastOrderFields(List<String> extractionFields) throws UIFrameworkBuildingException, UIFrameworkExecutionException {
		return extractOrderFields(extractionFields, 1);
	}

	public Map<String, ResponseData> extractOrderFields(List<String> extractionFields, int rowNumber) throws UIFrameworkExecutionException, UIFrameworkBuildingException {
		if (rowNumber < 0)
			return Collections.emptyMap();

		Map<String, ResponseData> result = new HashMap<>(extractionFields.size());
		for (String headerName : extractionFields) {
			int cellNumber = getCellNumber(headerName);
			if (cellNumber < 0)
				continue;

			result.put(headerName, extractField(rowNumber, cellNumber, headerName));
		}
		return result;
	}


	private ResponseData extractField(int rowNumber, int cellNumber, String headerName) throws UIFrameworkExecutionException, UIFrameworkBuildingException {
		String id = "extractCellNameElId_" + cellNumber;
		String cellXpath = String.format(ORDER_CELL_XPATH_FORMAT, rowNumber, cellNumber);
		builders.getElAttribute().id(id).winLocator(gridLocator.byXpath(cellXpath)).attributeName(NAME_ATTRIBUTE).build();
		ExecutionParams executionParams = ExecutionParams.builder()
				.setEventName("getCellValueFor_" + headerName)
				.build();
		RhBatchResponse response = builders.getContext().submit(executionParams);
		return ResponseUtils.getResultByIdOrThrow(response, id);
	}

	private int getCellNumber(String headerName) throws UIFrameworkExecutionException, UIFrameworkBuildingException {
		String id = "getCellNumberForHeader_" + headerName;
		builders.getElAttribute().id(id).winLocator(headerLocator.byName(headerName)).attributeName(AUTOMATION_ID_ATTRIBUTE).build();
		ExecutionParams executionParams = ExecutionParams.builder()
				.setEventName("getColumnIndexFor_" + headerName)
				.build();
		RhBatchResponse response = builders.getContext().submit(executionParams);
		ResponseData headerWithCellNumber = ResponseUtils.getResultByIdOrThrow(response, id);

		Matcher cellMatcher = CELL_INDEX_PATTERN.matcher(headerWithCellNumber.getValue());
		if (!cellMatcher.matches())
			return -1;

		return Integer.parseInt(cellMatcher.group(1)) + 1;
	}
}
