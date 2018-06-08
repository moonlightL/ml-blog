package com.extlight.common.utils;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.FencedCodeBlock;
import org.commonmark.node.Image;
import org.commonmark.node.ListItem;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.*;

import java.util.*;

public abstract class MarkdownUtil {

    public static List<Extension> extensions = Arrays.asList(TablesExtension.create());
    private static final Parser parser = Parser.builder().extensions(extensions).build();
    private static final HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions)
            .nodeRendererFactory(context -> new NodeRenderer() {

                @Override
                public Set<Class<? extends Node>> getNodeTypes() {
                    return Collections.singleton(FencedCodeBlock.class);
                }

                @Override
                public void render(Node node) {

                    HtmlWriter html = context.getWriter();
                    FencedCodeBlock codeBlock = (FencedCodeBlock) node;
                    html.line();
                    html.tag("pre");
                    html.tag("code");
                    html.tag("ol");
                    String data = codeBlock.getLiteral();
                    String[] split = data.split("\n");
                    for (String s : split) {
                        html.tag("li");
                        html.text(s);
                        html.tag("/li");
                    }
                    html.tag("/ol");
                    html.tag("/code");
                    html.tag("/pre");
                    html.line();

                }
            }).nodeRendererFactory(context -> new NodeRenderer() {
                @Override
                public Set<Class<? extends Node>> getNodeTypes() {
                    return Collections.singleton(Image.class);
                }

                @Override
                public void render(Node node) {

                    HtmlWriter html = context.getWriter();

                    Image image = (Image) node;
                    Map<String,String> attrs = new HashMap<>();
                    Map<String,String> attrs2 = new HashMap<>();
                    attrs.put("href",image.getDestination());
                    attrs2.put("src",image.getDestination());
                    html.line();
                    html.tag("a",attrs);
                    html.tag("image",attrs2);
                    html.tag("/a");
                    html.line();
                }
            })
            .attributeProviderFactory(context -> (node, s, map) -> {

                if ("p".equals(s) && node.getParent() instanceof ListItem) {
                    map.put("class","show-line");
                }

            })
            .build();

    /**
     * markdown 转 html
     * @param markdown
     * @return
     */
    public static String md2html(String markdown) {
        Node document = parser.parse(markdown);
        String result = renderer.render(document);
        return result;
    }
}
