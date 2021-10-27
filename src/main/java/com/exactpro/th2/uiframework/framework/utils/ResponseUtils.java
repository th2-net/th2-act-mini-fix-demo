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

package com.exactpro.th2.uiframework.framework.utils;

import com.exactpro.th2.act.framework.InternalMessageType;
import com.exactpro.th2.act.framework.exceptions.UIFrameworkExecutionException;
import com.exactpro.th2.act.grpc.hand.RhBatchResponse;
import com.exactpro.th2.uiframework.ResponseData;

public class ResponseUtils {
	public static ResponseData getResultByIdOrThrow(RhBatchResponse response, String id) throws UIFrameworkExecutionException {
		return response.getResultList().stream()
				.filter(r -> r.getActionId().equals(id))
				.map(r -> new ResponseData(r.getResult(), response.getExecutionId(), InternalMessageType.convert(response.getMessageType())))
				.findFirst()
				.orElseThrow(() -> new UIFrameworkExecutionException("Unexpected response from hand"));
	}
}