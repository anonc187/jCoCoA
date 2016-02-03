/**
 * File OrderedAgent.java
 *
 * This file is part of the jCoCoA project 2014.
 * 
 * Copyright 2014 Anomymous
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.anon.cocoa.agents;

import java.util.SortedSet;

import org.anon.cocoa.exceptions.DuplicateChildException;

/**
 * OrderedAgent
 * 
 * @author Anomymous
 * @version 0.1
 * @since 4 feb. 2014
 * 
 */
public interface OrderedAgent extends Agent {

	public void addChild(Agent agent) throws DuplicateChildException;

	public SortedSet<Agent> getChildren();

	public Agent getParent();

	public int getSequenceID();

	public void setParent(Agent parent);

}
