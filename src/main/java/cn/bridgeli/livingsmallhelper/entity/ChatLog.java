package cn.bridgeli.livingsmallhelper.entity;

import java.util.Date;

public class ChatLog {
    private int id;
    private String fromUserName;
    private Date createTime;
    private String question;
    private String answer;
    private int chatCategory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getChatCategory() {
        return chatCategory;
    }

    public void setChatCategory(int chatCategory) {
        this.chatCategory = chatCategory;
    }

}
