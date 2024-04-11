import kotlin.test.Test
import kotlin.test.assertEquals

class MarkdownTransformerShould {

    @Test
    fun `not transform text without links`() {
        val markdown = "Hello World, this is a test"

        val result = MarkdownTransformer().transform(markdown)

        assertEquals("Hello World, this is a test", result.text)
        assertEquals(mapOf(), result.anchors)
    }

    @Test
    fun `transform text with links to anchors`() {
        val markdown = "Hello [World](https://www.helloworld.es), this is a [test](https://www.test.es)"

        val result = MarkdownTransformer().transform(markdown)

        assertEquals("Hello World [1], this is a test [2]", result.text)
        assertEquals(mapOf(1 to "https://www.helloworld.es", 2 to "https://www.test.es"), result.anchors)
    }

    @Test
    fun `transform text with repeated links to anchors`() {
        val markdown =
            "Hello [World](https://www.helloworld.es), this is a test, bye [World](https://www.helloworld.es), this is a [test](https://www.test.es)"

        val result = MarkdownTransformer().transform(markdown)

        assertEquals("Hello World [1], this is a test, bye World [1], this is a test [2]", result.text)
        assertEquals(mapOf(1 to "https://www.helloworld.es", 2 to "https://www.test.es"), result.anchors)
    }

    @Test
    fun `not transform text with something similar to a link with only square brackets`() {
        val markdown = "Hello [World], this is a test"

        val result = MarkdownTransformer().transform(markdown)

        assertEquals("Hello [World], this is a test", result.text)
        assertEquals(mapOf(), result.anchors)
    }

    @Test
    fun `not transform text with something similar to a link with only parenthesis`() {
        val markdown = "Hello World(https://www.helloworld.es, this is a test"

        val result = MarkdownTransformer().transform(markdown)

        assertEquals("Hello World(https://www.helloworld.es, this is a test", result.text)
        assertEquals(mapOf(), result.anchors)
    }
}