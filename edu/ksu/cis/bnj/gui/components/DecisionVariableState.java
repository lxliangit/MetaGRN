/*
 * Created on Oct 18, 2003
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
package edu.ksu.cis.bnj.gui.components;

import java.awt.event.MouseEvent;

import edu.ksu.cis.bnj.bbn.*;
import edu.ksu.cis.bnj.gui.*;

import salvo.jesus.graph.visual.*;

/**
 * @author Roby Joehanes
 */
public class DecisionVariableState extends VariableState {

	/**
	 * @param panel
	 */
	public DecisionVariableState(NodeManager owner, GraphPanel panel) {
		super(owner, panel);
	}

	/**
	 * @see edu.ksu.cis.bnj.gui.components.VariableState#createNode(edu.ksu.cis.bnj.bbn.BBNGraph)
	 */
	protected BBNNode createNode(BBNGraph graph) {
        BBNNode  newNode = new BBNNode(graph, "Decision"+(counter++));
        newNode.setType(BBNNode.DECISION);
		return newNode;
	}
}
