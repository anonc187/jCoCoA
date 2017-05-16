/**
 * File VariableAgent.java
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
package org.anon.cocoa.agents;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.anon.cocoa.variables.DiscreteVariable;

/**
 * VariableAgent
 *
 * @author Anomymous
 * @version 0.1
 * @since 8 apr. 2016
 */
public class VariableAgent<T extends DiscreteVariable<V>, V> extends MultiSolverAgent<T, V> {

    private final Set<UUID> functionAddresses;

    /**
     * @param var
     * @param name
     */
    public VariableAgent(final T var, final String name, final boolean synchronous) {
        super(var, name, synchronous);
        this.functionAddresses = new HashSet<>();
    }

    public VariableAgent(final T var, final String name) {
        this(var, name, SolverAgent.MULTI_THREADED);
    }

    public Set<UUID> getFunctionAdresses() {
        return this.functionAddresses;
    }

    public void addFunctionAddress(final UUID id) {
        this.functionAddresses.add(id);
    }

}
