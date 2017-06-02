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
public class WPTReceiverConstraint<T extends Variable<V>, V extends Number> extends HigherOrderConstraint<T, V> {

    private final double[] position;

    private final double mError;

    /**
     *
     */
    public WPTReceiverConstraint(final double[] pos) {
        this.position = pos;
        this.mError = 1.0;
    }

    /**
    *
    */
    public WPTReceiverConstraint(final double[] pos, final double mError) {
        this.position = pos;
        this.mError = mError;
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
        double receivedEnergy = 0.0;
        for (final T var : this.constrainedVariables.values()) {
            if (valueMap.containsAssignment(var)) {
                final double pathLoss = PathLossFactor.computePathLoss(this.position, (double[]) var.get("position"));

                // If the variable is set to the proposed value, act as is we ACTUALLY measured the received energy by
                // incorporating the error
                // if (var.isSet() && valueMap.getAssignment(var).equals(var.getValue())) {
                receivedEnergy += this.mError * valueMap.getAssignment(var).doubleValue() * pathLoss;
                // } else {
                // receivedEnergy += valueMap.getAssignment(var).doubleValue() * pathLoss;
                // }
            }
        }
        return -receivedEnergy;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.constraints.Constraint#getExternalCost()
     */
    @Override
    public double getExternalCost() {
        double receivedEnergy = 0.0;
        for (final T var : this.constrainedVariables.values()) {
            final double pathLoss = PathLossFactor.computePathLoss(this.position, (double[]) var.get("position"));
            receivedEnergy += this.mError * (var.getValue().doubleValue()) * pathLoss;
        }
        return -receivedEnergy;
    }

    public double getPower() {
        double receivedEnergy = 0.0;
        for (final T var : this.constrainedVariables.values()) {
            final double pathLoss = PathLossFactor.computePathLoss(this.position, (double[]) var.get("position"));
            receivedEnergy += this.mError * (var.getValue().doubleValue()) * pathLoss;
        }
        return receivedEnergy;
    }

}
