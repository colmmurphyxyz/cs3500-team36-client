package xyz.colmmurphy.klaassify.collections

class Edge(
           override val v1: IVertex,
           override val v2: IVertex,
           override val element: String
) : IEdge {

    override fun equalTo(other: IEdge): Boolean {
        return (this.v1 == other.v1 && this.v2 == other.v2) || (this.v1 == other.v2 && this.v2 == other.v1)
    }

    override fun toString(): String {
        return "$v1<-->$v2: $element"
    }

    override fun vertices(): Pair<IVertex, IVertex> {
        // uses Kotlin's 'to' operator to create a Pair object of the two vertices
        return v1 to v2
    }

    override fun opposite(v: IVertex): IVertex? {
        if (v == v1) return v2
        if (v == v2) return v1
        return null
    }

}
