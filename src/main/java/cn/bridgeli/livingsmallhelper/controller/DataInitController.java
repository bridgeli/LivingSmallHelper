package cn.bridgeli.livingsmallhelper.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.wltea.analyzer.lucene.IKAnalyzer;

import cn.bridgeli.livingsmallhelper.entity.Knowledge;
import cn.bridgeli.livingsmallhelper.mapper.KnowledgeMapper;
import cn.bridgeli.livingsmallhelper.service.ChatService;

@Controller
@RequestMapping("/dataInitController")
public class DataInitController {
    private static final Logger LOG = LoggerFactory.getLogger(DataInitController.class);

    @Resource
    private KnowledgeMapper knowledgeMapper;
    @Resource
    private ChatService chatService;

    @RequestMapping(value = "/createIndex", method = RequestMethod.GET)
    public void createIndex() {

        List<Knowledge> knowledges = knowledgeMapper.query();

        Directory directory = null;
        IndexWriter indexWriter = null;
        File indexFile = new File(chatService.getIndexDir());
        if (!indexFile.exists()) {

            try {
                directory = FSDirectory.open(indexFile);
                IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_46, new IKAnalyzer(true));
                indexWriter = new IndexWriter(directory, indexWriterConfig);
                for (Knowledge k : knowledges) {
                    Document document = new Document();
                    document.add(new TextField("question", k.getQuestion(), Field.Store.YES));
                    document.add(new IntField("id", k.getId(), Field.Store.YES));
                    document.add(new TextField("answer", k.getAnswer() == null ? "" : k.getAnswer(), Field.Store.YES));
                    document.add(new IntField("category", k.getCategory(), Field.Store.YES));

                    indexWriter.addDocument(document);
                }
                indexWriter.commit();
            } catch (IOException e) {
                LOG.error("IOException", e);
            } finally {
                try {
                    if (null != indexWriter) {
                        indexWriter.close();
                    }
                    if (null != directory) {
                        directory.close();
                    }
                } catch (IOException e) {
                    LOG.error("IOException", e);
                }
            }

        }
    }
}