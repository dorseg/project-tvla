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
/* ----------------
 * SimpleGraph.java
 * ----------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: SimpleGraph.java 1923 2007-11-13 14:17:48Z tla $
 *
 * Changes
 * -------
 * 05-Aug-2003 : Initial revision (BN);
 *
 */
package org._3pq.jgrapht.graph;

import org._3pq.jgrapht.EdgeFactory;
import org._3pq.jgrapht.UndirectedGraph;
import org._3pq.jgrapht.edge.EdgeFactories;

/**
 * A simple graph. A simple graph is an undirected graph for which at most one
 * edge connects any two vertices, and loops are not permitted.  If you're
 * unsure about simple graphs, see: <a
 * href="http://mathworld.wolfram.com/SimpleGraph.html">
 * http://mathworld.wolfram.com/SimpleGraph.html</a>.
 */
public class SimpleGraph extends AbstractBaseGraph implements UndirectedGraph {
    /**
     * @see AbstractBaseGraph
     */
    public SimpleGraph( EdgeFactory ef ) {
        super( ef, false, false );
    }


    /**
     * @see AbstractBaseGraph
     */
    public SimpleGraph(  ) {
        this( new EdgeFactories.UndirectedEdgeFactory(  ) );
    }
}
