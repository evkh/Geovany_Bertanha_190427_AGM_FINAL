package com.example.listapersonagens.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.listapersonagens.R;
import com.example.listapersonagens.dao.PersonagemDAO;
import com.example.listapersonagens.model.Personagem;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.io.Serializable;

import static com.example.listapersonagens.ui.activities.ConstantesActivities.CHAVE_PERSONAGEM;

public class FormularioPersonagemActivity extends AppCompatActivity {

    //variavel dos nomes no cabeçalho
    private static final String TITULO_APPBAR_EDITA_PERSONAGEM = "Editar Personagem" ;
    private static final String TITULO_APPBAR_NOVO_PERSONAGEM = "Novo Personagem";

    //variavel dos campos de preenchimento
    private EditText campoNome;
    private EditText campoAltura;
    private EditText campoNascimento;
    private EditText campoTelefone;
    private EditText campoEndereco;
    private EditText campoCep;
    private EditText camporg;
    private EditText campogenero;
    private final PersonagemDAO dao = new PersonagemDAO();
    private Personagem personagem;

    //abre o scrollview
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_formulario_personagem_menu_salvar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //finaliza o scrollview ao clicar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        //pegando item pelo id
        if (itemId == R.id.activity_formulario_personagem_menu_salvar){
            FinalizaFormulario();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    //faz o processo de inicialização
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pegando item pelo id
        setContentView(R.layout.activity_formulario_personagem);
        //ordem da execução dos eventos no Oncreate
        inicializaCampos();
        configuraoBotaoSalvar();
        carregaPersonagem();
    }

    //carrega o personagem para fazer o edit / criar
    private void carregaPersonagem() {
        Intent dados = getIntent();
        if (dados.hasExtra(CHAVE_PERSONAGEM)) {
            setTitle(TITULO_APPBAR_EDITA_PERSONAGEM);
            personagem = (Personagem) dados.getSerializableExtra(CHAVE_PERSONAGEM);
            preencheCampos();
        } else {
            setTitle(TITULO_APPBAR_NOVO_PERSONAGEM);
            personagem = new Personagem();
        }
    }

    //Coloca conteudo nos campos
    private void preencheCampos() {
        campoNome.setText(personagem.getNome());
        campoAltura.setText(personagem.getAltura());
        campoNascimento.setText(personagem.getNascimento());
        campoTelefone.setText(personagem.getTelefone());
        campoCep.setText(personagem.getCep());
        campoEndereco.setText(personagem.getEndereco());
        camporg.setText(personagem.getRg());
        campogenero.setText(personagem.getGenero());
    }

    //salva o conteudo ao clicar
    private void configuraoBotaoSalvar() {
        Button botaosalvar = findViewById(R.id.savebutton);
        botaosalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FinalizaFormulario();
            }
        });
    }

    //Finaliza e salva formulario
    private void FinalizaFormulario() {
        preenchePersonagens();
        if (personagem.IdValido()) {
            dao.edita(personagem);
        } else {

            dao.salva(personagem);
        }
        finish();
    }

    //Pegando os ids do editText e inicializando, configura o layout
    private void inicializaCampos() {
        //pegando itens pelo id que estão no xml
        campoNome = findViewById(R.id.editText_nome);
        campoAltura = findViewById(R.id.editText_altura);
        campoNascimento = findViewById(R.id.editText_nascimento);
        campogenero = findViewById(R.id.editText_genero);
        camporg = findViewById(R.id.editText_rg);
        campoCep = findViewById(R.id.editText_cep);
        campoEndereco = findViewById(R.id.editText_endereco);
        campoTelefone = findViewById(R.id.editText_telefone);

        //configura a formatação com imports de outra biblioteca
        SimpleMaskFormatter smfAltura = new SimpleMaskFormatter("N,NN");
        MaskTextWatcher mtwAltura = new MaskTextWatcher(campoAltura, smfAltura);
        campoAltura.addTextChangedListener(mtwAltura);

        //configura a formatação layout com imports de outra biblioteca
        SimpleMaskFormatter smfNascimento = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtwNascimento = new MaskTextWatcher(campoNascimento, smfNascimento);
        campoNascimento.addTextChangedListener(mtwNascimento);

        //configura a formatação layout com imports de outra biblioteca
        SimpleMaskFormatter smfRg = new SimpleMaskFormatter("NN.NNN.NNN-N");
        MaskTextWatcher mtwRG = new MaskTextWatcher(camporg, smfRg);
        camporg.addTextChangedListener(mtwRG);

        //configura a formatação layout com imports de outra biblioteca
        SimpleMaskFormatter smfcep = new SimpleMaskFormatter("NNNNN-NN");
        MaskTextWatcher mtwcep = new MaskTextWatcher(campoCep, smfcep);
        campoCep.addTextChangedListener(mtwcep);

        //configura a formatação layout com imports de outra biblioteca
        SimpleMaskFormatter smftelefone = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher mtwtelefone = new MaskTextWatcher(campoTelefone, smftelefone);
        campoTelefone.addTextChangedListener(mtwtelefone);
    }

    //usado para dar conteudo aos campos e transforma em string
    private void preenchePersonagens() {
        String nome = campoNome.getText().toString();
        String altura = campoAltura.getText().toString();
        String nascimento = campoNascimento.getText().toString();
        String telefone = campoTelefone.getText().toString();
        String rg = camporg.getText().toString();
        String cep = campoCep.getText().toString();
        String genero = campogenero.getText().toString();
        String endereco = campoEndereco.getText().toString();

        personagem.setNome(nome);
        personagem.setAltura(altura);
        personagem.setNascimento(nascimento);
        personagem.setEndereco(endereco);
        personagem.setRg(rg);
        personagem.setCep(cep);
        personagem.setTelefone(telefone);
        personagem.setGenero(genero);
    }
}