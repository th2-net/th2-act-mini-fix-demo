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
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Set;

public class TransactionTab extends BaseTab<TransactionTab> {
	
	public TransactionTab(WinBuilderManager builders) {
		super(builders);
	}
	
	public static final String CHECKBOX_STATE_ATTR = "Toggle.ToggleState";
	public static final String NAME_ATTR = "Name";
	public static final String CHECKBOX_ON_STATE_VALUE = "1";
	public static final String CHECKBOX_OFF_STATE_VALUE = "0";
	
	private WinLocator tagsTable;
	private WinLocator transactionsTable;

	private WinLocator getTagsTableLocator() throws UIFrameworkBuildingException {
		if (this.tagsTable == null)
			tagsTable = this.findAndSaveLocators(WinLocator.root().byId("27"), "tags_table_hand_id", false);
		return tagsTable;
	}

	private WinLocator getTransactionsTable() throws UIFrameworkBuildingException {
		if (this.transactionsTable == null)
			transactionsTable = this.findAndSaveLocators(WinLocator.root().byId("25"), "transactions_table_hand_id", false);
		return transactionsTable;
	}

	public void selectTransaction(String name) throws UIFrameworkBuildingException {
		selectTransaction(name, null);
	}

	public void enableTags(Set<String> tags) throws UIFrameworkBuildingException {
		toggleTags(tags, true);
	}

	public void disableTags(Set<String> tags) throws UIFrameworkBuildingException {
		toggleTags(tags, false);
	}

	public void toggleTags(Set<String> tags, boolean targetState) throws UIFrameworkBuildingException {
		WinLocator table = getTagsTableLocator();
		for (String tag : tags) {
			WinLocator tagRow = table.byName(tag);
			String tagId = String.format("tag_%s_value", tag);
			String expectedState = targetState ? CHECKBOX_ON_STATE_VALUE : CHECKBOX_OFF_STATE_VALUE; 
			builders.getElAttribute().attributeName(CHECKBOX_STATE_ATTR).id(tagId)
					.winLocator(tagRow).build();
			builders.sendText().winLocator(tagRow.byName(tag)).execute(String.format("%s != %s", tagId, expectedState))
					.isDirectText(true).text(SendTextExtraButtons.SPACE.handCommand()).build();
		}
	}
	
	public void setTags(List<Pair<String, String>> tags) throws UIFrameworkBuildingException {
		WinLocator table = getTagsTableLocator();
		for (Pair<String, String> tagValue : tags) {
			String tagHandId = "tag_present_id";
			
			builders.checkElement().id(tagHandId).winLocator(table.byName(tagValue.getKey())).saveElement(true).build();
			
			addEditTagCommands(tagHandId, tagValue.getValue());
			addNewTagCommands(tagHandId, tagValue.getKey(), tagValue.getValue());
		}
	}
	
	private void addEditTagCommands(String id, String value) throws UIFrameworkBuildingException {
		String presentCondition = String.format("%s == 'found'", id);
		String tagValueHandId = "tag_value_id";
		String tagValueCondition = String.format("%s and %s != '%s'", presentCondition, tagValueHandId, value);
		builders.getElAttribute().winLocator(WinLocator.fromCachedId(id).byId("ListViewSubItem-1"))
				.attributeName(NAME_ATTR).id(tagValueHandId).execute(presentCondition).build();
		builders.click().execute(tagValueCondition).winLocator(WinLocator.fromCachedId(id)).build();
		//edit button
		builders.click().execute(tagValueCondition).winLocator(WinLocator.root().byId("5014")).build();
		builders.sendText().execute(tagValueCondition).winLocator(WinLocator.root().byId("1005")).text(value)
				.clearBefore(true).build();
		builders.click().execute(tagValueCondition).winLocator(WinLocator.root().byId("1")).build();
	}

	private void addNewTagCommands(String id, String tagName, String value) throws UIFrameworkBuildingException {
		String presentCondition = String.format("%s != 'found'", id);
		//new button
		builders.click().execute(presentCondition).winLocator(WinLocator.root().byId("5013")).build();
		builders.sendText().execute(presentCondition).winLocator(WinLocator.root().byId("1005")).text(tagName).build();
		builders.sendText().execute(presentCondition).winLocator(WinLocator.root().byId("1010")).text(value).build();
		builders.click().execute(presentCondition).winLocator(WinLocator.root().byId("1")).build();
	}

	public void send() throws UIFrameworkBuildingException {
		builders.click().winLocator(WinLocator.root().byId("5007")).build();
	}
	
	@Override
	protected TransactionTab getTab() {
		return this;
	}


	private void selectTransaction(String transactionName, String condition) throws UIFrameworkBuildingException {
		builders.sendText().winLocator(getTransactionsTable()).isDirectText(transactionName)
				.text(transactionName).execute(condition).build();
	}

	public void createTransaction(String transactionName) throws UIFrameworkBuildingException {
		String handId = "transAvailHId";
		//check that transaction doesn't already exist
		builders.checkElement().saveElement(false).winLocator(getTransactionsTable().byName(transactionName))
				.id(handId).build();
		String transExistsCondition = handId + " == 'found'";
		
		//remove old
		selectTransaction(transactionName, transExistsCondition);
		deleteTransaction(transactionName, transExistsCondition);
		
		builders.click().winLocator(WinLocator.root().byId("5008")).build();
		//name
		builders.sendText().winLocator(WinLocator.root().byId("1001")).text(transactionName).build();
		builders.click().winLocator(WinLocator.root().byId("1")).build();
	}

	private void deleteTransaction(String name, String condition) throws UIFrameworkBuildingException {
		String transactionNameId = "dialogTextId";
		builders.click().execute(condition).winLocator(WinLocator.root().byId("5010")).build();
		builders.getElAttribute().execute(condition).winLocator(WinLocator.root().byId("65535"))
				.attributeName(NAME_ATTR).id(transactionNameId).build();
		String checkingDialogCond = transactionNameId + " contains '" + name + "'";
		condition = (condition != null) ? condition + " and " + checkingDialogCond : checkingDialogCond;
		//confirmation
		builders.click().execute(condition).winLocator(WinLocator.root().byId("6")).build();
	}

	public void deleteTransaction(String name) throws UIFrameworkBuildingException {
		deleteTransaction(name, null);
	}
}
