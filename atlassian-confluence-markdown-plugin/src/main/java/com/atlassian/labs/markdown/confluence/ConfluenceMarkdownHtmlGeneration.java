package com.atlassian.labs.markdown.confluence;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.content.render.xhtml.XhtmlException;
import com.atlassian.confluence.content.render.xhtml.migration.UrlResourceIdentifier;
import com.atlassian.confluence.content.render.xhtml.model.resource.DefaultEmbeddedImage;
import com.atlassian.confluence.content.render.xhtml.model.resource.identifiers.AttachmentResourceIdentifier;
import com.atlassian.confluence.content.render.xhtml.model.resource.identifiers.NamedResourceIdentifier;
import com.atlassian.confluence.content.render.xhtml.model.resource.identifiers.ResourceIdentifier;
import com.atlassian.confluence.xhtml.api.XhtmlContent;
import com.atlassian.labs.markdown.MarkdownHtmlGeneration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfluenceMarkdownHtmlGeneration implements MarkdownHtmlGeneration
{
    public static final Pattern HTTP_URL = Pattern.compile("^http[s]?:\\/\\/");
    public static final Pattern P_TAGS = Pattern.compile("^<p>(.*)</p>$");

    private final XhtmlContent xhtmlContent;
    private final ConversionContext conversionContext;

    public ConfluenceMarkdownHtmlGeneration(final XhtmlContent xhtmlContent, final ConversionContext conversionContext)
    {
        this.xhtmlContent = xhtmlContent;
        this.conversionContext = conversionContext;
    }

    public String generateImageHTML(String sharedSecret, String url, String alt_text, String title)
    {
        NamedResourceIdentifier resourceIdentifier = new UrlResourceIdentifier(url);
        if (! HTTP_URL.matcher(url).find())
        {
            resourceIdentifier = new AttachmentResourceIdentifier(url);
        }
        DefaultEmbeddedImage embeddedImage = new DefaultEmbeddedImage(resourceIdentifier);
        try
        {
            return addSharedSecret(sharedSecret, "img", xhtmlContent.convertEmbeddedImageToView(embeddedImage, conversionContext));
        }
        catch (XhtmlException e)
        {
            return null;
        }
    }

    public String generateLinkHTML(String sharedSecret, String url, String title, String linkText)
    {
        return null;
    }


    private String addSharedSecret(String sharedSecret, String selector, String html)
    {
        // wiki rendering puts <p> tags that we dont want
        Matcher matcher = P_TAGS.matcher(html);
        if (matcher.matches())
        {
            html = matcher.group(1);
        }

        Document frag = Jsoup.parseBodyFragment(html);
        frag.select(selector).attr("data-shared-secret", sharedSecret);
        return frag.body().html();
    }
}
