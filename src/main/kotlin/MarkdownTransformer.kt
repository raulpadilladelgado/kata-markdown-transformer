class MarkdownTransformer {
    fun transform(textWithLinks: String): String {
        val links = findLinksAt(textWithLinks)
        val textWithAnchors = replaceLinksByAnchors(textWithLinks, links)
        val anchorsToLinks = associateAnchorsToLinks(links)
        return appendAnchorsToText(textWithAnchors, anchorsToLinks)
    }

    private fun findLinksAt(markdownText: String): List<MarkdownLink> {
        return LINK_REGEX.findAll(markdownText)
            .map { matchResult -> MarkdownLink(matchResult.groupValues[1], matchResult.groupValues[2]) }
            .distinct()
            .toList()
    }

    private fun replaceLinksByAnchors(markdownText: String, links: List<MarkdownLink>): String {
        return links.fold(markdownText) { acc, link ->
            acc.replace(link.format(), link.formatAsAnchor(links.indexOf(link) + 1))
        }
    }

    private fun associateAnchorsToLinks(links: List<MarkdownLink>) =
        links.foldIndexed(mutableMapOf<Int, String>()) { index, acc, link ->
            acc[index + 1] = link.url
            acc
        }

    private fun appendAnchorsToText(text: String, anchors: Map<Int, String>): String {
        if (anchors.isEmpty()) {
            return text
        }
        val anchorsText = anchors.entries.joinToString(separator = "\n") { "[${it.key}]: ${it.value}" }
        return "$text\n$anchorsText"
    }
}

private val LINK_REGEX = "\\[(.*?)]\\((.*?)\\)".toRegex()

data class MarkdownLink(val name: String, val url: String) {
    fun format(): String {
        return "[${name}](${url})"
    }

    fun formatAsAnchor(anchorNumber: Int): String {
        return "$name [$anchorNumber]"
    }
}