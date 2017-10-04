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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.anon.cocoa.agents;

import org.anon.cocoa.messages.Message;
import org.anon.cocoa.solvers.IterativeSolver;
import org.anon.cocoa.solvers.Solver;
import org.anon.cocoa.solvers.SolverRunner;
import org.anon.cocoa.variables.Variable;

/**
 * In another layer of hierarchy the SolverAgent provides the functionality that any agent has that contains a solver.
 * The function of this class is to hide the Solving functionality from the rest of the agent.
 *
 * @author Anomymous
 * @version 0.1
 * @since 11 apr. 2014
 *
 */
public class SingleSolverAgent<T extends Variable<V>, V> extends SolverAgent<T, V> {

    private Solver mySolver;

    /**
     * @param name
     * @param var
     */
    public SingleSolverAgent(final T var, final String name) {
        super(var, name);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.Agent#init()
     */
    @Override
    public final synchronized void init() {
        // this.startThread();

        if (this.mySolver != null) {
            this.mySolver.init();
        } else {
            throw new RuntimeException("Solver must be set!");
        }
    }

    /**
     *
     */
    // private final void startThread() {
    // if (this.synchronous) {
    // return;
    // }
    //
    // // Start the runner threads
    // if (this.mySolver != null) {
    // ((SolverRunner) this.mySolver).startThread();
    // }
    // }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.Agent#push(org.anon.cocoa.Message)
     */
    @Override
    public final synchronized void push(final Message m) {
        this.mySolver.push(m);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.solvers.Solver#tick()
     */
    @Override
    public void tick() {
        if (this.mySolver instanceof IterativeSolver) {
            ((IterativeSolver) this.mySolver).tick();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.Agent#reset()
     */
    @Override
    public void reset() {
        super.reset();

        if (this.mySolver != null) {
            this.mySolver.reset();
        }
    }

    /**
     * Updates the solver of the Agent. If a solver was already running, it will try to stop that running solver and
     * update it with the new one. Note that the {@link Solver#init()} function is not called on the solver during this
     * process.
     *
     * @param solver The new solver to be used by this agent
     */
    @Override
    public final void setSolver(final Solver solver) {
        if (solver == null) {
            this.mySolver = null;
        } else if (this.singleThreaded) {
            this.mySolver = solver;
        } else {
            this.mySolver = new SolverRunner(solver);
        }
    }

    // @Override
    // public boolean isFinished() {
    // return this.mySolver.emptyQueue();
    // }

}
