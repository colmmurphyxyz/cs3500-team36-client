package xyz.colmmurphy.klaassify.collections

/**
 * Graph representing artists as vertices and edges defined by a given relation
 */
class ArtistGraph(val relation: (Artist, Artist) -> Boolean) : IGraph {
    override fun toString(): String {
        TODO("Not yet implemented")
    }

    /**
     * List of all vertices in the graph
     */
    override val vertices: List<IVertex>
        get() = TODO("Not yet implemented")
    override val numVertices: Int
        get() = TODO("Not yet implemented")

    /**
     * List of all edges in the graph
     */
    override val edges: List<IEdge>
        get() = TODO("Not yet implemented")
    override val numEdges: Int
        get() = TODO("Not yet implemented")

    /**
     * @return the edge incident on v1 and v2, or null if no such edge exists
     */
    override fun getEdge(v1: IVertex, v2: IVertex): Edge? {
        TODO("Not yet implemented")
    }

    /**
     * @return the degree of a given vertex v
     */
    override fun degree(v: IVertex): Int {
        TODO("Not yet implemented")
    }

    /**
     * @return all edges incident on a given vertex v
     */
    override fun getEdges(v: IVertex): List<Edge> {
        TODO("Not yet implemented")
    }

    /**
     * Add a new vertex v to the graph
     * @return newly created vertex
     */
    override fun addVertex(v: IVertex): IVertex {
        TODO("Not yet implemented")
    }

    /**
     * create a vertex from the given artist and add it to the graph
     * @param artist artist from which to create a vertex
     * @return newly created vertex
     */
    override fun addVertex(artist: Artist): IVertex {
        TODO("Not yet implemented")
    }

    /**
     * Add a new edge to the graph incident on the two given vertices
     * @return newly created edge
     */
    override fun addEdge(v1: IVertex, v2: IVertex): IEdge {
        TODO("Not yet implemented")
    }

    /**
     * Remove the vertex v and all its adjacent edges
     */
    override fun removeVertex(v: IVertex) {
        TODO("Not yet implemented")
    }

    /**
     * Removes the edge e
     */
    override fun removeEdge(e: IEdge) {
        TODO("Not yet implemented")
    }

}
