package com.example.examenpm01p3scsh.configuracion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class utils {

    public class Utils {
        public static Bitmap base64ToBitmap(String base64String) {
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
    }
}
