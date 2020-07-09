package com.smartnight.antiepidemicsupport;

import android.graphics.Bitmap;
/*
 *
 * */

public class Post {
    private String content;//内容
    private int ID;//发表的用户ID
    private int value;//权值
    private int vote_num;//点赞数
    private int concern_num;//关注数
    private boolean concerned,voted;//是否关注、点赞
    private Bitmap[]pics;//发表的图片

    public Post(String content,Bitmap[]pics, int ID) {
        this.content = content;
        this.pics = pics;
        this.ID = ID;
        this.vote_num = 0;
        this.concern_num = 0;
        this.value = R.string.INITVALUE;//初始权值
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getVote_num() {
        return vote_num;
    }

    public void setVote_num(int vote_num) {
        this.vote_num = vote_num;
    }

    public int getConcern_num() {
        return concern_num;
    }

    public void setConcern_num(int concern_num) {
        this.concern_num = concern_num;
    }

    public boolean getConcerned(){ return concerned; };

    public void setConcerned(boolean concerned){this.concerned=concerned; };

    public boolean getVoted(){ return voted; };

    public void setVoted(boolean voted){this.voted=voted; };

    public Bitmap[] getPics(){ return pics; }

    public void setPics(Bitmap[]pics){ this.pics = pics;  }

}
