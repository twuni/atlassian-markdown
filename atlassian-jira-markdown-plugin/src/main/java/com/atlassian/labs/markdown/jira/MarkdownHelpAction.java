package com.atlassian.labs.markdown.jira;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.labs.markdown.PageDownMarkdown;
import com.atlassian.labs.markdown.ResourceReader;

import java.io.StringWriter;

/**
 * An action to show help in
 */
public class MarkdownHelpAction extends JiraWebActionSupport
{
    @Override
    protected String doExecute() throws Exception
    {
        return SUCCESS;
    }

    public String getHelpHTML()
    {
        return new PageDownMarkdown().markdown(getHelpContents());
    }

    private String getHelpContents()
    {
        StringWriter sw = new StringWriter();
        new ResourceReader().readResource("templates/markdown-help.md", sw);
        return sw.toString();
    }
}
