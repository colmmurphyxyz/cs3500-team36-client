package xyz.colmmurphy.klaassify.collections

interface IGraph {
    override fun toString(): String

    /**
     * List of all vertices in the graph
     */
    val vertices: List<IVertex>
    val numVertices: Int

    /**
     * List of all edges in the graph
     */
    val edges: List<IEdge>
    val numEdges: Int

    /**
     * @return the edge incident on v1 and v2, or null if no such edge exists
     */
    fun getEdge(v1: IVertex, v2: IVertex): Edge?

    /**
     * @return the degree of a given vertex v
     */
    fun degree(v: IVertex): Int

    /**
     * @return all edges incident on a given vertex v
     */
    fun getEdges(v: IVertex): List<Edge>

    /**
     * Add a new vertex v to the graph
     * @return newly created vertex
     */
    fun addVertex(v: IVertex): IVertex

    /**
     * create a vertex from the given artist and add it to the graph
     * @param artist artist from which to create a vertex
     * @return newly created vertex
     */
    fun addVertex(artist: Artist): IVertex

    /**
     * Add a new edge to the graph incident on the two given vertices
     * @return newly created edge
     */
    fun addEdge(v1: IVertex, v2: IVertex): IEdge

    /**
     * Remove the vertex v and all its adjacent edges
     */
    fun removeVertex(v: IVertex)

    /**
     * Removes the edge e
     */
    fun removeEdge(e: IEdge)
}