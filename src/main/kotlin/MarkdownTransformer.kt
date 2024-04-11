class MarkdownTransformer {
    companion object {
        private val LINK_REGEX = "\\[(.*?)]\\((.*?)\\)".toRegex()
    }

    fun transform(markdown: String): MarkdownWithAnchors {
        val links = findLinksAt(markdown)
        val transformedText = replaceLinksByAnchors(markdown, links)
        return MarkdownWithAnchors(transformedText, associateAnchorsToLinks(links))
    }

    private fun replaceLinksByAnchors(markdownText: String, links: List<MarkdownLink>): String {
        return links.fold(markdownText) { acc, link ->
            acc.replace("[${link.name}](${link.url})", "${link.name} [${links.indexOf(link) + 1}]")
        }
    }

    private fun findLinksAt(markdownText: String): List<MarkdownLink> {
        return LINK_REGEX.findAll(markdownText)
            .map { matchResult -> MarkdownLink(matchResult.groupValues[1], matchResult.groupValues[2]) }
            .distinct()
            .toList()
    }

    private fun associateAnchorsToLinks(links: List<MarkdownLink>) =
        links.foldIndexed(mutableMapOf<Int, String>()) { index, acc, link ->
            acc[index + 1] = link.url
            acc
        }
}

data class MarkdownWithAnchors(val text: String, val anchors: Map<Int, String>)
data class MarkdownLink(val name: String, val url: String)