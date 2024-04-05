import kotlin.test.Test
import kotlin.test.assertEquals

class MarkdownTransformerShould {
    /*
    *
    * Hello World, this is a test
    *   Hello World, this is a test
    *
    * Hello [World](https://www.helloworld.es), this is a test
    *   Hello World [1], this is a test
    *   [1]: https://www.helloworld.es
    *
    * Hello [World](https://www.helloworld.es), this is a [test](https://www.test.es)
    *   Hello World [1], this is a test [2]
    *   [1]: https://www.helloworld.es
    *   [2]: https://www.test.es
    *
    * Hello [World](https://www.helloworld.es), this is a test, bye [World](https://www.helloworld.es)
    *   Hello World [1], this is a test, bye World [1]
    *   [1]: https://www.helloworld.es
    *
    * Hello [World](https://www.helloworld.es), this is a test, bye [World](https://www.helloworld.es), this is a [test](https://www.test.es)
    *   Hello World [1], this is a test, bye World [1], this is a test [2]
    *   [1]: https://www.helloworld.es
    *   [2]: https://www.test.es
    *
    * Hello [World], this is a test
    *   Hello World [1], this is a test
    *   [1]: No link found
    *
    * Hello World(https://www.helloworld.es), this is a test
    *   Hello World(https://www.helloworld.es), this is a test
    *
    * */

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
}