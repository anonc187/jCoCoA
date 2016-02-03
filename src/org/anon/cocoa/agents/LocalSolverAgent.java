/**
 * File LocalSolverAgent.java
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

import java.util.SortedSet;
import java.util.TreeSet;

import org.anon.cocoa.variables.Variable;

/**
 * Agents of the LocalCommunicationAgent can only communicate in a short range.
 * This class provides the functionality of that interface.
 * 
 * @author Anomymous
 * @version 0.1
 * @since 11 apr. 2014
 * 
 */
public class LocalSolverAgent extends AbstractSolverAgent implements LocalCommunicatingAgent {

	private final SortedSet<Agent> neighborhood;

	public LocalSolverAgent(String name, Variable<?> var) {
		super(name, var);
		this.neighborhood = new TreeSet<Agent>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.anon.cocoa.agents.LocalCommunicatingAgent
	 */
	@Override
	public final void addToNeighborhood(Agent agent) {
		this.neighborhood.add(agent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.anon.cocoa.agents.LocalCommunicatingAgent#getNeighborhood()
	 */
	@Override
	public final synchronized SortedSet<Agent> getNeighborhood() {
		return neighborhood;
	}

	@Override
	public void reset() {
		super.reset();
		this.neighborhood.clear();
	}

}
