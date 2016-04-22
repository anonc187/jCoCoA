/**
 * File ConstraintAgent.java
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
package org.anon.cocoa.agents;

import java.util.Set;
import java.util.UUID;

import org.anon.cocoa.MailMan;
import org.anon.cocoa.constraints.BiPartiteConstraint;
import org.anon.cocoa.constraints.Constraint;
import org.anon.cocoa.exceptions.VariableNotInvolvedException;
import org.anon.cocoa.messages.Message;
import org.anon.cocoa.solvers.Solver;
import org.anon.cocoa.solvers.SolverRunner;
import org.anon.cocoa.variables.AssignmentMap;
import org.anon.cocoa.variables.Variable;

/**
 * ConstraintAgent
 *
 * @author Anomymous
 * @version 0.1
 * @since 8 apr. 2016
 */
public class ConstraintAgent<T extends Variable<V>, V> extends AbstractPropertyOwner implements Agent<T, V> {

	private final UUID address;
	private final String name;
	private final BiPartiteConstraint<T, V> myConstraint;

	Solver mySolver;

	/**
	 * @param name
	 * @param var
	 */
	public ConstraintAgent(String name, BiPartiteConstraint<T, V> constraint) {
		super();
		this.name = name;
		this.myConstraint = constraint;
		this.address = UUID.randomUUID();
		MailMan.register(this.address, this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.anon.cocoa.Agent#init()
	 */
	@Override
	public final synchronized void init() {
		this.mySolver.init();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.anon.cocoa.Agent#push(org.anon.cocoa.Message)
	 */
	@Override
	public final synchronized void push(Message m) {
		this.mySolver.push(m);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.anon.cocoa.Agent#reset()
	 */
	@Override
	public void reset() {
		this.mySolver.reset();
	}

	public final void setSolver(Solver solver) {
		this.mySolver = new SolverRunner(solver);
	}

	public final void setSolver(Solver solver, boolean asynchronous) {
		if (asynchronous) {
			this.mySolver = new SolverRunner(solver);
		} else {
			// System.err.println("Warning: You are using a synchronous solver!");
			this.mySolver = solver;
		}
	}

	public T getVariable(UUID id) {
		if (this.myConstraint.getFrom().getID().equals(id)) {
			return this.myConstraint.getFrom();
		} else if (this.myConstraint.getTo().getID().equals(id)) {
			return this.myConstraint.getTo();
		} else {
			throw new VariableNotInvolvedException("Variable not part of Constraint!");
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.anon.cocoa.agents.Agent#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @return
	 */
	public UUID getID() {
		return this.address;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.anon.cocoa.agents.Agent#getConstraintIds()
	 */
	@Override
	public Set<UUID> getConstraintIds() {
		return this.myConstraint.getVariableIds();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.anon.cocoa.agents.Agent#getLocalCost()
	 */
	@Override
	public double getLocalCost() {
		return this.myConstraint.getCost(this.myConstraint.getFrom())
				+ this.myConstraint.getCost(this.myConstraint.getTo());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.anon.cocoa.agents.Agent#getLocalCostIf(org.anon.cocoa.variables.AssignmentMap)
	 */
	@Override
	public double getLocalCostIf(AssignmentMap<V> valueMap) {
		return this.myConstraint.getCostIf(this.myConstraint.getFrom(), valueMap)
				+ this.myConstraint.getCostIf(this.myConstraint.getTo(), valueMap);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.anon.cocoa.agents.Agent#getVariable()
	 */
	@Override
	public T getVariable() {
		return null;
		// throw new UnsupportedOperationException("Cannot get the variable of ConstraintAgent");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.anon.cocoa.agents.Agent#addConstraint(org.anon.cocoa.constraints.Constraint)
	 */
	@Override
	public void addConstraint(Constraint<T, V> c) {
		throw new UnsupportedOperationException("Cannot add Constraints to ConstraintAgent");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.anon.cocoa.agents.Agent#removeConstraint(org.anon.cocoa.constraints.Constraint)
	 */
	@Override
	public void removeConstraint(Constraint<T, V> c) {
		throw new UnsupportedOperationException("Cannot remove Constraints to ConstraintAgent");
	}

}
