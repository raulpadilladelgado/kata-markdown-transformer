class MarkdownTransformer {
    fun transform(markdown: String): MarkdownWithAnchors {
        val links = findLinksAt(markdown)
        val transformedText = replaceLinksByAnchors(markdown, links)
        return MarkdownWithAnchors(transformedText, associateAnchorsToLinks(links))
    }

    private fun replaceLinksByAnchors(markdownText: String, links: List<MarkdownLink>): String {
        var transformedText = markdownText
        links.forEachIndexed { index, link ->
            transformedText = transformedText.replace("[${link.name}](${link.url})", "${link.name} [${index + 1}]")
        }
        return transformedText
    }

    private fun findLinksAt(markdownText: String): List<MarkdownLink> {
        val linkRegex = "\\[(.*?)]\\((.*?)\\)".toRegex()
        return linkRegex.findAll(markdownText).mapIndexed { _, matchResult ->
            val linkName = matchResult.groupValues[1]
            val linkUrl = matchResult.groupValues[2]
            MarkdownLink(linkName, linkUrl)
        }.distinct().toList()
    }

    private fun associateAnchorsToLinks(links: List<MarkdownLink>) =
        links.foldIndexed(mutableMapOf<Int, String>()) { index, acc, link ->
            acc[index + 1] = link.url
            acc
        }
}

data class MarkdownWithAnchors(val text: String, val anchors: Map<Int, String>)
data class MarkdownLink(val name: String, val url: String)