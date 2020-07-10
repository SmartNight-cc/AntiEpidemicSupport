package com.google.common;

import java.io.Serializable;

/**
 * 客户端发来的请求
 * 包括：操作的表的代码编号，操作动作的代码编号，对应的Bean
 */
public class Request implements Serializable {

    private int tableCode;
    private int operationCode;
    private TableBean tableBean;

    public Request(){

    }

    public Request(int tableCode, int operationCode, TableBean tableBean){
        this.tableCode = tableCode;
        this.operationCode = operationCode;
        this.tableBean = tableBean;
    }

    public int getTableCode() {
        return tableCode;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public TableBean getTableBean() {
        return tableBean;
    }

    public void setTableCode(int tableCode) {
        this.tableCode = tableCode;
    }

    public void setOperationCode(int operationCode) {
        this.operationCode = operationCode;
    }

    public void setTableBean(TableBean tableBean) {
        this.tableBean = tableBean;
    }
}
