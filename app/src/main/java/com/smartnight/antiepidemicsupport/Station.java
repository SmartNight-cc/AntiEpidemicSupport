package com.smartnight.antiepidemicsupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * 中转站类，需要本地用户的物资数量和种类，物资需求的种类和数量，设置范围来判断什么属于普遍少量和普遍大量
 * 需要子中转站列表，需要的援助或到达列表
 * 来一个需求处理一个，不积攒
 */
public class Station {
    private int ID;
    private String passWord;
    private String address;
    private int NUMSUM;//物资种类总数
    private HashMap<String,Integer> BOX;//物资名称和数量
    private int GIVENUM,RECEIVENUM;//需要处理的事项数量
    private List<String> GIVEITEM = new ArrayList<>();
    private List<String> RECEIVEITEM = new ArrayList<>();
    private int SONNUM;//子中转站数量
    private List<Integer> SONLIST = new ArrayList<>();//子中转站的ID列表
    private int FATHER;//上级中转站ID
    private int CHOOSE1 = 1;//本地
    private int CHOOSE2 = 2;//上报

    public Station(int ID, String passWord, String address) {
        this.ID = ID;
        this.passWord = passWord;
        this.address = address;
    }

    public void watcher(String need,int sum){
        //接收到了一个下级或本地的请求，计算需求处理方式
        int choose = caculateChoose(need,sum);
        if(choose == CHOOSE1){
            //本地解决
        }else{
            //上报
        }
    }

    //计算Need中need的需求需要处理的方式
    public int caculateChoose(String need,int sum){
        if(!BOX.containsKey(need)){
            return CHOOSE2;
        }else if(BOX.get(need)<sum){
            return CHOOSE2;
        }else{
            return CHOOSE1;
        }
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public int getNUMSUM() {
        return NUMSUM;
    }

    public void setNUMSUM(int NUMSUM) {
        this.NUMSUM = NUMSUM;
    }

    public HashMap<String, Integer> getBOX() {
        return BOX;
    }

    public void setBOX(HashMap<String, Integer> BOX) {
        this.BOX = BOX;
    }

    public int getGIVENUM() {
        return GIVENUM;
    }

    public void setGIVENUM(int GIVENUM) {
        this.GIVENUM = GIVENUM;
    }

    public int getRECEIVENUM() {
        return RECEIVENUM;
    }

    public void setRECEIVENUM(int RECEIVENUM) {
        this.RECEIVENUM = RECEIVENUM;
    }

    public List<String> getGIVEITEM() {
        return GIVEITEM;
    }

    public void setGIVEITEM(List<String> GIVEITEM) {
        this.GIVEITEM = GIVEITEM;
    }

    public List<String> getRECEIVEITEM() {
        return RECEIVEITEM;
    }

    public void setRECEIVEITEM(List<String> RECEIVEITEM) {
        this.RECEIVEITEM = RECEIVEITEM;
    }

    public int getSONNUM() {
        return SONNUM;
    }

    public void setSONNUM(int SONNUM) {
        this.SONNUM = SONNUM;
    }

    public List<Integer> getSONLIST() {
        return SONLIST;
    }

    public void setSONLIST(List<Integer> SONLIST) {
        this.SONLIST = SONLIST;
    }
}