package xyz.colmmurphy.klaassify.collections

abstract class Graph<V : Vertex, T> {
    abstract override fun toString(): String
    abstract val vertices: List<V>
    abstract val numVertices: Int
    abstract val edges: List<Edge<T>>
    abstract val numEdges: Int
    abstract fun getEdge(v1: V, v2: V): Edge<T>?
    abstract fun getEdges(v: V): List<Edge<T>>
    abstract fun degree(v: V): Int
    abstract fun addVertex(vertex: V)
    abstract fun addEdge(v1: V, v2: V, element: T): Edge<T>
    abstract fun removeVertex(v: Vertex)
}