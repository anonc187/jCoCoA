/**
 * File IntegrationTest.java
 *
 * Copyright 2016 Anonymous
 */
package org.anon.cocoa;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import org.anon.cocoa.agents.Agent;
import org.anon.cocoa.agents.VariableAgent;
import org.anon.cocoa.constraints.Constraint;
import org.anon.cocoa.constraints.InequalityConstraint;
import org.anon.cocoa.exceptions.InvalidPropertyException;
import org.anon.cocoa.solvers.CoCoASolver;
import org.anon.cocoa.solvers.CoCoSolver;
import org.anon.cocoa.variables.IntegerVariable;
import org.anon.cocoa.variables.Variable;

/**
 * IntegrationTest
 *
 * @author Anomymous
 * @version 0.1
 * @since 3 okt. 2016
 */
public class IntegrationTest {

    @Test
    public void runTest() throws InterruptedException, InvalidPropertyException {
        final List<Variable> variables = new ArrayList<>();
        final List<Agent> agents = new ArrayList<>();

        for (int v = 0; v < 10; v++) {
            final IntegerVariable var = new IntegerVariable(1, 3);
            final VariableAgent agent = new VariableAgent<>(var, String.format("Agent %d", v));
            agent.setSolver(new CoCoASolver<>(agent));

            variables.add(var);
            agents.add(agent);
        }

        final int[][] edges = {{0, 4}, {1, 4}, {2, 7}, {3, 7}, {4, 6}, {4, 8}, {4, 9}, {5, 7}, {6, 7}};
        for (final int[] e : edges) {
            final Constraint c = new InequalityConstraint(variables.get(e[0]), variables.get(e[1]));
            agents.get(e[0]).addConstraint(c);
            agents.get(e[1]).addConstraint(c);
        }

        agents.get(0).set(CoCoSolver.ROOTNAME_PROPERTY, true);
        agents.get(0).init();
        Thread.sleep(1000);

        for (final Variable v : variables) {
            System.out.println(v.getValue());
        }
    }

}
