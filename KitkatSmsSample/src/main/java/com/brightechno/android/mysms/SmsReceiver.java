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

import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * SMS Receiver
 */
public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        if (extras == null) {
            return;
        }

        Object[] smsExtras = (Object[]) extras.get(SmsConstant.PDUS);

        ContentResolver contentResolver = context.getContentResolver();
        Uri smsUri = Uri.parse(SmsConstant.SMS_URI);

        for (Object smsExtra : smsExtras) {
            byte[] smsBytes = (byte[]) smsExtra;

            // FYI: createFromPdu(byte[] pdu) will be deprecated in near future.
            // Please refer Javadoc of SmsMessage#createFromPdu(byte[] pdu)
            SmsMessage smsMessage = SmsMessage.createFromPdu(smsBytes);

            String body = smsMessage.getMessageBody();
            String address = smsMessage.getOriginatingAddress();

            ContentValues values = new ContentValues();
            values.put(SmsConstant.COLUMN_ADDRESS, address);
            values.put(SmsConstant.COLUMN_BODY, body);

            Uri uri = contentResolver.insert(smsUri, values);

            // TODO: implement notification
        }
    }
}
