package xyz.colmmurphy.klaassify.collections

import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class EdgeTest {

    @Test
    fun testEquals() {
        val john = Artist("John Doe", setOf(), 100L, "", 10)
        val jane = Artist("Jane Doe", setOf(), 100L, "", 10)
        val bob = Artist("Bob", setOf(), 100L, "", 10)
        val alice = Artist("Alice", setOf(), 100L, "", 10)
        val e = Edge<Int>(john, jane, 1)
        val e1 = Edge(bob, alice, 2)
        assertNotEquals(e, e1, "Input edges have no common fields")
        val e2 = Edge(john, jane, 1)
        assertEquals(e, e2, "Input edges are equal")
        val e3 = Edge(john, bob, 1)
        assertNotEquals(e, e3)
        val e4 = Edge(john, jane, 2)
        assertNotEquals(e, e4, "Input edges have different values for `element`")
        val e5 = Edge(alice, bob, 1)
        assertNotEquals(e, e5, "Input edges has same element, but different vertices")
    }

    @Test
    fun testOpposite() {
        val john = Artist("John Doe", setOf(), 100L, "", 10)
        val jane = Artist("Jane Doe", setOf(), 100L, "", 10)
        val bob = Artist("Bob", setOf(), 100L, "", 10)
        val e = Edge(john, jane, 1)
        assertEquals(jane, e.opposite(john))
        assertEquals(john, e.opposite(jane))
        assertNull(e.opposite(bob))
    }
}