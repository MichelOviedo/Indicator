package engineering.project.indicator.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import engineering.project.indicator.R;
import engineering.project.indicator.fragments.FragmentLogin;
import engineering.project.indicator.preferences.Preferences;


public class LoginActivity extends Activity {

    FragmentLogin login;
    Preferences p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        builder();

        if (p.getAccesToken().equalsIgnoreCase("null acces"))
            loaderFragment(getLogin());
    }

    private void builder(){
        p = new Preferences(this);
    }
    public void loaderFragment(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.rltContentLogin, fragment);
        transaction.commit();
    }

    private FragmentLogin getLogin() {
    if (login == null) login = new FragmentLogin();
        return login;
    }
}
