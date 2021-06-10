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
import com.exactpro.th2.act.framework.ui.utils.UIUtils;

public class SessionTab extends BaseTab<SessionTab> {
	public SessionTab(WinBuilderManager builders) {
		super(builders);
	}


	public void setSenderCompId(String senderCompId) throws UIFrameworkBuildingException {
		setField("28", senderCompId);
	}

	public void setTargetCompId(String targetCompId) throws UIFrameworkBuildingException {
		setField("29", targetCompId);
	}

	public void setInMsgSeq(String msgSeq) throws UIFrameworkBuildingException {
		setField("30", msgSeq);
	}

	public void setOutMsgSeq(String msgSeq) throws UIFrameworkBuildingException {
		setField("31", msgSeq);
	}

	public void setHearthBeatInterval(String interval) throws UIFrameworkBuildingException {
		setField("33", interval);
	}

	public void setFixVersion(String fixVersion) throws UIFrameworkBuildingException {
		setField("32", fixVersion);
	}

	public void resetSession() throws UIFrameworkBuildingException {
		builders.click().winLocator(tabLocator.byId("5033")).build();
		WinLocator modalLocator = WinLocator.root().byName("Question");
		builders.waitForElement().winLocator(modalLocator).timeout(15).build();
		builders.sendText().winLocator(modalLocator).isDirectText(true)
				.text(UIUtils.keyCombo(SendTextExtraButtons.ALT, "y")).build();
	}


	@Override
	protected SessionTab getTab() {
		return this;
	}


	private void setField(String locatorId, String value) throws UIFrameworkBuildingException {
		if (value.isEmpty())
			return;
		builders.sendText().winLocator(tabLocator.byId(locatorId)).text(value).clearBefore(true).build();
	}
}
