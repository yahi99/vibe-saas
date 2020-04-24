package com.yongche.yopsaas.db.util;

public class RideOrderHandleOption {
    private boolean cancel = false;      // 取消操作
    private boolean delete = false;      // 删除操作
    private boolean pay = false;         // 支付操作
    private boolean comment = false;    // 评论操作

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isPay() {
        return pay;
    }

    public void setPay(boolean pay) {
        this.pay = pay;
    }

    public boolean isComment() {
        return comment;
    }

    public void setComment(boolean comment) {
        this.comment = comment;
    }
}
