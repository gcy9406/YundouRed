package cc.iandroid.yundoured.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;

/**
 * Created by gcy on 2017/5/29.
 */
@Entity
public class DeviceInfo implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    @Property(nameInDb = "sn")
    private String sn;
    @Property(nameInDb = "user")
    private String user;
    @Property(nameInDb = "psw")
    private String psw;
    @Property(nameInDb = "ver")
    private String ver;
    @Property(nameInDb = "ip")
    private String ip;
    @Property(nameInDb = "name")
    private String name;
    @Property(nameInDb = "state")
    private boolean state;

    public DeviceInfo(String sn, String user, String psw, String ver,
                      String ip, String name, boolean state) {
        this.sn = sn;
        this.user = user;
        this.psw = psw;
        this.ver = ver;
        this.ip = ip;
        this.name = name;
        this.state = state;
    }

    @Generated(hash = 422846830)
    public DeviceInfo(Long id, String sn, String user, String psw, String ver,
            String ip, String name, boolean state) {
        this.id = id;
        this.sn = sn;
        this.user = user;
        this.psw = psw;
        this.ver = ver;
        this.ip = ip;
        this.name = name;
        this.state = state;
    }
    @Generated(hash = 2125166935)
    public DeviceInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSn() {
        return this.sn;
    }
    public void setSn(String sn) {
        this.sn = sn;
    }
    public String getUser() {
        return this.user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getPsw() {
        return this.psw;
    }
    public void setPsw(String psw) {
        this.psw = psw;
    }
    public String getVer() {
        return this.ver;
    }
    public void setVer(String ver) {
        this.ver = ver;
    }
    public String getIp() {
        return this.ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean getState() {
        return this.state;
    }
    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "id=" + id +
                ", sn='" + sn + '\'' +
                ", user='" + user + '\'' +
                ", psw='" + psw + '\'' +
                ", ver='" + ver + '\'' +
                ", ip='" + ip + '\'' +
                ", name='" + name + '\'' +
                ", state=" + state +
                '}';
    }
}
