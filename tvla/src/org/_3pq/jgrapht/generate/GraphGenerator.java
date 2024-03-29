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
/* -------------------
 * GraphGenerator.java
 * -------------------
 * (C) Copyright 2003, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id: GraphGenerator.java 1923 2007-11-13 14:17:48Z tla $
 *
 * Changes
 * -------
 * 16-Sep-2003 : Initial revision (JVS);
 *
 */
package org._3pq.jgrapht.generate;

import java.util.Map;

import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.VertexFactory;

/**
 * GraphGenerator defines an interface for generating new graph structures.
 *
 * @author John V. Sichi
 *
 * @since Sep 16, 2003
 */
public interface GraphGenerator {
    /**
     * Generate a graph structure. The topology of the generated graph is
     * dependent on the implementation.  For graphs in which not all vertices
     * share the same automorphism equivalence class, the generator may
     * produce a labeling indicating the roles played by generated elements.
     * This is the purpose of the resultMap parameter.  For example, a
     * generator for a wheel graph would designate a hub vertex.  Role names
     * used as keys in resultMap should be declared as public static final
     * Strings by implementation classes.
     *
     * @param target receives the generated edges and vertices; if this is
     *        non-empty on entry, the result will be a disconnected graph
     *        since generated elements will not be connected to existing
     *        elements
     * @param vertexFactory called to produce new vertices
     * @param resultMap if non-null, receives implementation-specific mappings
     *        from String roles to graph elements (or collections of graph
     *        elements)
     */
    public void generateGraph( Graph target, VertexFactory vertexFactory,
        Map resultMap );
}
