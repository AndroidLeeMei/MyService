package com.example.auser.myservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
/*
    //第一次啟動onCreate-->onStartCommand-->onDestroy
//    己經啟動過了,而且還在執行中時,重複按下啟動服務鈕,-->onStartCommand
//onStartCommand會被呼叫多次,適用放長時間執行

    @Overrideon StartCommand//自動產生的第二個參數有 @IntDef(value = {Service.START_FLAG_REDELIVERY, Service.START_FLAG_RETRY}, flag = true)
    //可以拿掉,不需要用到
 */
public class MyService extends Service {
    public static boolean isStop=true;  //從未被啟動過時,允許被啟動
    //在背景裏做長時間的服務,分配Thread,否則UI上的工作會受響
    private int count;
    private Thread t;
    public MyService() {
    }

    @Override//使用第一種started服務時,不會用到onBind,用第二種才會呼叫到
    public IBinder onBind(Intent intent) {

        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        isStop=false;//代表服務被啟動,
        t=new Thread(){
            @Override
                public void run(){
                    while ( !isStop) {
                        try {
                            Thread.sleep(1000);  //並不準確,不要拿來做計時器
                            System.out.println("count" + count);
                            count++;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            }
        };
        t.start();//也可以在onStartCommand
        super.onCreate();
    }

    @Override//自動產生的第二個參數有 @IntDef(value = {Service.START_FLAG_REDELIVERY, Service.START_FLAG_RETRY}, flag = true)
    public int onStartCommand(Intent intent, int flags, int startId) {
//        t.start();//當希望被重複呼叫時,就放在onStartCommand裏啟動,但執行緒尚未結束前不可以產生另一個執行緒,要把run移下來
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isStop=true;//讓Thread結束,若沒有這一段,則無法關掉
        t=null;//釋放讓Thread結束
        super.onDestroy();
    }


}
