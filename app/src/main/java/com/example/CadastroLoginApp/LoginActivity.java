package com.example.CadastroLoginApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText emailET, senhaET;
    private TextView mensagemCadastro;
    private OnClickShowText onClickText;
    private SharedPreferences sp;

    // Constante duração Toast
    private static final int DURACAO_TOAST = Toast.LENGTH_LONG;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Pega o último email cadastrado (veio da tela de cadastro) e insere no campo de email
        String emailUsuario = getIntent().getStringExtra("emailUsuario");
        emailET = findViewById(R.id.editTextEmailLogin);
        emailET.setText(emailUsuario);

        // Inicializa o SharedPreferences
        sp = getSharedPreferences("PREFS_USUARIO", MODE_PRIVATE);

        // Configura mostrar/exibir senha para o campo senha
        senhaET = findViewById(R.id.editTextSenhaLogin);
        onClickText = new OnClickShowText(senhaET);
        senhaET.setOnTouchListener(onClickText);

        // Configura o texto para ir na tela de cadastro
        configuraOpcaoIrTelaCadastro();
    }

    public void validarLogin(View v) {
        String emailUsuario = emailET.getText().toString().trim();
        String senhaUsuario = senhaET.getText().toString().trim();

        boolean camposVazios = verificarCamposVazios(emailUsuario, senhaUsuario);
        if (camposVazios){
            return;
        }

        // Recupera a lista de usuários
        String usuariosJson = sp.getString("usuarios", "[]");
        JSONArray usuarios;
        try {
            usuarios = new JSONArray(usuariosJson);

            // Percorre a lista de usuários para validar o login
            for (int i = 0; i < usuarios.length(); i++) {
                JSONObject usuario = usuarios.getJSONObject(i);
                if (emailUsuario.equals(usuario.getString("email")) && senhaUsuario.equals(usuario.getString("senha"))) {
                    exibirMensagemToast("Login realizado com sucesso!");
                    // Insira o startActivity para tela principal
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        exibirMensagemToast("Email ou senha incorretos!");
    }

    private boolean verificarCamposVazios(String emailUsuario, String senhaUsuario) {
        if (emailUsuario.isEmpty()  || senhaUsuario.isEmpty()) {
            StringBuilder mensagemErro = new StringBuilder("Os campos ");
            boolean estaVazio = true;

            if (emailUsuario.isEmpty()) {
                mensagemErro.append("email, ");
            }
            if (senhaUsuario.isEmpty()) {
                mensagemErro.append("senha, ");
            }

            // Remove a última vírgula e espaço e adiciona o ponto final
            mensagemErro.setLength(mensagemErro.length() - 2);
            mensagemErro.append(" estão vazios.");

            exibirMensagemToast(mensagemErro.toString());
            return estaVazio;
        }
        return false;
    }

    private void configuraOpcaoIrTelaCadastro() {
        // Configura o texto para ir para a tela de cadastro
        mensagemCadastro = findViewById(R.id.textViewCadastroLogin);
        mensagemCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void opcaoParaIrTelaCadastro() {
        mensagemCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intent);

                // Adicionando transição de animação personalizada
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void exibirMensagemToast(String mensagem) {
        Toast.makeText(this, mensagem, DURACAO_TOAST).show();
    }
}