/*
 * Created on Oct 20, 2003
 *
 * This file is part of Bayesian Network for Java (BNJ).
 *
 * BNJ is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * BNJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BNJ in LICENSE.txt file; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package edu.ksu.cis.bnj.gui.event;

import java.util.EventObject;
import java.util.Set;

/**
 * @author Roby Joehanes
 */
public class NodeDeletedEvent extends EventObject {

    protected Set deletedNodes;

    /**
	 * @param source
	 */
	public NodeDeletedEvent(Object source, Set s) {
		super(source); deletedNodes = s;
	}

    public Set getDeletedNodes() {
        return deletedNodes;
    }
}
