package xyz.colmmurphy.klaassify.collections

interface IEdge {
    val v1: IVertex
    val v2: IVertex
    val element: Any

    override fun toString(): String

    /**
     * Compares two Edge objects.
     * @param other The edge with which to compare the callee Edge
     * @returns true if they are equal (both Edges have the same vertices)
     */
    fun equalTo(other: IEdge): Boolean

    /**
     * @return The two vertices associated with this Edge
     */
    fun vertices(): Pair<IVertex, IVertex>

    /**
     * @param v any vertex
     * @return the opposite vertex to v, if v is in this Edge, null otherwise
     */
    fun opposite(v: IVertex): IVertex?
}