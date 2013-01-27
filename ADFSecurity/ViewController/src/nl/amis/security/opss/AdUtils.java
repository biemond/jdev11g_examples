package nl.amis.security.opss;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;


import javax.mail.internet.MimeUtility;

import oracle.security.idm.IMException;
import oracle.security.idm.OperationFailureException;

public class AdUtils {
    public AdUtils() {
        super();
    }

    public static void main(String[] args) {
        AdUtils adUtils = new AdUtils();


        try {
            System.out.println( new String( adUtils.encode(  
                                               adUtils.getUTF16LEPassword("Weblogic2".toCharArray())))
                                   );
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println( new String( adUtils.encode(  
                                               adUtils.getUTF16LEPassword("Welcome02".toCharArray())))
                                   );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static byte[] encode(byte[] b) throws Exception {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStream b64os = MimeUtility.encode(baos, "base64");
            b64os.write(b);
            b64os.close();
            return baos.toByteArray();
    }


    static byte[] getUTF16LEPassword(char[] password) throws IMException {
      byte[] bytePassword = null;
      int curIdx = 0;

      if (password == null) {
        return null;
      }
      if (password.length == 0) {
        return new byte[0];
      }

      try
      {
        bytePassword = ("\"" + new String(password) + "\"").getBytes("UTF-16LE");
      }
      catch (UnsupportedEncodingException unse)
      {
        throw new OperationFailureException(unse);
      }
      return bytePassword;
    }

}
