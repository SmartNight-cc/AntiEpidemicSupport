package com.google.common;

public interface ResponseCode {

    public final int INSERT_OK = 11;// 插入成功
    public final int INSERT_FAIL = 12;// 插入失败

    public final int DELETE_OK = 21;// 删除成功
    public final int DELETE_FAIL = 22;// 删除失败

    public final int UPDATE_OK = 31;// 更新成功
    public final int UPDATE_FAIL = 32;// 更新失败

    public final int SELECT_OK = 41;// 查找成功
    public final int SELECT_FAIL = 42;// 查找失败

    public final int SELECT_ALL_OK = 51;// 查找全部成功
    public final int SELECT_ALL_FAIL = 52;// 查找全部失败

}
