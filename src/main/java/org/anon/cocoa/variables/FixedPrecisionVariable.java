/**
 * File FixedPrecisionVariable.java
 *
 * Copyright 2016 Anonymous
 */
package org.anon.cocoa.variables;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import org.anon.cocoa.agents.AbstractPropertyOwner;
import org.anon.cocoa.exceptions.InvalidDomainException;
import org.anon.cocoa.exceptions.InvalidValueException;
import org.anon.cocoa.exceptions.VariableNotSetException;

/**
 * FixedPrecisionVariable
 *
 * @author Anomymous
 * @version 0.1
 * @since 23 sep. 2016
 */
public class FixedPrecisionVariable extends AbstractPropertyOwner implements DiscreteVariable<Double> {

    private static final double UNDEFINED = -Double.MAX_VALUE;

    private static int unnamedVariableSequence = 0;

    private final UUID id;

    private final double lowerBound;
    private final double upperBound;
    private final double step;
    private final LinkedList<Double> attainableValues;

    private final String name;

    private boolean set = false;
    private double value = FixedPrecisionVariable.UNDEFINED;

    /**
     * Creates a variable with a given lower bound and upper bound. Future calls to setValue will never be able to set
     * the Variable value to something higher or lower than these bounds' values
     *
     * @param lowerBound
     *            the lower bound of the variable domain
     * @param upperBound
     *            the upper bound of the variable domain
     *
     * @throws InvalidDomainException
     *             exception is the lower bound is higher than the upper bound
     */
    public FixedPrecisionVariable(final double lowerBound, final double upperBound, final double step) {
        this(lowerBound,
                upperBound,
                step,
                "MyFixedPrecisionVariable" + FixedPrecisionVariable.unnamedVariableSequence++);
    }

    /**
     * Creates a variable with a given lower bound and upper bound. Future calls to setValue will never be able to set
     * the Variable value to something higher or lower than these bounds' values. Also provide the variable with a name
     * which can be used to identify it later.
     *
     * @param lowerBound
     *            the lower bound of the variable domain
     * @param upperBound
     *            the upper bound of the variable domain
     * @param name
     *            The name of the variable
     *
     * @throws InvalidDomainException
     *             exception is the lower bound is higher than the upper bound
     *
     * @see #IntegerVariable(Integer, Integer)
     */
    public FixedPrecisionVariable(final double lowerBound,
            final double upperBound,
            final double step,
            final String name) throws InvalidDomainException {
        if ((lowerBound > upperBound) || (step <= 0) || (step > (upperBound - lowerBound))) {
            throw new InvalidDomainException();
        }

        this.name = name;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.step = step;

        this.attainableValues = new LinkedList<>();
        for (double d = lowerBound; d <= upperBound; d += step) {
            this.attainableValues.add(new Double(d));
        }

        this.id = UUID.randomUUID();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.variables.Variable#clear()
     */
    @Override
    public void clear() {
        this.set = false;
        this.value = FixedPrecisionVariable.UNDEFINED;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.variables.Variable#clone()
     */
    @Override
    public FixedPrecisionVariable clone() {
        FixedPrecisionVariable ret = null;
        try {
            ret = new FixedPrecisionVariable(this.lowerBound, this.upperBound, this.step, this.name + " (clone)");
            ret.value = this.value;
        } catch (final InvalidDomainException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.variables.Variable#getLowerBound()
     */
    @Override
    public Double getLowerBound() {
        return this.lowerBound;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.variables.Variable#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.variables.Variable#getRandomValue()
     */
    @Override
    public Double getRandomValue() {
        return this.lowerBound + (Math.floor(this.getRange() * Math.random()) * this.step);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.variables.Variable#getUpperBound()
     */
    @Override
    public Double getUpperBound() {
        return this.attainableValues.getLast();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.variables.Variable#getValue()
     */
    @Override
    public Double getValue() throws VariableNotSetException {
        if (!this.set) {
            throw new VariableNotSetException();
        }

        return this.value;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.variables.Variable#isSet()
     */
    @Override
    public boolean isSet() {
        return this.set;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.variables.Variable#setValue(java.lang.Object)
     */
    @Override
    public Variable<Double> setValue(final Double value) throws InvalidValueException {
        if ((value < this.lowerBound) || (value > this.upperBound)) {
            throw new InvalidValueException(value);
        } else if (!this.attainableValues.contains(value)) {
            Double best = this.lowerBound;
            double mindiff = Double.MAX_VALUE;
            for (final Double possible : this.attainableValues) {
                final double diff = Math.abs(possible - value);
                if (diff < mindiff) {
                    mindiff = diff;
                    best = possible;
                }
            }
            this.value = best;
        } else {
            this.value = value;
        }
        this.set = true;
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.variables.Variable#getID()
     */
    @Override
    public UUID getID() {
        return this.id;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.variables.DiscreteVariable#getRange()
     */
    @Override
    public int getRange() {
        return this.attainableValues.size();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.anon.cocoa.variables.DiscreteVariable#iterator()
     */
    @Override
    public Iterator<Double> iterator() {
        return Collections.unmodifiableList(this.attainableValues).iterator();
    }

}
