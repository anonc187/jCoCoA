/**
 * File DuplicateChildException.java
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
package org.anon.cocoa.exceptions;

import org.anon.cocoa.agents.Agent;

/**
 * DuplicateChildException
 *
 * @author Anomymous
 * @version 0.1
 * @since 4 feb. 2014
 *
 */
public class DuplicateChildException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 8719376691990837701L;

    /**
     *
     */
    public DuplicateChildException(Agent<?, ?> agent) {
        super("Duplicate child for agent with name " + agent.getName());
    }

}
