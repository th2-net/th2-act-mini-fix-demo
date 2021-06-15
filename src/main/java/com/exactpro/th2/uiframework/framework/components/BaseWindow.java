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
import com.exactpro.th2.act.framework.ui.constants.SendTextExtraButtons;
import com.exactpro.th2.act.framework.ui.utils.UIUtils;

public abstract class BaseWindow<T extends BaseWindow<T>> extends WinUIElement {
	protected WinLocator windowLocator;


	public BaseWindow(WinBuilderManager builders) {
		super(builders);
	}


	public T init(WinLocator windowLocator) throws UIFrameworkBuildingException {
		this.windowLocator = findAndSaveLocators(windowLocator, "mainWindowElId", false);
		return getWindow();
	}

	public void maximize() throws UIFrameworkBuildingException {
		this.builders.sendText().winLocator(windowLocator).isDirectText(true)
				.text(UIUtils.keyCombo(SendTextExtraButtons.WINDOWS, SendTextExtraButtons.UP)).build();
	}

	public void minimize() throws UIFrameworkBuildingException {
		this.builders.sendText().winLocator(windowLocator).isDirectText(true)
				.text(UIUtils.keyCombo(SendTextExtraButtons.WINDOWS, SendTextExtraButtons.DOWN)).build();
	}

	public void close() throws UIFrameworkBuildingException {
		this.builders.sendText().winLocator(windowLocator).isDirectText(true)
				.text(UIUtils.keyCombo(SendTextExtraButtons.ALT, SendTextExtraButtons.F4)).build();
	}


	protected abstract String getWindowId();
	protected abstract T getWindow();
}
