/**
 * File ConstraintAgent.java
 *
 * Copyright 2016 Anonymous
 */
package org.anon.cocoa.agents;

import java.util.UUID;

import org.anon.cocoa.variables.Variable;

/**
 * ConstraintAgent
 *
 * @author Anomymous
 * @version 0.1
 * @since 23 sep. 2016
 */
public interface ConstraintAgent<T extends Variable<V>, V> extends Agent<T, V> {

    /**
     * @return
     */
    public UUID getID();

    /**
     * @param id
     * @return
     */
    public T getVariableWithID(UUID id);

}
