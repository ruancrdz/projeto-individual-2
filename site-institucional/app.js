// Configuração do ambiente de desenvolvimento ou produção
process.env.AMBIENTE_PROCESSO = "desenvolvimento";
// process.env.AMBIENTE_PROCESSO = "producao";

// Importação de módulos necessários
var express = require("express");
var cors = require("cors");
var path = require("path");

// Determinação da porta com base no ambiente
var PORTA = process.env.AMBIENTE_PROCESSO == "desenvolvimento" ? 3333 : 8080;

// Criação do aplicativo Express
var app = express();

// Importação de rotas
var indexRouter = require("./src/routes/index");
var dashboardRouter = require("./src/routes/dashboard");

// Configuração do aplicativo Express
app.use(express.json({ limit: '50mb' }));
app.use(express.urlencoded({ limit: '50mb', extended: true }));
app.use(express.static(path.join(__dirname, "public")));
app.use(cors());

// Associação de rotas
app.use("/", indexRouter);
app.use("/dashboard", dashboardRouter);

// Inicialização do servidor
app.listen(PORTA, function () {
    console.log(`Servidor do seu site já está rodando! Acesse o caminho a seguir para visualizar: http://localhost:${PORTA} \n
    Você está rodando sua aplicação em Ambiente de ${process.env.AMBIENTE_PROCESSO} \n
    \t\tSe "desenvolvimento", você está se conectando ao banco LOCAL (MySQL Workbench). \n
    \t\tSe "producao", você está se conectando ao banco REMOTO (SQL Server em nuvem Azure) \n
    \t\t\t\tPara alterar o ambiente, comente ou descomente as linhas 1 ou 2 no arquivo 'app.js'`);
});