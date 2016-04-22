/**
 * File MaxSumADVPVariableSolver.java
 *
 * This file is part of the jCoCoA project.
 *
 * Copyright 2016 Anonymous
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
package org.anon.cocoa.solvers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.anon.cocoa.MailMan;
import org.anon.cocoa.agents.Agent;
import org.anon.cocoa.messages.HashMessage;
import org.anon.cocoa.messages.Message;
import org.anon.cocoa.variables.CostMap;
import org.anon.cocoa.variables.IntegerVariable;

/**
 * MaxSumADVPVariableSolver
 *
 * @author Anomymous
 * @version 0.1
 * @since 22 jan. 2016
 */
public class MaxSumADVPVariableSolver extends AbstractSolver<IntegerVariable, Integer>
		implements IterativeSolver, BiPartiteGraphSolver {

	private final static int START_VP_AFTER_SWITCHES = 2;
	private final static int REVERSE_AFTER_ITERS = 100;

	private int iterCount;
	private boolean direction;
	private Map<UUID, CostMap<Integer>> receivedCosts;

	private int switchCount;
	private boolean doVP;

	public MaxSumADVPVariableSolver(Agent<IntegerVariable, Integer> agent) {
		super(agent);
		this.receivedCosts = new HashMap<>();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.anon.cocoa.solvers.Solver#init()
	 */
	@Override
	public synchronized void init() {
		this.iterCount = 0;
		this.direction = true;
		this.switchCount = 0;
		this.doVP = false;
	}

	@Override
	public synchronized void push(Message m) {
		UUID neighbor = m.getUUID("source");
		@SuppressWarnings("unchecked")
		CostMap<Integer> costMap = (CostMap<Integer>) m.getMap("costMap");
		this.receivedCosts.put(neighbor, costMap);
	}

	@Override
	public void reset() {
		this.receivedCosts.clear();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.anon.cocoa.solvers.IterativeSolver#tick()
	 */
	@Override
	public synchronized void tick() {
		this.iterCount++;
		if (this.iterCount % MaxSumADVPVariableSolver.REVERSE_AFTER_ITERS == 0) {
			this.direction = !this.direction;
			this.switchCount++;
			this.doVP = (this.switchCount >= MaxSumADVPVariableSolver.START_VP_AFTER_SWITCHES);
		}

		// Target represents function node f
		for (UUID target : this.parent.getConstraintIds()) {
			if ((target.hashCode() > this.parent.hashCode()) == this.direction) {
				continue;
			}

			Message v2f = this.var2funMessage(target);
			if (this.doVP) {
				v2f.put("value", this.myVariable.getValue());
			}

			MailMan.sendMessage(target, v2f);
		}

		this.setMinimizingValue();
		// this.receivedCosts.clear();
	}

	protected Message var2funMessage(UUID target) {
		// For all values of variable
		CostMap<Integer> costMap = new CostMap<>();
		double totalCost = 0;

		for (Integer value : this.myVariable) {
			double valueCost = 0;

			// The sum of costs for this value it received from all function neighbors apart from f in iteration i −
			// 1.
			for (UUID neighbor : this.parent.getConstraintIds()) {
				if (neighbor != target) {
					if (this.receivedCosts.containsKey(neighbor)
							&& this.receivedCosts.get(neighbor).containsKey(value)) {
						valueCost += this.receivedCosts.get(neighbor).get(value);
					}
				}
			}

			totalCost += valueCost;
			costMap.put(value, valueCost);
		}

		// Normalize to avoid increasingly large values
		double avg = totalCost / costMap.size();
		for (Integer value : this.myVariable) {
			costMap.put(value, costMap.get(value) - avg);
		}

		Message msg = new HashMessage("VAR2FUN");
		msg.put("source", this.myVariable.getID());
		msg.put("costMap", costMap);
		return msg;
	}

	protected void setMinimizingValue() {
		double minCost = Double.MAX_VALUE;
		Integer bestAssignment = null;

		for (Integer value : this.myVariable) {
			double valueCost = 0;
			for (UUID neighbor : this.parent.getConstraintIds()) {
				if (this.receivedCosts.containsKey(neighbor) && this.receivedCosts.get(neighbor).containsKey(value)) {
					valueCost += this.receivedCosts.get(neighbor).get(value);
				}
			}

			if (valueCost < minCost) {
				minCost = valueCost;
				bestAssignment = value;
			}
		}
		this.myVariable.setValue(bestAssignment);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.anon.cocoa.solvers.BiPartiteGraphSolver#getCounterPart()
	 */
	@Override
	public Class<? extends BiPartiteGraphSolver> getCounterPart() {
		return MaxSumADVPFunctionSolver.class;
	}
}
