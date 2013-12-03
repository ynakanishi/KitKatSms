/**
 * Copyright(C) 2013 Brightechno Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.brightechno.android.mysms;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * The service respond to quick response introduced since Android 4.0
 */
public class HeadlessSmsSendService extends IntentService {

    public HeadlessSmsSendService() {
        super(HeadlessSmsSendService.class.getName());

        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (!TelephonyManager.ACTION_RESPOND_VIA_MESSAGE.equals(action)) {
            return;
        }

        Bundle extras = intent.getExtras();

        if (extras == null) {
            return;
        }

        String message = extras.getString(Intent.EXTRA_TEXT);
        Uri intentUri = intent.getData();
        String recipients = getRecipients(intentUri);

        // 宛先がなければ終了
        if (TextUtils.isEmpty(recipients)) {
            return;
        }

        // メッセージがなければ終了
        if (TextUtils.isEmpty(message)) {
            return;
        }

        String[] destinations = TextUtils.split(recipients, ";");

        sendAndStoreTextMessage(getContentResolver(), destinations, message);
    }

    /**
     * get quick response recipients from URI
     */
    private String getRecipients(Uri uri) {
        String base = uri.getSchemeSpecificPart();
        int pos = base.indexOf('?');
        return (pos == -1) ? base : base.substring(0, pos);
    }

    /**
     * Send text message to recipients and store the message to SMS Content Provider
     *
     * @param contentResolver ContentResolver
     * @param destinations recipients of message
     * @param message message
     */
    private void sendAndStoreTextMessage(ContentResolver contentResolver, String[] destinations, String message) {
        SmsManager smsManager = SmsManager.getDefault();

        Uri smsSentUri = Uri.parse(SmsConstant.SMS_SENT_URI);

        for (String destination : destinations) {
            smsManager.sendTextMessage(destination, null, message, null, null);

            ContentValues values = new ContentValues();
            values.put(SmsConstant.COLUMN_ADDRESS, destination);
            values.put(SmsConstant.COLUMN_BODY, message);

            Uri uri = contentResolver.insert(smsSentUri, values);
        }
    }
}
