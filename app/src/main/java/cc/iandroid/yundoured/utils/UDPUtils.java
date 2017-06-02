package cc.iandroid.yundoured.utils;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import cc.iandroid.yundoured.common.EventMsg;

/**
 * Created by gcy on 2017/2/15.
 */

public class UDPUtils {
    public static void sendUdp(final Context context,final String ip, final String msg){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DatagramSocket socket = null;
                    try {
                        InetAddress address = InetAddress.getByName(ip);
                        int port = 9128;
                        byte[] dataSend = msg.getBytes();
                        DatagramPacket packetSend = new DatagramPacket(dataSend,dataSend.length,address,port);
                        socket = new DatagramSocket();
                        socket.setSoTimeout(1000);
                        socket.send(packetSend);

                        byte[] dataReceive = new byte[2048];
                        DatagramPacket packetReceive = new DatagramPacket(dataReceive,dataReceive.length);
                        socket.receive(packetReceive);
                        String receiveMsg = new String(dataReceive,0,packetReceive.getLength(),"utf-8");
                        if (receiveMsg != null){
                            EventBus.getDefault().post(new EventMsg(receiveMsg));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (socket != null) {
                            socket.close();
                        }
                    }
                }
            }).start();
    }
}
