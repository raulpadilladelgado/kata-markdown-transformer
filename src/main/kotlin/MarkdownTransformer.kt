class MarkdownTransformer {
    fun transform(markdown: String): MarkdownWithAnchors {
        return replaceLinksByAnchors(markdown, findLinksAt(markdown))
    }

    private fun replaceLinksByAnchors(
        markdownText: String,
        links: List<MarkdownLink>,
        linkIndex: Int = 0
    ): MarkdownWithAnchors {
        if (linkIndex == links.size) {
            return MarkdownWithAnchors(markdownText, associateAnchorsToLinks(links))
        }
        val (linkName, linkUrl) = links[linkIndex]
        val transformedText = markdownText.replace("[${linkName}](${linkUrl})", "$linkName [${linkIndex + 1}]")
        return replaceLinksByAnchors(transformedText, links, linkIndex + 1)
    }

    private fun findLinksAt(markdownText: String): List<MarkdownLink> {
        val linkRegex = "\\[(.*?)]\\((.*?)\\)".toRegex()
        return linkRegex.findAll(markdownText).mapIndexed { _, matchResult ->
            val linkName = matchResult.groupValues[1]
            val linkUrl = matchResult.groupValues[2]
            MarkdownLink(linkName, linkUrl)
        }.toList()
    }

    private fun associateAnchorsToLinks(links: List<MarkdownLink>) =
        links.foldIndexed(mutableMapOf<Int, String>()) { index, acc, link ->
            acc[index + 1] = link.url
            acc
        }
}

data class MarkdownWithAnchors(val text: String, val anchors: Map<Int, String>)

data class MarkdownLink(val name: String, val url: String)