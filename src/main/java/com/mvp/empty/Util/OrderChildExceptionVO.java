package com.mvp.empty.Util;

import java.util.List;

/**
 * Created by Gao on 2017/4/18.
 */

public class OrderChildExceptionVO {

    /**
     * childNo : 2201704118765432189
     * compensationFee : 250
     * fromType : 1
     * handleResult : 赔偿货主150元
     * imageList : ["string"]
     * reason : 货少了
     * status : 1
     */

    private String childNo;
    private int compensationFee;
    private int fromType;
    private String handleResult;
    private String reason;
    private int status;
    private List<String> imageList;
    private String domain;

    public String getChildNo() {
        return childNo;
    }

    public void setChildNo(String childNo) {
        this.childNo = childNo;
    }

    public int getCompensationFee() {
        return compensationFee;
    }

    public void setCompensationFee(int compensationFee) {
        this.compensationFee = compensationFee;
    }

    public int getFromType() {
        return fromType;
    }

    public void setFromType(int fromType) {
        this.fromType = fromType;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
