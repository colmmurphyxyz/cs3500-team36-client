package xyz.colmmurphy.klaassify.collections

class Edge<T>(
    val v1: Vertex,
    val v2: Vertex,
    val element: T
) {
    override fun toString(): String = "$v1 <--$element--> $v2"

    override fun equals(other: Any?): Boolean {
        return if (other is Edge<*>) {
            (v1 == other.v1 && v2 == other.v2)
                    || (v1 == other.v2 && v2 == other.v1)
                    && element == other.element
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = v1.hashCode()
        result = 31 * result + v2.hashCode()
        result = 31 * result + element.hashCode()
        return result
    }

    private val vertices: Pair<Vertex, Vertex>
        get() = v1 to v2

    fun opposite(v: Vertex): Vertex? {
        if (v == v1) return v2
        if (v == v2) return v1
        return null
    }
}
