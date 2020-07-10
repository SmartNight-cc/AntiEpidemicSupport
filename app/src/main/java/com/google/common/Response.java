package com.google.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务端发回的消息
 * 返回内容：
 * 1. 进行操作的表的代码编号
 * 2. 进行操作的代码编号
 * 3. 所有操作的结果（List）
 */
public class Response implements Serializable {

    private int tableCode; // 需要操作的表的代码编号
    private int operationCode;// 动作的代码编号
    private int responseCode;//操作结果
    private List<TableBean> list = new ArrayList<>();

    public Response(){

    }

    public Response(int tableCode, int operationCode, int responseCode, List list){
        this.tableCode = tableCode;
        this.operationCode = operationCode;
        this.responseCode = responseCode;
        this.list = list;
    }

    public int getTableCode() {
        return tableCode;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public int getResponseCode(){
        return responseCode;
    }

    public List<TableBean> getTableBeanList() {
        return list;
    }

    public void setTableCode(int tableCode) {
        this.tableCode = tableCode;
    }

    public void setOperationCode(int operationCode) {
        this.operationCode = operationCode;
    }

    public void setResponseCode(int responseCode){
        this.responseCode = responseCode;
    }

    public void setTableBeanList(List<TableBean> list) {
        this.list = list;
    }

}
