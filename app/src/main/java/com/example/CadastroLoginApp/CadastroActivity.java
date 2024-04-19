package com.example.CadastroLoginApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CadastroActivity extends AppCompatActivity {
    private EditText nomeUsuarioET, emailUsuarioET, senhaUsuarioET, confirmarSenhaUsuarioET;
    private LinearLayout setaVoltar;
    private OnClickShowText onClickSenha;
    private OnClickShowText onClickConfirmarSenha;
    private SharedPreferences sp;

    // Constante duração Toast
    private static final int DURACAO_TOAST = Toast.LENGTH_LONG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Habilitar função de mostrar/exibir senha no campo Senha
        senhaUsuarioET = findViewById(R.id.editTextSenhaUsuarioCadastro);
        onClickSenha = new OnClickShowText(senhaUsuarioET);
        senhaUsuarioET.setOnTouchListener(onClickSenha);

        // Habilitar função de mostrar/exibir senha no campo ConfirmarSenha
        confirmarSenhaUsuarioET = findViewById(R.id.editTextConfirmarSenhaUsuarioCadastro);
        onClickConfirmarSenha = new OnClickShowText(confirmarSenhaUsuarioET);
        confirmarSenhaUsuarioET.setOnTouchListener(onClickConfirmarSenha);

        // Habilitar função de voltar a tela anterior apertando na seta de voltar
        setaVoltar = findViewById(R.id.linearLayoutSetaVoltar);
        voltarTelaAnterior();

        //Inicia o sharedPreferences
        sp = getSharedPreferences("PREFS_USUARIO", MODE_PRIVATE);
    }

    public void cadastrarUsuario(View v) throws JSONException {
        nomeUsuarioET = findViewById(R.id.editTextNomeUsuarioCadastro);
        emailUsuarioET = findViewById(R.id.editTextEmailUsuarioCadastro);

        String nomeUsuario = nomeUsuarioET.getText().toString().trim();
        String emailUsuario = emailUsuarioET.getText().toString().trim();
        String senhaUsuario = senhaUsuarioET.getText().toString().trim();
        String confirmarSenhaUsuario = confirmarSenhaUsuarioET.getText().toString().trim();

        //Validar se a senha e confirmarSenha estão iguais
        if (!validarSenha(senhaUsuario, confirmarSenhaUsuario)) {
            return;
        }

        boolean camposVazios = verificarCamposVazios(nomeUsuario, emailUsuario, senhaUsuario, confirmarSenhaUsuario);
        if (camposVazios){
            return;
        }

        boolean emailExiste = verificaEmailExistente(emailUsuario);
        if (emailExiste){
            exibirMensagemToast("Email já está em uso");
            return;
        }

        guardarDadosSharedPreferences(nomeUsuario, emailUsuario, senhaUsuario);
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        intent.putExtra("emailUsuario", emailUsuario);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void guardarDadosSharedPreferences(String nomeUsuario, String emailUsuario, String senhaUsuario) throws JSONException {
        //Guardar dados no Json
        JSONObject novoUsuario = new JSONObject();
        try{
            novoUsuario.put("nome", nomeUsuario);
            novoUsuario.put("email", emailUsuario);
            novoUsuario.put("senha", senhaUsuario);
        } catch (JSONException e){
            e.printStackTrace();
        }

        // Recupera a lista de usuários existentes do SharedPreferences
        String usuariosJson = sp.getString("usuarios", "");

        // Verifica se a lista de usuários está vazia
        if (usuariosJson.isEmpty()) {
            // Se estiver vazia, inicializa uma nova lista de usuários
            usuariosJson = "[]";
        }

        // Cria um novo JSONArray com a string JSON recuperada ou a lista vazia
        JSONArray usuarios = new JSONArray(usuariosJson);

        usuarios.put(novoUsuario);

        //Salvar no shared preferences
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("usuarios", usuarios.toString());
        editor.apply();
        exibirMensagemToast("Cadastro realizado com sucesso");
    }

    private boolean verificarCamposVazios(String nomeUsuario, String emailUsuario, String senhaUsuario, String confirmarSenhaUsuario) {
            if (nomeUsuario.isEmpty() || emailUsuario.isEmpty() || senhaUsuario.isEmpty() || confirmarSenhaUsuario.isEmpty()) {
                StringBuilder mensagemErro = new StringBuilder("Os campos ");
                boolean estaVazio = true;

                if (nomeUsuario.isEmpty()) {
                    mensagemErro.append("nome, ");
                }
                if (emailUsuario.isEmpty()) {
                    mensagemErro.append("email, ");
                }
                if (senhaUsuario.isEmpty()) {
                    mensagemErro.append("senha, ");
                }
                if (confirmarSenhaUsuario.isEmpty()) {
                    mensagemErro.append("confirmar senha, ");
                }

                // Remove a última vírgula e espaço e adiciona o ponto final
                mensagemErro.setLength(mensagemErro.length() - 2);
                mensagemErro.append(" estão vazios.");

                exibirMensagemToast(mensagemErro.toString());
                return estaVazio;
            }
            return false;
        }


    private boolean verificaEmailExistente(String emailUsuario) throws JSONException {
        String usuariosJson = sp.getString("usuarios", "[]");
        JSONArray usuarios = new JSONArray(usuariosJson);

        for (int i = 0; i < usuarios.length(); i++) {
            try {
                JSONObject usuario = usuarios.getJSONObject(i);
                if (emailUsuario.equals(usuario.getString("email"))) {
                    return true; //Email já existe
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false; //Email disponível
    }

    private boolean validarSenha(String senhaUsuario, String confirmarSenhaUsuario) {
        if (senhaUsuario.equals(confirmarSenhaUsuario)) {
            return true;
        } else {
            exibirMensagemToast("As senhas não coincidem!");
            return false;
        }
    }

    private void voltarTelaAnterior() {
        setaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    // Se apertar o botão de voltar no celular, ele volta com uma animação
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
    }

    private void exibirMensagemToast(String mensagem) {
        Toast.makeText(this, mensagem, DURACAO_TOAST).show();
    }
}