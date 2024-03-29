package filip.ondrusek.uv.passwordmanager;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public  class PwnedPasswordHandling {
    private static String hashString;
    private final HTTPConnector httpConnector = new HTTPConnector();
    private String pwnedPasswords;
    private FragmentManager fragmentManager;
    private Context context;

    public PwnedPasswordHandling(FragmentManager fragmentManager, Context context) {
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    public  void hashPassword(String input)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);
            hashString = no.toString(16);


            if (hashString.length() < 40) {
                hashString = "0" + hashString;
            }
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        getPwnedPassword();
    }

    private void getPwnedPassword()
    {
        httpConnector.setHash(hashString.substring(0,5));
            Thread thread = new Thread(() -> {
                try  {
                    this.pwnedPasswords =  httpConnector.doInBackground();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isPasswordPwned();
    }

    private void isPasswordPwned()
    {
        String a = "";
        String hashSuffix = hashString.substring(5,32).toUpperCase(Locale.ROOT);
        if(pwnedPasswords.contains(hashSuffix))
        {
            openEmptyNameDialog(context.getResources().getString(R.string.warning), context.getResources().getString(R.string.password_exposed), "exposed_password");
        } else {
            openEmptyNameDialog(context.getResources().getString(R.string.notice), context.getResources().getString(R.string.password_no_exposed), "password_ok");

        }
    }

    private void openEmptyNameDialog(String title, String text, String tag)
    {
        EmptyNameDialog emptyNameDialog = new EmptyNameDialog(title, text);
        emptyNameDialog.show(fragmentManager, tag);
    }

}
