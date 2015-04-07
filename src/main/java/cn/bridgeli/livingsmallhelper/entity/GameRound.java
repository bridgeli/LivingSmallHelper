package cn.bridgeli.livingsmallhelper.entity;

import java.util.Date;

public class GameRound {
    private int id;
    private int gameId;
    private String openId;
    private String quessNo;
    private Date quessTime;
    private String quessResult;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getQuessNo() {
        return quessNo;
    }

    public void setQuessNo(String quessNo) {
        this.quessNo = quessNo;
    }

    public Date getQuessTime() {
        return quessTime;
    }

    public void setQuessTime(Date quessTime) {
        this.quessTime = quessTime;
    }

    public String getQuessResult() {
        return quessResult;
    }

    public void setQuessResult(String quessResult) {
        this.quessResult = quessResult;
    }

}
