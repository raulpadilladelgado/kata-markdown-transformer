class MarkdownTransformer {
    fun transform(markdownText: String): MarkdownTransformed {
        if (notContainsLinks(markdownText)) {
            return MarkdownTransformed(markdownText, mapOf())
        }
        val links = findLinksAt(markdownText)
        val markdownTransformed = replaceLinksByAnchors(links, markdownText)
        val anchorsWithLinks = associateAnchorsToLinks(links)
        return MarkdownTransformed(markdownTransformed, anchorsWithLinks)
    }

    private fun notContainsLinks(markdown: String) = !(markdown.contains("[") && markdown.contains("("))

    private fun findLinksAt(markdownText: String): List<Pair<String, String>> {
        val linkRegex = "\\[(.*?)]\\((.*?)\\)".toRegex()
        return linkRegex.findAll(markdownText).mapIndexed { _, matchResult ->
            val linkName = matchResult.groupValues[1]
            val linkUrl = matchResult.groupValues[2]
            linkName to linkUrl
        }.toList()
    }

    private fun replaceLinksByAnchors(
        links: List<Pair<String, String>>,
        markdownText: String
    ) = links.foldIndexed(markdownText) { index, acc, (linkName, linkUrl) ->
        acc.replace("[${linkName}](${linkUrl})", "$linkName [${index + 1}]")
    }

    private fun associateAnchorsToLinks(links: List<Pair<String, String>>) =
        links.foldIndexed(mutableMapOf<Int, String>()) { index, acc, (_, linkUrl) ->
            acc[index + 1] = linkUrl
            acc
        }
}

data class MarkdownTransformed(val text: String, val anchorsWithLinks: Map<Int, String>)
