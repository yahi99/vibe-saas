package com.yongche.yopsaas.db.util;

public class RideOrderDispatchHandleOption {
    private boolean delete = false;      // 删除操作
    private boolean selected = false;    // 已选操作

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
