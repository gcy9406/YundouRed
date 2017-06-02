package cc.iandroid.yundoured.bean;

/**
 * Created by gcy on 2017/6/2.
 */

public class PostInfo {
    private String outs;
    private String ins;

    public PostInfo() {
    }

    public PostInfo(String outs, String ins) {
        this.outs = outs;
        this.ins = ins;
    }

    public String getOuts() {
        return outs;
    }

    public void setOuts(String outs) {
        this.outs = outs;
    }

    public String getIns() {
        return ins;
    }

    public void setIns(String ins) {
        this.ins = ins;
    }

}
