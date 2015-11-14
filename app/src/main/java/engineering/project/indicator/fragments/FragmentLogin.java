package engineering.project.indicator.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import cn.pedant.SweetAlert.SweetAlertDialog;
import engineering.project.indicator.R;
import engineering.project.indicator.preferences.Preferences;
import engineering.project.indicator.webService.WebService;

/**
 * Created by EduardoMichel on 07/11/2015.
 */
public class FragmentLogin extends Fragment {

    View view;
    Context context;
    EditText user, password;
    Button start, help;
    Preferences p;
    Resources rs;

    public FragmentLogin(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_login, container, false);
        context = container.getContext();

        builder();

        useButton();

        return view;
    }

    private void builder(){
        user = (EditText) view.findViewById(R.id.edtLoginUser);
        password = (EditText) view.findViewById(R.id.edtLoginPassword);
        start = (Button) view.findViewById(R.id.btnLoginStart);
        help = (Button) view.findViewById(R.id.btnLoginHelp);
        p = new Preferences(context);
        rs = getResources();

    }

    private void useButton(){
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEnter()){
                    //enviar los datos
                    new WebService(context, "oauth/token").
                            getToken(user.getText().toString(), password.getText().toString());
                }
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText(rs.getString(R.string.errorMessage))
                        .setContentText(rs.getString(R.string.construccion))
                        .setConfirmText(rs.getString(R.string.errorConfirm))
                        .setCustomImage(R.mipmap.construccion)
                        .show();

            }
        });
    }

    private boolean validateEnter(){
        if (!user.getText().toString().equalsIgnoreCase("") &&
                ! password.getText().toString().equalsIgnoreCase(""))
            return true;
        else{
            new SweetAlertDialog(context)
                    .setTitleText(rs.getString(R.string.errorMessage))
                    .setContentText(rs.getString(R.string.faltaD))

                    .show();
            return false;
        }

    }

    private void showLog(String log){
        Log.v("FragmentLogin",log);
    }


}
