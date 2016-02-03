/**
 * File RandomAgent.java
 * 
 * This file is part of the jCoCoA project.
 *
 * Copyright 2014 Anomymous
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.anon.cocoa.agents.lazy;

import org.anon.cocoa.agents.AbstractAgent;
import org.anon.cocoa.exceptions.InvalidValueException;
import org.anon.cocoa.messages.Message;
import org.anon.cocoa.variables.Variable;

/**
 * RandomAgent
 *
 * @author Anomymous
 * @version 0.1
 * @since 24 okt. 2014
 *
 */
@Deprecated
public class RandomAgent<T> extends AbstractAgent {

	private final Variable<T> var;

	/**
	 * @param name
	 * @param var
	 */
	public RandomAgent(String name, Variable<T> var) {
		super(name, var);
		this.var = var;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anon.cocoa.agents.Agent#init()
	 */
	@Override
	public void init() {
		try {
			var.setValue(var.getRandomValue());
		} catch (InvalidValueException e) {
			System.err.println("Unable to set to random value");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anon.cocoa.agents.Agent#push(org.anon.cocoa.messages.Message)
	 */
	@Override
	public void push(Message m) {
		// Does nothing
	}

}
