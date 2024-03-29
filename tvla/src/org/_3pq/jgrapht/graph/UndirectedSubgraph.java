/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (barak_naveh@users.sourceforge.net)
 *
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* -----------------------
 * UndirectedSubgraph.java
 * -----------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: UndirectedSubgraph.java 1923 2007-11-13 14:17:48Z tla $
 *
 * Changes
 * -------
 * 05-Aug-2003 : Initial revision (BN);
 *
 */
package org._3pq.jgrapht.graph;

import java.util.Set;

import org._3pq.jgrapht.UndirectedGraph;

/**
 * An undirected graph that is a subgraph on other graph.
 *
 * @see org._3pq.jgrapht.graph.Subgraph
 */
public class UndirectedSubgraph extends Subgraph implements UndirectedGraph {
    /**
     * @see Subgraph#Subgraph(Graph, Set, Set)
     */
    public UndirectedSubgraph( UndirectedGraph base, Set vertexSubset,
        Set edgeSubset ) {
        super( base, vertexSubset, edgeSubset );
    }
}
