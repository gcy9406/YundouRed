package cc.iandroid.yundoured.callback;

/**
 * Created by gcy on 2017/6/3.
 */

public interface OnRelayControlClickLister {
    void doOnClick(int pos,int size);
    void doOffClick(int pos,int size);
    void doPluseClick(int pos,int size);
    void doTurnClick(int pos,int size);
    void doLockClick(int pos,int size);
}
