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

/**
 * defines constants for SMS functions
 */
public class SmsConstant {
    public static final String SMS_URI = "content://sms/";
    public static final String SMS_SENT_URI = "content://sms/sent";

    public static final String PDUS = "pdus";

    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_BODY = "body";
}
