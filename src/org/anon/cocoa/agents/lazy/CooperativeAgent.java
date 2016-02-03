/**
 * File CooperativeAgent.java
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
package org.anon.cocoa.agents.lazy;

import org.anon.cocoa.agents.LocalSolverAgent;
import org.anon.cocoa.costfunctions.LocalInequalityConstraintCostFunction;
import org.anon.cocoa.solvers.GreedyCooperativeSolver;
import org.anon.cocoa.variables.Variable;

/**
 * CooperativeAgent
 * 
 * @author Anomymous
 * @version 0.1
 * @since 11 apr. 2014
 * 
 */
@Deprecated
public class CooperativeAgent extends LocalSolverAgent {

	private final LocalInequalityConstraintCostFunction costfun;

	/**
	 * @param name
	 * @param var
	 */
	public CooperativeAgent(String name, Variable<?> var) {
		super(name, var);

		this.costfun = new LocalInequalityConstraintCostFunction(this);
		this.setSolver(new GreedyCooperativeSolver(this, costfun), true);
	}

}
