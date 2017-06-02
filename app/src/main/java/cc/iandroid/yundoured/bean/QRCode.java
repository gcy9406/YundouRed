package cc.iandroid.yundoured.bean;

/**
 * Created by gcy on 2017/5/29.
 */

public class QRCode {

    /**
     * sn : 0150982661293172
     * user : test
     * psw : test
     * ver : ZMRN1016-S3
     */

    private String sn;
    private String user;
    private String psw;
    private String ver;

    public QRCode() {
    }

    public QRCode(String sn, String user, String psw, String ver) {
        this.sn = sn;
        this.user = user;
        this.psw = psw;
        this.ver = ver;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }
}
