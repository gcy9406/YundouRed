package cc.iandroid.yundoured.utils;

import android.content.Context;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class UDPClient implements Runnable {
    final static int udpPort = 9128;
    final static String hostIp = "192.168.1.166";
    private static DatagramSocket socket = null;
    private static DatagramPacket packetSend, packetRcv;
    private boolean udpLife = true; //udp生命线程
    private byte[] msgRcv = new byte[1024]; //接收消息
    private Context context;

    public UDPClient(Context context) {
        super();
        this.context = context;
    }

    //返回udp生命线程因子是否存活
    public boolean isUdpLife() {
        if (udpLife) {
            return true;
        }

        return false;
    }

    //更改UDP生命线程因子
    public void setUdpLife(boolean b) {
        udpLife = b;
    }

    //发送消息
    public String send(String msgSend, String ip) {
        InetAddress hostAddress = null;

        try {
            hostAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

/*        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            Log.i("udpClient","建立发送数据报失败");
            e.printStackTrace();
        }*/

        packetSend = new DatagramPacket(msgSend.getBytes(), msgSend.getBytes().length, hostAddress, udpPort);

        try {
            socket.send(packetSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //   socket.close();
        return msgSend;
    }

    @Override
    public void run() {

        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(30000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        packetRcv = new DatagramPacket(msgRcv, msgRcv.length);
        while (udpLife) {
            if (socket.isClosed()){
                break;
            }
            try {
                socket.receive(packetRcv);
                String rcvMsg = new String(packetRcv.getData(), packetRcv.getOffset(), packetRcv.getLength(), "gbk");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }
}