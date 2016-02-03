/**
 * File AbstractAgent.java
 *
 * This file is part of the jCoCoA project 2014.
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
package org.anon.cocoa.agents;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.anon.cocoa.exceptions.InvalidPropertyException;
import org.anon.cocoa.exceptions.PropertyNotSetException;
import org.anon.cocoa.messages.Message;
import org.anon.cocoa.variables.Variable;

/**
 * The abstract agent defines some bare essentials for any agent to provide. It
 * contains the name of the agent as well as the variable it is assigned to.
 * 
 * @author Anomymous
 * @version 0.1
 * @since 4 feb. 2014
 * 
 */
public abstract class AbstractAgent implements Agent, Comparable<Agent> {

	private final Map<String, Object> properties;

	private final String name;

	private final Variable<?> variable;

	private final static HashSet<Agent> allAgents = new HashSet<Agent>();

	protected AbstractAgent(String name, Variable<?> var) {
		this.name = name;
		this.variable = var;
		this.properties = new HashMap<String, Object>();
		allAgents.add(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anon.cocoa.Agent#getName()
	 */
	@Override
	public final String getName() {
		return this.name;
	}

	@Override
	public final String toString() {
		return "" + this.getClass().getSimpleName() + " " + this.name + " ("
				+ this.getVariable() + ")";
	}

	@Override
	public final synchronized Variable<?> getVariable() {
		return this.variable;
	}

	@Override
	public void reset() {
		if (this.variable != null)
			this.variable.clear();
	}

	/**
     * 
     */
	public final static void broadCast(Message m) {
		for (Agent a : allAgents) {
			a.push(m.clone());
		}
	}

	public final static void destroyAgents() {
		for (Agent a : allAgents) {
			Variable<?> var = a.getVariable();
			if (var != null)
				var.clear();
			
			a.reset();
		}
		allAgents.clear();
	}

	@Override
	public final boolean has(String key) {
		return this.properties.containsKey(key);
	}
	
	@Override
	public final Object get(String key) throws PropertyNotSetException {
		if (!this.properties.containsKey(key))
			throw new PropertyNotSetException(key);

		return this.properties.get(key);
	}

	@Override
	public final void set(String key, Object val)
			throws InvalidPropertyException {
		if (key == null || key.isEmpty())
			throw new InvalidPropertyException("Property name cannot be empty");

		this.properties.put(key, val);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Agent o) {
		return this.name.compareTo(o.getName());
	}
}
