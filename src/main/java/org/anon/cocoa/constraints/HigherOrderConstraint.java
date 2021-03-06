/**
 * File HigherOrderConstraint.java
 *
 * Copyright 2016 Anonymous
 */
package org.anon.cocoa.constraints;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.anon.cocoa.exceptions.VariableNotInvolvedException;
import org.anon.cocoa.variables.Variable;

/**
 * HigherOrderConstraint
 *
 * @author Anomymous
 * @version 0.1
 * @since 23 sep. 2016
 */
public abstract class HigherOrderConstraint<T extends Variable<V>, V> implements Constraint<T, V> {

    protected final Map<UUID, T> constrainedVariables;

    public HigherOrderConstraint() {
        this.constrainedVariables = new HashMap<>();
    }

    @Override
    public Set<UUID> getVariableIds() {
        return this.constrainedVariables.keySet();
    }

    public void addVariable(T var) {
        this.constrainedVariables.put(var.getID(), var);
    }

    public void removeVariable(T var) {
        this.constrainedVariables.remove(var.getID());
    }

    public boolean containsVariable(T var) {
        return this.constrainedVariables.containsKey(var.getID());
    }

    protected void assertVariableIsInvolved(T var) {
        if (!this.containsVariable(var)) {
            throw new VariableNotInvolvedException("Variable " + var + " is not involved in the constraint");
        }
    }

}
