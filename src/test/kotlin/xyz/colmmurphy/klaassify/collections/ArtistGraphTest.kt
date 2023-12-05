package xyz.colmmurphy.klaassify.collections

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class ArtistGraphTest {
    private fun getDummyArtist(name: String = "John Doe", genres: Set<String> = setOf()): Artist {
        return Artist(
            name,
            genres,
            100L,
            "https://example.com",
            1
        )
    }
    @Test
    fun testAddVertex() {
        // should add artist to graph only if it is not already in the graph
        val graph = ArtistGraph {_, _ -> 0}
        val artist = getDummyArtist()
        graph.addVertex(artist)
        assertTrue(graph.vertices.contains(artist))
        assertEquals(1, graph.numVertices)
        graph.addVertex(artist)
        assertEquals(1, graph.numVertices, "Added duplicate vertex to vertex already in graph")
    }

    @Test
    fun testAddEdge() {
        val graph = ArtistGraph { _, _ -> 0}
        val john = getDummyArtist("John Doe")
        val jane = getDummyArtist("Jane Doe")
        graph.addVertex(john)
        graph.addVertex(jane)

        val e = graph.addEdge(john, jane, 1)
        assertTrue(graph.edges.contains(e))
        assertEquals(1, graph.numEdges)

        graph.addEdge(john, john, 1)
        assertEquals(1, graph.numEdges, "Added reflective edge")

        graph.addEdge(john, jane, 1)
        assertEquals(1, graph.numEdges, "Added duplicate duplicate edge")
    }

    @Test
    fun testRemoveVertex() {
        val graph = ArtistGraph { _, _ -> 0 }

        val artist1 = getDummyArtist("Artist1")
        val artist2 = getDummyArtist("Artist2")

        graph.addVertex(artist1)
        graph.addVertex(artist2)
        graph.addEdge(artist1, artist2, 1)

        graph.removeVertex(artist1)

        assertFalse(graph.vertices.contains(artist1))
        assertFalse(graph.edges.any { it.v1 == artist1 || it.v2 == artist1 })
        assertEquals(1, graph.numVertices)
        assertEquals(0, graph.numEdges)
    }

    @Test
    fun testAddVertexAndCreateEdges() {
        // only one vertex relation predicate is used in the application, thus we only need to test that one
        val graph = ArtistGraph(commonGenre)
        val rockstar = getDummyArtist("rockstar", setOf("rock"))
        val rapper = getDummyArtist("rapper", setOf("rap"))
        graph.addVertexAndCreateEdges(rockstar)
        graph.addVertexAndCreateEdges(rapper)
        assertEquals(0, graph.numEdges)
        val ratm = getDummyArtist("Rage Against The Machine", setOf("rap", "rock", "rap-rock"))
        graph.addVertexAndCreateEdges(ratm)
        assertEquals(2, graph.numEdges)
    }
}