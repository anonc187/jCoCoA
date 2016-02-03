/**
 * File LocalInequalityConstraintCostFunction.java
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
package org.anon.cocoa.costfunctions;

import java.util.HashMap;

import org.anon.cocoa.agents.Agent;
import org.anon.cocoa.agents.LocalCommunicatingAgent;
import org.anon.cocoa.exceptions.VariableNotSetException;
import org.anon.cocoa.problemcontexts.LocalProblemContext;
import org.anon.cocoa.problemcontexts.ProblemContext;

/**
 * LocalInequalityConstraintCostFunction
 * 
 * @author Anomymous
 * @version 0.1
 * @since 19 may 2014
 * 
 */
public class LocalInequalityConstraintCostFunction implements CostFunction {

	private final LocalCommunicatingAgent localAgent;

	/**
	 * @param Agent
	 */
	public LocalInequalityConstraintCostFunction(LocalCommunicatingAgent me) {
		this.localAgent = me;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.anon.cocoa.CostFunction#evaluate(org.anon.cocoa.IndexedProblemContext)
	 */
	@Override
	public double evaluate(ProblemContext<?> pc) {

		if (!(pc instanceof LocalProblemContext<?>))
			throw new RuntimeException(
					"Error using LocalInequalityConstraintCostFunction with invalid problemcontext");

		@SuppressWarnings("unchecked")
		LocalProblemContext<Integer> context = (LocalProblemContext<Integer>) pc;

		// Get the current assignment in the problemcontext
		HashMap<Agent, Integer> currentAssignments = context.getAssignment();

		if (!currentAssignments.containsKey(localAgent))
			throw new VariableNotSetException();
		int myAssignedValue = currentAssignments.get(this.localAgent);

		double cost = 0;
		for (Agent neighbor : this.localAgent.getNeighborhood()) {
			CompareCounter.compare();
			if (currentAssignments.containsKey(neighbor)
					&& myAssignedValue == currentAssignments.get(neighbor))
				cost++;
		}

		return cost;
	}
	
	@Override
	public double evaluateFull(ProblemContext<?> pc) {
		if (!(pc instanceof LocalProblemContext<?>))
			throw new RuntimeException(
					"Error using LocalInequalityConstraintCostFunction with invalid problemcontext");

		@SuppressWarnings("unchecked")
		LocalProblemContext<Integer> context = (LocalProblemContext<Integer>) pc;
		HashMap<Agent, Integer> currentAssignments = context.getAssignment();
		
		double cost = 0;
		
		for (Agent one : currentAssignments.keySet()) {
			for (Agent other : currentAssignments.keySet()) {
				if (one == other) continue;
				
				CompareCounter.compare();
				if (currentAssignments.get(one) == currentAssignments.get(other))
					cost++;
			}
		}
		
		return cost;
	}
	
}
