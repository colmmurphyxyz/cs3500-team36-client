package xyz.colmmurphy.klaassify.collections

/**
 * CLass to represent a Graph of artists in the user's listening catalogue
 * with edges constructed programmatically via the supplied relation predicate
 * @param vertexRelationPredicate function that defines if two artists should be related
 *      can be expanded to provide support for different formats of data visualisation
 */
class ArtistGraph(
    val vertexRelationPredicate: (Artist, Artist) -> Int
): Graph<Artist, Int>() {
    private val adjacencyList: HashMap<Artist, MutableList<Edge<Int>>> = hashMapOf()
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
    override val edges: List<Edge<Int>>
        get() {
            val e = mutableListOf<Edge<Int>>()
            for (v in adjacencyList.keys) {
                for (edge in getEdges(v)) {
                    if (edge !in e) {
                        e.add(edge)
                    }
                }
//                e.addAll(getEdges(v))
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
    override fun getEdge(v1: Artist, v2: Artist): Edge<Int>? {
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
    override fun getEdges(v: Artist): List<Edge<Int>> {
        return adjacencyList[v]!!
    }

    /**
     * Get the degree of a vertex x
     * @param v Artist
     * @return Number of edges incident on v
     */
    override fun degree(v: Artist): Int {
        return adjacencyList[v]!!.size
    }

    /**
     * Add a vertex to the graph if it is not already in the graph
     * @param vertex artist from which to create the vertex
     */
    override fun addVertex(vertex: Artist) {
        adjacencyList[vertex] = mutableListOf<Edge<Int>>()
    }

    /**
     * Adds a new vertex and create any edges based on the vertexRelationPredicate
     * supplied in the ArtistGraph constructor
     * @param artist new vertex to add
     * @return List of all new edges created
     */
    fun addVertexAndCreateEdges(artist: Artist): List<Edge<Int>> {
        addVertex(artist)
        val newEdges = mutableListOf<Edge<Int>>()
        for (v in vertices) {
            val commonGenres = vertexRelationPredicate(artist, v)
            if (commonGenres > 0) {
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
    override fun addEdge(v1: Artist, v2: Artist, element: Int): Edge<Int> {
        if (v1 == v2) return Edge(v1, v2, element)
        val newEdge = Edge<Int>(v1, v2, element)
        if (newEdge in adjacencyList[v1]!!) {
            return newEdge
        }
        adjacencyList[v1]!!.add(newEdge)
        adjacencyList[v2]!!.add(newEdge)
        return newEdge
    }

    /**
     * Remove a vertex and all edges incident on it
     * @param v vertex to be rmeoved
     */
    override fun removeVertex(v: Artist) {
        for (edge in adjacencyList[v]!!) {
            adjacencyList[edge.opposite(v)]!!.remove(edge)
        }
        println("before remove: ${adjacencyList.keys.size}")
        adjacencyList.remove(v)
        println("after remove: ${adjacencyList.keys.size}")
    }

    /**
     * determine if two given vertices are adjacent
     * @return true if there exists an edge between v1 and v2
     */
    fun areAdjacent(v1: Artist, v2: Artist): Boolean {
        return getEdge(v1, v2) != null
    }
}