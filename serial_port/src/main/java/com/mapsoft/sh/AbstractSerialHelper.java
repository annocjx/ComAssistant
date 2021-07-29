package com.mapsoft.sh;

/**
 * author: annocx
 * ___           ___            ___              _____         _____          _ _
 * /  /\         /__/\          /__/\            /  /::\       /  /::|        /__/\
 * /  /::\        \  \:\         \  \:\         /  /:/\:\     /  /:/          |: :|
 * /  /:/\:\        \  \:\         \  \:\      /  /:/  \:\   /  /:/           |: :|
 * /  /:/~/::\    ____\__\:\     ___ \__\:\   /__/:/    \_\  |__/:/        ___|: :|___
 * /__/:/ /:/\:\ /__/::::::::\  /__/::::::::\ \  \:\    /:/  |\:\     __  /__/::::::::\
 * \  \:\/:/__\/ \  \:\~~\~~\/  \  \:\~~\~~\/  \  \::  /:/   \  \::  /:/  \~~\:\~~\~~\/
 * \  \::/        \  \:\  ~~~    \  \:\  ~~~    \  \:\/:/     \  \:\/:/       |: :|
 * \  \:\          \  \:\         \  \:\         \  \/;/       \  \/;/        |: :|
 * \__\:\           \__\/          \__\/          \__\/         \ : /         \_:_\
 * <p>
 * created on: 2019/2/23 002313:23
 * packagename: serialporthelper
 * projectname: LCDApplication
 * description:
 */





import android.util.Log;

import com.mapsoft.bean.ComBean;
import com.mapsoft.utils.FuncUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;


/**
 * @author Administrator
 */
public abstract class AbstractSerialHelper {
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private SendThread mSendThread;
    private String sPort;
    private int iBaudRate ;
    private boolean _isOpen;
    private byte[] _bLoopData;
    private int iDelay;

    public AbstractSerialHelper(String sPort, String sBaudRate) {
        this(sPort, Integer.parseInt(sBaudRate));
    }
    
    public AbstractSerialHelper(String pSPort, int pIBaudRate) {
        sPort = pSPort;
        iBaudRate = pIBaudRate;
        _isOpen = false;
        _bLoopData = new byte[]{48};
        iDelay = 500;
    }
    
  

    public InputStream open() throws SecurityException, IOException, InvalidParameterException {
        return open(false);
    }

    public InputStream open(boolean handle) throws SecurityException, IOException, InvalidParameterException {
        mSerialPort = new SerialPort(new File(sPort), iBaudRate, 0);
        mOutputStream = mSerialPort.getOutputStream();
        mInputStream = mSerialPort.getInputStream();
        if (!handle) {
            mReadThread = new ReadThread();
            mReadThread.start();
            mSendThread = new SendThread();
            mSendThread.setSuspendFlag();
            mSendThread.start();
            _isOpen = true;
        }
        return mInputStream;
    }

    public void close() {
        if (mReadThread != null) {
            mReadThread.interrupt();
        }
        if (mSendThread != null) {
            mSendThread.interrupt();
        }

        if (mSerialPort != null) {
            try {
                mSerialPort.close();
                mSerialPort = null;
            } catch (Exception pE) {
                Log.e("关闭串口出错了",""+pE.toString());
            }
        }

        _isOpen = false;
    }

    public void send(byte[] bOutArray) {
        try {
            mOutputStream.write(bOutArray);
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public void sendHex(String sHex) {
        byte[] bOutArray = FuncUtil.HexToByteArr(sHex);
        send(bOutArray);
    }

    public void sendTxt(String sTxt) {
        byte[] bOutArray = sTxt.getBytes();
        send(bOutArray);
    }

    public int getBaudRate() {
        return iBaudRate;
    }

    public boolean setBaudRate(int iBaud) {
        if (_isOpen) {
            return false;
        } else {
            iBaudRate = iBaud;
            return true;
        }
    }

    public boolean setBaudRate(String sBaud) {
        int iBaud = Integer.parseInt(sBaud);
        return setBaudRate(iBaud);
    }

    public String getPort() {
        return sPort;
    }

    public boolean setPort(String port) {
        if (_isOpen) {
            return false;
        } else {
            sPort = port;
            return true;
        }
    }

    public boolean isOpen() {
        return _isOpen;
    }

    public byte[] getbLoopData() {
        return _bLoopData;
    }

    public void setbLoopData(byte[] bLoopData) {
        _bLoopData = bLoopData;
    }

    public void setTxtLoopData(String sTxt) {
        _bLoopData = sTxt.getBytes();
    }

    public void setHexLoopData(String sHex) {
        _bLoopData = FuncUtil.HexToByteArr(sHex);
    }

    public int getiDelay() {
        return iDelay;
    }

    public void setiDelay(int iDelay) {
        iDelay = iDelay;
    }

    public void startSend() {
        if (mSendThread != null) {
            mSendThread.setResume();
        }

    }

    public void stopSend() {
        if (mSendThread != null) {
            mSendThread.setSuspendFlag();
        }

    }

    @Override
    public String toString() {
        return "串口"+sPort+"-波特率"+iBaudRate;
    }

    protected abstract void onDataReceived(ComBean var1);

    private class SendThread extends Thread {
        public boolean suspendFlag;

        private SendThread() {
            suspendFlag = true;
        }

        @Override
        public void run() {
            super.run();

            while (!isInterrupted()) {
                synchronized (this) {
                    while (suspendFlag) {
                        try {
                            wait();
                        } catch (InterruptedException var5) {
                            var5.printStackTrace();
                        }
                    }
                }

                send(getbLoopData());

                try {
                    Thread.sleep(AbstractSerialHelper.this.iDelay);
                } catch (InterruptedException var4) {
                    var4.printStackTrace();
                }
            }

        }

        public void setSuspendFlag() {
            suspendFlag = true;
        }

        public synchronized void setResume() {
            suspendFlag = false;
            notify();
        }
    }

    private class ReadThread extends Thread {
        private ReadThread() {
        }

        @Override
        public void run() {
            super.run();

            while (!isInterrupted()) {
                try {
                    if (AbstractSerialHelper.this.mInputStream == null) {
                        return;
                    }

                    byte[] buffer = new byte[512];
                    int size = AbstractSerialHelper.this.mInputStream.read(buffer);
                    if (size > 0) {
                        ComBean ComRecData = new ComBean(AbstractSerialHelper.this.sPort, buffer, size);
                        AbstractSerialHelper.this.onDataReceived(ComRecData);
                    }
                } catch (Throwable var4) {
                    Log.e("error", var4.getMessage());
                    return;
                }
            }

        }
    }
}

