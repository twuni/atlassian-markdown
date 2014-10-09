package com.atlassian.labs.markdown.confluence;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.xhtml.api.XhtmlContent;
import com.atlassian.labs.markdown.MarkdownHtmlGeneration;
import com.atlassian.labs.markdown.PageDownMarkdown;

import java.util.Map;

/**
 * A macro for Confluence that provides Markdown support
 */
public class ConfluenceMarkdownXhtmlMacro implements Macro
{

    private final XhtmlContent xhtmlContent;

    public ConfluenceMarkdownXhtmlMacro(XhtmlContent xhtmlContent)
    {
        this.xhtmlContent = xhtmlContent;
    }

    @Override
    public String execute(Map<String, String> parameters, String body, ConversionContext context) throws MacroExecutionException
    {
        MarkdownHtmlGeneration confluenceMarkdownHtmlGeneration = new ConfluenceMarkdownHtmlGeneration(xhtmlContent, context);
        String markdown = new PageDownMarkdown(confluenceMarkdownHtmlGeneration).markdown(body);
        return markdown;
    }

    @Override
    public BodyType getBodyType()
    {
        return BodyType.PLAIN_TEXT;
    }

    @Override
    public OutputType getOutputType()
    {
        return OutputType.BLOCK;
    }

}
