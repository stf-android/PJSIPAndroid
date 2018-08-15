package com.cpsc.cpsc_pgsip.Utils;

import android.content.Context;

import com.cpsc.cpsc_pgsip.Config;
import com.cpsc.cpsc_pgsip.pjsip.MyAccount;
import com.cpsc.cpsc_pgsip.pjsip.MyCall;
import com.cpsc.cpsc_pgsip.pjsip.OnPJSipRegStateListener;
import com.orhanobut.logger.Logger;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.pjsip_transport_type_e;

/**
 * 描述:
 * <p>
 *
 * @author allens
 * @date 2018/1/25
 */

public class PJSipUtil {

    static {

        try {
            System.loadLibrary("openh264");
        }catch (Exception e){
            e.fillInStackTrace();
        }

        System.loadLibrary("pjsua2");
        Logger.e("=========Library 正在加载=========");
    }

    public static PJSipUtil newInst;
    public static MyCall currentCall = null;
    public static Endpoint ep;
    public static MyAccount myAccount;

    public static PJSipUtil newInstance() {
        if (newInst == null) {
            synchronized (PJSipUtil.class) {
                if (newInst == null) {
                    newInst = new PJSipUtil();
                }
            }
        }
        return newInst;
    }

    private PJSipUtil() {
        if (ep == null) {
            ep = new Endpoint();
        }
    }

    public void init() {
        try {
            //创建端点
            ep.libCreate();
            //初始化端点
            EpConfig epConfig = new EpConfig();
            ep.libInit(epConfig);
            //创建SIP传输。显示错误处理示例
            TransportConfig sipTpConfig = new TransportConfig();
            sipTpConfig.setPort(5060);
            ep.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_UDP, sipTpConfig);
            //启动库
            ep.libStart();
        } catch (Exception e) {
            Logger.e("初始化失败" + e.getMessage());
            LogHelper.writeLog(Config.initEpConfig, "端点5060:" + e.getMessage());
        }
    }


    /***
     * 注册
     * @param context
     * @param account
     * @param pwd
     * @param ip
     */
    public void register(Context context, final String account, final String pwd, final String ip, final OnPJSipRegStateListener listener) {

        try {

            String ipAddress = IPAddress.getIPAddress(context);
            AccountConfig acfg = new AccountConfig();
            acfg.getNatConfig().setIceEnabled(true);
            acfg.setIdUri("sip:" + account + "@" + ipAddress); // 本机的账号与ip
            acfg.getRegConfig().setRegistrarUri("sip:" + ip); // 服务器的

//            acfg.setIdUri("sip:" + account + "@" + ip);
//            acfg.getRegConfig().setRegistrarUri("sip:" + ip);// 获取本机的ip地址
            AuthCredInfo cred = new AuthCredInfo("digest", "*", account, 0, pwd);
            acfg.getSipConfig().getAuthCreds().add(cred);
            //创建帐户
            myAccount = new MyAccount(context, listener);
            myAccount.create(acfg);
        } catch (Exception e) {
            e.printStackTrace();

            Logger.e("注册失败 " + e.getMessage());
            LogHelper.writeLog(Config.initEpConfig, "sip注册：" + e.getMessage());
        }
    }


}
