/**
 * File CostFunction.java
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

import org.anon.cocoa.problemcontexts.ProblemContext;

/**
 * CostFunction
 * 
 * @author Anomymous
 * @version 0.1
 * @since 19 mei 2014
 * 
 */
public interface CostFunction {

	/**
	 * @param context
	 * @return
	 */
	public double evaluate(ProblemContext<?> context);
	
	/**
	 * Disregarding any local contexts, compute full problem context costs
	 * 
	 * @param context
	 * @return
	 */
	public double evaluateFull(ProblemContext<?> context);
}
