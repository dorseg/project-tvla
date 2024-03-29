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
/* ----------------------
 * VertexSetListener.java
 * ----------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: VertexSetListener.java 1923 2007-11-13 14:17:48Z tla $
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 * 10-Aug-2003 : Adaptation to new event model (BN);
 *
 */
package org._3pq.jgrapht.event;

import java.util.EventListener;

/**
 * A listener that is notified when the graph's vertex set changes. It should
 * be used when <i>only</i> notifications on edge-set changes are of interest.
 * If all graph  notifications are of interest better use
 * <code>GraphListener</code>.
 *
 * @author Barak Naveh
 *
 * @see org._3pq.jgrapht.event.GraphListener
 * @since Jul 18, 2003
 */
public interface VertexSetListener extends EventListener {
    /**
     * Notifies that a vertex has been added to the graph.
     *
     * @param e the vertex event.
     */
    public void vertexAdded( GraphVertexChangeEvent e );


    /**
     * Notifies that a vertex has been removed from the graph.
     *
     * @param e the vertex event.
     */
    public void vertexRemoved( GraphVertexChangeEvent e );
}
