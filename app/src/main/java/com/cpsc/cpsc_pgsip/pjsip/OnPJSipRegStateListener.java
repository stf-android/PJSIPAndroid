package com.cpsc.cpsc_pgsip.pjsip;

import org.pjsip.pjsua2.pjsip_status_code;

/**
 * 描述:
 * <p>
 * <p>
 * pjsip 注册状态
 *
 * @author allens
 * @date 2018/1/25
 */

public interface OnPJSipRegStateListener {

    void onSuccess();

    void onError();
}
