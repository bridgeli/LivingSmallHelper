package cn.bridgeli.livingsmallhelper.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import cn.bridgeli.livingsmallhelper.entity.ChatLog;
import cn.bridgeli.livingsmallhelper.entity.Knowledge;
import cn.bridgeli.livingsmallhelper.mapper.ChatLogMapper;
import cn.bridgeli.livingsmallhelper.mapper.KnowledgeSubMapper;
import cn.bridgeli.livingsmallhelper.service.ChatService;
import cn.bridgeli.livingsmallhelper.service.JokeService;
import cn.bridgeli.livingsmallhelper.util.WeiXinUtil;

@Service
public class ChatServiceImpl implements ChatService {
    private static final Logger LOG = LoggerFactory.getLogger(ChatServiceImpl.class);

    @Resource
    private JokeService jokeService;
    @Resource
    private ChatLogMapper chatLogMapper;
    @Resource
    private KnowledgeSubMapper knowledgeSubMapper;

    @Override
    public String chat(String question, String fromUserName) {
        String answer = null;
        int chatCategory = 0;
        Knowledge knowledge = searchIndex(question);
        if (null != knowledge) {
            int category = knowledge.getCategory();
            if (2 == category) {
                answer = jokeService.getJoke().getContent();
                chatCategory = 2;
            } else if (3 == category) {
                category = chatLogMapper.getLastCategory(fromUserName);
                if (2 == category) {
                    answer = jokeService.getJoke().getContent();
                    chatCategory = 2;
                } else {
                    answer = knowledge.getAnswer();
                    chatCategory = knowledge.getCategory();
                }
            } else {
                answer = knowledge.getAnswer();
                if (null == answer || "".equals(answer)) {
                    answer = knowledgeSubMapper.getKnowledgeSub(knowledge.getId()).getAnswer();
                }
            }
        } else {
            answer = WeiXinUtil.getDefaultAnswer();
            chatCategory = 0;
        }
        ChatLog chatLog = new ChatLog();
        chatLog.setFromUserName(fromUserName);
        chatLog.setCreateTime(new Date());
        chatLog.setQuestion(question);
        chatLog.setAnswer(answer);
        chatLog.setChatCategory(chatCategory);
        chatLogMapper.addChatLog(chatLog);
        return answer;
    }

    @SuppressWarnings("deprecation")
    private Knowledge searchIndex(String content) {
        Knowledge knowledge = null;
        Directory directory = null;
        IndexReader reader = null;
        try {
            directory = FSDirectory.open(new File(getIndexDir()));
            reader = IndexReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser queryParser = new QueryParser(Version.LUCENE_46, "question", new IKAnalyzer(true));
            Query query = queryParser.parse(QueryParser.escape(content));
            TopDocs topDocs = searcher.search(query, 1);
            if (topDocs.totalHits > 0) {
                knowledge = new Knowledge();
                ScoreDoc[] scoreDocs = topDocs.scoreDocs;
                for (ScoreDoc scoreDoc : scoreDocs) {
                    Document doc = searcher.doc(scoreDoc.doc);
                    knowledge.setId(doc.getField("id").numericValue().intValue());
                    knowledge.setQuestion(doc.get("questory"));
                    knowledge.setAnswer(doc.get("answer"));
                    knowledge.setCategory(doc.getField("category").numericValue().intValue());
                }

            }
        } catch (IOException e) {
            LOG.error("IOException", e);
        } catch (ParseException e) {
            LOG.error("ParseException", e);
        } finally {
            try {
                reader.close();
                directory.close();
            } catch (IOException e) {
                LOG.error("IOException", e);
            }
        }
        return knowledge;
    }

    @Override
    public String getIndexDir() {
        String classpath = ChatServiceImpl.class.getResource("/").getPath();
        classpath = classpath.replaceAll("%20", " ");
        LOG.warn("==================" + classpath);
        return classpath + "index/";
    }

}