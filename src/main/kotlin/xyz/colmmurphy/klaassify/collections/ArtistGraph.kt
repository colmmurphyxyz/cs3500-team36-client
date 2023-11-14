package xyz.colmmurphy.klaassify.collections

typealias GenreRelationEdge = Edge<Set<String>>

class ArtistGraph(
    val vertexRelationPredicate: (Artist, Artist) -> Set<String>
): Graph<Artist, Set<String>>() {
    private val adjacencyList: HashMap<Artist, MutableList<GenreRelationEdge>> = hashMapOf()
    /**
     * String representation of the graph.
     * Containing all vertices and edges, and the amount of each
     * @return string representation of the graph
     */
    override fun toString(): String {
        return """
            |V| = $numVertices
            |E| = $numEdges
            Vertices: ${vertices.joinToString(", ")}
            Edges: ${edges.joinToString(", ")}
        """.trimIndent()
    }

    /**
     * List of vertices in the graph
     */
    override val vertices: List<Artist>
        get() = adjacencyList.keys.toList()

    /**
     * Number of vertices in the graph
     */
    override val numVertices: Int
        get() = adjacencyList.keys.size

    /**
     * List of edges in the graph
     */
    override val edges: List<GenreRelationEdge>
        get() {
            val e = mutableSetOf<GenreRelationEdge>()
            for (v in adjacencyList.keys) {
                e.addAll(getEdges(v))
            }
            return e.toList()
        }

    /**
     * Number of edges in the graph
     */
    override val numEdges: Int
        get() = edges.size

    /**
     * Get the edge incident on both v1 and v2
     * @param v1 Artst
     * @param v2 Artist
     * @return edge incident on v1 and v2 if one exists, nul otherwise
     */
    override fun getEdge(v1: Artist, v2: Artist): GenreRelationEdge? {
        val e = adjacencyList.getOrElse(v1) { return null }
        for (edge in e) {
            if (v2 == edge.opposite(v1)) return edge
        }
        return null
    }

    /**
     * Get all edges incident on v
     * @param v Artist
     * @return List if all edges incident on v
     */
    override fun getEdges(v: Artist): List<GenreRelationEdge> {
        return adjacencyList[v]!!
    }

    /**
     * Get the degree of a vertex x
     * @param v Artist
     * @return Number of edges incident on v
     */
    override fun degree(v: Artist): Int {
        return getEdges(v).size
    }

    /**
     * Add a vertex to the graph if it is not already in the graph
     * @param vertex artist from which to create the vertex
     */
    override fun addVertex(vertex: Artist) {
        adjacencyList[vertex] = mutableListOf<GenreRelationEdge>()
    }

    /**
     * Adds a new vertex and create any edges based on the vertexRelationPredicate
     * supplied in the ArtistGraph constructor
     * @param artist new vertex to add
     * @return List of all new edges created
     */
    fun addVertexAndCreateEdges(artist: Artist): List<GenreRelationEdge> {
        addVertex(artist)
        val newEdges = mutableListOf<GenreRelationEdge>()
        for (v in vertices) {
            val commonGenres = vertexRelationPredicate(artist, v)
            if (commonGenres.isNotEmpty()) {
                newEdges.add(addEdge(artist, v, commonGenres))
            }
        }
        return newEdges
    }

    /**
     * Add a new edge to the graph, incident on v1 and v2
     * @param v1 vertex on which the new edge will be incident
     * @param v2 vertex on which the new edge will be incident
     * @return newly created edge
     */
    override fun addEdge(v1: Artist, v2: Artist, element: Set<String>): GenreRelationEdge {
        val newEdge = GenreRelationEdge(v1, v2, element)
        adjacencyList[v1]!!.add(newEdge)
        adjacencyList[v2]!!.add(newEdge)
        return newEdge
    }

    override fun removeVertex(v: Vertex) {
        for (edge in adjacencyList[v]!!) {
            adjacencyList[edge.opposite(v)]!!.remove(edge)
        }
        adjacencyList.remove(v)
    }

}