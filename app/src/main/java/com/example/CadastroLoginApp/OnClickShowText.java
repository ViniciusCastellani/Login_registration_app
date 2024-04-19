package com.example.CadastroLoginApp;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class OnClickShowText implements View.OnTouchListener {
    private static final int DRAWABLE_RIGHT = 2;
    private static final int LOCK_ICON = R.drawable.lock_icon_light;
    private static final int EYE_SHOW_ICON = R.drawable.eye_show_icon_light;
    private static final int EYE_SEE_ICON = R.drawable.eye_see_icon_light;

    private EditText senha;
    private boolean senhaVisivel = false;

    public OnClickShowText(EditText senha) {
        this.senha = senha;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (iconeFoiPressionado(event)) {
                int selecao = senha.getSelectionEnd();
                alternarVisibilidadeSenha();
                atualizarIconeSenha();
                senha.setSelection(selecao);
                return true;
            }
        }
        return false;
    }

    private boolean iconeFoiPressionado(MotionEvent event) {
        return event.getRawX() >= senha.getRight() - senha.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
    }

    private void alternarVisibilidadeSenha() {
        senhaVisivel = !senhaVisivel;
        if (senhaVisivel) {
            mostrarSenha();
        } else {
            ocultarSenha();
        }
    }

    private void mostrarSenha() {
        senha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }

    private void ocultarSenha() {
        senha.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private void atualizarIconeSenha() {
        int icone;
        if(senhaVisivel){
            icone = EYE_SHOW_ICON;
        }
        else {
            icone = EYE_SEE_ICON;
        }
        senha.setCompoundDrawablesRelativeWithIntrinsicBounds(LOCK_ICON, 0, icone, 0);
    }
}