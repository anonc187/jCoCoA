/**
 * File MaxSumFunctionSolver.java
 *
 * This file is part of the jCoCoA project.
 *
 * Copyright 2016 Anonymous
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.anon.cocoa.solvers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.anon.cocoa.MailMan;
import org.anon.cocoa.agents.ConstraintAgent;
import org.anon.cocoa.messages.HashMessage;
import org.anon.cocoa.messages.Message;
import org.anon.cocoa.variables.AssignmentMap;
import org.anon.cocoa.variables.CostMap;
import org.anon.cocoa.variables.DiscreteVariable;

/**
 * MaxSumFunctionSolver
 *
 * @author Anomymous
 * @version 0.1
 * @since 22 jan. 2016
 */
public class MaxSumFunctionSolver<T extends DiscreteVariable<V>, V> extends AbstractSolver<T, V>
        implements IterativeSolver, BiPartiteGraphSolver {

    protected final ConstraintAgent<T, V> constraintAgent;
    protected Map<UUID, CostMap<V>> receivedCosts;

    public MaxSumFunctionSolver(final ConstraintAgent<T, V> agent) {
        super(agent);
        this.constraintAgent = agent;
        this.receivedCosts = new HashMap<>();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.solvers.Solver#init()
     */
    @Override
    public final void init() {
        // Do nothing?
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.solvers.Solver#push(org.anon.cocoa.messages.Message)
     */
    @Override
    public synchronized void push(final Message m) {
        if (m.getType().equals("VAR2FUN")) {
            final UUID neighbor = m.getSource();
            @SuppressWarnings("unchecked")
            final CostMap<V> costMap = (CostMap<V>) m.getMap("costMap");
            this.receivedCosts.put(neighbor, costMap);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.solvers.Solver#reset()
     */
    @Override
    public void reset() {
        this.receivedCosts.clear();
    }

    /*
     * A message sent from a function-node f to a variable-node x in iteration i includes for each possible value d \in
     * Dx the minimal cost of any combination of assignments to the variables involved in f apart from x and the
     * assignment of value d to variable x.
     *
     * @see org.anon.cocoa.solvers.IterativeSolver#tick()
     */
    @Override
    public synchronized void tick() {
        // Only works for binary constraints
        assert (super.numNeighbors() == 2);

        for (final UUID target : this.parent.getConstrainedVariableIds()) {
            final Message f2v = this.fun2varmessage(target);
            MailMan.sendMessage(target, f2v);
        }

        this.receivedCosts.clear();
    }

    /**
     * TODO: Fix this one for more than 2 variables
     */
    protected final Message fun2varmessage(final UUID target) {
        // For all values of variable
        final CostMap<V> costMap = new CostMap<>();

        for (final V value : this.constraintAgent.getVariableWithID(target)) {
            final AssignmentMap<V> temp = new AssignmentMap<>();
            temp.put(target, value);

            // double minCost = Double.MAX_VALUE;

            final ArrayList<UUID> neighbors = new ArrayList<>(this.parent.getConstrainedVariableIds());
            neighbors.remove(target);

            final double minCost = this.findMin(temp, neighbors, 0);

            // // Now we know there is only one other neighbor, so iterate for him
            // for (final UUID other : this.parent.getConstrainedVariableIds()) {
            // if (other == target) {
            // continue;
            // }
            //
            // for (final V val2 : this.constraintAgent.getVariableWithID(other)) {
            // temp.put(other, val2);
            // double cost = this.parent.getLocalCostIf(temp);
            //
            // // What is this? Do I need it?
            // if (this.receivedCosts.containsKey(other) && this.receivedCosts.get(other).containsKey(val2)) {
            // cost += this.receivedCosts.get(other).get(val2);
            // }
            //
            // if (cost < minCost) {
            // minCost = cost;
            // }
            // }
            // }

            // The following can hold if there are no other variables, i.e. I a am a unary constraint
            // if (minCost == Double.MAX_VALUE) {
            // minCost = this.parent.getLocalCostIf(temp);
            // }

            costMap.put(value, minCost);
        }

        final Message msg = new HashMessage(this.constraintAgent.getID(), "FUN2VAR");
        msg.put("costMap", costMap);

        return msg;
    }

    /**
     * @param temp
     * @param neighbors
     * @param indices
     * @param i
     */
    protected double findMin(final AssignmentMap<V> temp, final ArrayList<UUID> neighbors, final int i) {
        if (neighbors.size() == i) {
            return this.parent.getLocalCostIf(temp);
        } else {
            final UUID neighbor = neighbors.get(i);
            double bestCost = Double.MAX_VALUE;
            for (final V val : this.constraintAgent.getVariableWithID(neighbor)) {
                temp.put(neighbor, val);
                double cost = this.findMin(temp, neighbors, i + 1);

                if (this.receivedCosts.containsKey(neighbor) && this.receivedCosts.get(neighbor).containsKey(val)) {
                    cost += this.receivedCosts.get(neighbor).get(val);
                }

                if (cost < bestCost) {
                    bestCost = cost;
                }
            }
            return bestCost;
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.solvers.BiPartiteGraphSolver#getCounterPart()
     */
    @Override
    public Class<? extends BiPartiteGraphSolver> getCounterPart() {
        return MaxSumVariableSolver.class;
    }

}
