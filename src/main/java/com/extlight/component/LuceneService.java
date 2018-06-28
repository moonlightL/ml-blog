package com.extlight.component;

import com.chenlb.mmseg4j.analysis.ComplexAnalyzer;
import com.extlight.common.utils.DateUtil;
import com.extlight.common.vo.PageVo;
import com.extlight.model.Post;
import com.extlight.web.exception.GlobalException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class LuceneService {

    @Value("${lucene.index.path}")
    private String indexPath;

    private Directory directory;

    private DirectoryReader reader;

    @PostConstruct
    public void init() {
        try {
            directory = FSDirectory.open(Paths.get(indexPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加索引
     *
     * @param post
     * @throws Exception
     */
    public void add(Post post) throws GlobalException {
        IndexWriter writer = null;
        try {
            Analyzer analyzer = new ComplexAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(directory, iwc);
            Document doc = new Document();
            doc.add(new TextField("postId", post.getId().toString(), Field.Store.YES));
            doc.add(new TextField("title", post.getTitle(), Field.Store.YES));
            doc.add(new TextField("postUrl", post.getPostUrl(), Field.Store.YES));
            doc.add(new TextField("status", post.getStatus().toString(), Field.Store.YES));
            doc.add(new TextField("categoryName", post.getCategoryName(), Field.Store.YES));
            doc.add(new TextField("publishDate", DateUtil.formateToStr(post.getPublishDate(), "yyyy-MM-dd"), Field.Store.YES));
            writer.addDocument(doc);
        } catch (Exception e) {
            throw new GlobalException(500, e.toString());
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 修改索引
     *
     * @param post
     * @throws Exception
     */
    public void update(Post post) throws GlobalException {
        IndexWriter writer = null;
        try {
            Analyzer analyzer = new ComplexAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(directory, iwc);
            Document doc = new Document();
            doc.add(new TextField("postId", post.getId().toString(), Field.Store.YES));
            doc.add(new TextField("title", post.getTitle(), Field.Store.YES));
            doc.add(new TextField("postUrl", post.getPostUrl(), Field.Store.YES));
            doc.add(new TextField("status", post.getStatus().toString(), Field.Store.YES));
            doc.add(new TextField("categoryName", post.getCategoryName(), Field.Store.YES));
            doc.add(new TextField("publishDate", DateUtil.formateToStr(post.getPublishDate(), "yyyy-MM-dd"), Field.Store.YES));
            writer.updateDocument(new Term("postId", post.getId().toString()), doc);
        } catch (Exception e) {
            throw new GlobalException(500, e.toString());
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除索引
     *
     * @param postId
     * @throws Exception
     */
    public void delete(Integer postId) throws GlobalException {
        IndexWriter writer = null;
        try {
            Analyzer analyzer = new ComplexAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(directory, iwc);
            writer.deleteDocuments(new Term("postId", postId.toString()));
        } catch (Exception e) {
            throw new GlobalException(500, e.toString());
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询数据
     *
     * @param keyword
     * @return
     */
    public List<Post> query(String keyword) throws GlobalException {

        try {
            IndexSearcher searcher = this.getIndexSearcher();
            Analyzer analyzer = new ComplexAnalyzer();

            String[] fields = {"title"};// 使用多域查询，便于以后扩展
            MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(fields, analyzer);
            Query query = multiFieldQueryParser.parse(keyword);

            TopDocs topDocs = searcher.search(query, 100);

            // 1.格式化对象，设置前缀和后缀
            Formatter formatter = new SimpleHTMLFormatter("<font color='red'>","</font>");
            // 2.关键词对象
            Scorer scorer = new QueryScorer(query);
            // 3. 高亮对象
            Highlighter highlighter = new Highlighter(formatter, scorer);

            List<Post> list = new ArrayList<>();
            Post post;
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;

            for (ScoreDoc scoreDoc : scoreDocs) {
                Document document = searcher.doc(scoreDoc.doc);
                if (Integer.parseInt(document.get("status")) == 1) {
                    post = new Post();
                    String titleHighLight  = highlighter.getBestFragment(analyzer,"title",document.get("title"));
                    post.setId(Integer.parseInt(document.get("postId")))
                            .setTitle(titleHighLight)
                            .setPostUrl(document.get("postUrl"))
                            .setCategoryName(document.get("categoryName"))
                            .setPublishDate(DateUtil.parseToDate(document.get("publishDate"), "yyyy-MM-dd"));
                    list.add(post);
                }
            }
            return list;
        } catch (Exception e) {
            throw new GlobalException(500, e.toString());
        }
    }

    public PageVo queryByPage(String keyword, int pageNum, int pageSize) throws GlobalException {

        try {
            IndexSearcher searcher = this.getIndexSearcher();
            Analyzer analyzer = new ComplexAnalyzer();

            String[] fields = {"title"};// 使用多域查询，便于以后扩展
            MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(fields, analyzer);
            Query query = multiFieldQueryParser.parse(keyword);

            TopDocs topDocs = searcher.search(query, 100);

            // 1.格式化对象，设置前缀和后缀
            Formatter formatter = new SimpleHTMLFormatter("<font color='red'>","</font>");
            // 2.关键词对象
            Scorer scorer = new QueryScorer(query);
            // 3. 高亮对象
            Highlighter highlighter = new Highlighter(formatter, scorer);

            List<Post> list = new ArrayList<>();
            Post post;
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;

            if (pageNum < 1) {
                pageNum = 1;
            }

            if (scoreDocs.length == 0) {
                return new PageVo(pageNum,pageSize,scoreDocs.length,null);
            }

            int start = (pageNum - 1) * pageSize;
            int end = (int) Math.min(start + pageSize,topDocs.totalHits);

            for(int i=start; i<end; i++) {
                Document document = searcher.doc(scoreDocs[i].doc);
                if (Integer.parseInt(document.get("status")) == 1) {
                    post = new Post();
                    String titleHighLight  = highlighter.getBestFragment(analyzer,"title",document.get("title"));
                    post.setId(Integer.parseInt(document.get("postId")))
                            .setTitle(titleHighLight)
                            .setPostUrl(document.get("postUrl"))
                            .setCategoryName(document.get("categoryName"))
                            .setPublishDate(DateUtil.parseToDate(document.get("publishDate"), "yyyy-MM-dd"));
                    list.add(post);
                }
            }

            return new PageVo(pageNum,pageSize,scoreDocs.length,list);
        } catch (Exception e) {
            throw new GlobalException(500, e.toString());
        }
    }

    private IndexSearcher getIndexSearcher() throws IOException {
        if (reader == null) {
            reader = DirectoryReader.open(directory);
        } else {
            DirectoryReader changeReader = DirectoryReader.openIfChanged(reader);
            if (changeReader != null) {
                reader.close();
                reader = changeReader;
            }
        }
        return new IndexSearcher(reader);
    }
}
