class MarkdownTransformer {
    fun transform(markdown: String): MarkdownTransformed {
        if (!markdown.contains("[")) {
            return MarkdownTransformed(markdown, mapOf())
        }
        val linkName = markdown.substringAfter("[").substringBefore("]")
        val link = markdown.substringAfter("(").substringBefore(")")
        val linkNumber = 1
        val markdownTransformed = markdown.replace("[$linkName]($link)", "$linkName [$linkNumber]")
        return MarkdownTransformed(
            markdownTransformed,
            mapOf(linkNumber to link)
        )
    }
}

data class MarkdownTransformed(val text: String, val links: Map<Int, String>)
