package cc.iandroid.yundoured.utils;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import cc.iandroid.yundoured.common.EventMsg;
import cc.iandroid.yundoured.common.UdpMsg;

import static org.greenrobot.eventbus.EventBus.TAG;

/**
 * Created by gcy on 2017/2/15.
 */

public class UDPUtils {
    public static void sendUdp(final String ip, final String msg){
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
                        while (true){
                            try {
                                byte[] dataReceive = new byte[10240];
                                DatagramPacket packetReceive = new DatagramPacket(dataReceive,dataReceive.length);
                                socket.receive(packetReceive);
                                String receiveMsg = new String(dataReceive,0,packetReceive.getLength(),"utf-8");
                                Log.d(TAG, "run: "+receiveMsg);
                                if (receiveMsg != null){
                                    EventBus.getDefault().post(new UdpMsg(receiveMsg));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                break;
                            }
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

    public static void sendCmd(final String ip, final String msg){
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
                    try {
                        byte[] dataReceive = new byte[2048];
                        DatagramPacket packetReceive = new DatagramPacket(dataReceive,dataReceive.length);
                        socket.receive(packetReceive);
                        String receiveMsg = new String(dataReceive,0,packetReceive.getLength(),"utf-8");
                        Log.d(TAG, "run: "+receiveMsg);
                        if (receiveMsg != null){
                            EventBus.getDefault().post(new EventMsg(receiveMsg));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
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
