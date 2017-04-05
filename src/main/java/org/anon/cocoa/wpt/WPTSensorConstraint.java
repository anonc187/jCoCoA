/**
 * File WPTSensorConstraint.java
 *
 * Copyright 2016 Anonymous
 */
package org.anon.cocoa.wpt;

import org.anon.cocoa.constraints.CompareCounter;
import org.anon.cocoa.constraints.HigherOrderConstraint;
import org.anon.cocoa.variables.AssignmentMap;
import org.anon.cocoa.variables.Variable;

/**
 * WPTSensorConstraint
 *
 * @author Anomymous
 * @version 0.1
 * @since 23 sep. 2016
 */
public class WPTSensorConstraint<T extends Variable<V>, V extends Number> extends HigherOrderConstraint<T, V> {

    public static final double THRESHOLD = 0.018;
    public static final double COST = 1e9;

    private final double[] position;

    /**
     *
     */
    public WPTSensorConstraint(final double[] pos) {
        this.position = pos;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.constraints.Constraint#getCost(org.anon.cocoa.variables.Variable)
     */
    @Override
    public double getCost(final T targetVariable) {
        CompareCounter.compare();
        return this.getExternalCost();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.constraints.Constraint#getCostIf(org.anon.cocoa.variables.Variable,
     * org.anon.cocoa.variables.AssignmentMap)
     */
    @Override
    public double getCostIf(final T variable, final AssignmentMap<V> valueMap) {
        CompareCounter.compare();
        double receivedEnergy = 0;
        for (final T var : this.constrainedVariables.values()) {
            if (valueMap.containsAssignment(var)) {
                final double pathLoss = PathLossFactor.computePathLoss(this.position, (double[]) var.get("position"));
                receivedEnergy += valueMap.getAssignment(var).doubleValue() * pathLoss;
            }
        }
        return receivedEnergy < WPTSensorConstraint.THRESHOLD ? 0 : WPTSensorConstraint.COST;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.constraints.Constraint#getExternalCost()
     */
    @Override
    public double getExternalCost() {
        double receivedEnergy = 0;
        for (final T var : this.constrainedVariables.values()) {
            final double pathLoss = PathLossFactor.computePathLoss(this.position, (double[]) var.get("position"));
            receivedEnergy += (var.getValue().doubleValue()) * pathLoss;
        }
        return receivedEnergy < WPTSensorConstraint.THRESHOLD ? 0 : WPTSensorConstraint.COST;
    }

}
