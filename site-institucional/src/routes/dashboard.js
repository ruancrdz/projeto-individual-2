// Importa o módulo 'express' para criação de rotas
var express = require("express");

// Cria um objeto 'router' para definir rotas
var router = express.Router();

// Importa o controlador do dashboard que contém as funções necessárias
var dashboardController = require("../controllers/dashboardController");

// Rota para ação de 'calcular'
router.get("/calcular", function (req, res) {
    // Chama a função 'calcular' do controlador e passa os objetos de requisição e resposta
    dashboardController.calcular(req, res);
});

// Rota para obter métricas em tempo real
router.get("/metricas_tempo_real", function (req, res) {
    // Chama a função 'obterMetricasTempoReal' do controlador e passa os objetos de requisição e resposta
    dashboardController.obterMetricasTempoReal(req, res);
});

// Exporta o objeto de roteamento para ser utilizado em outras partes da aplicação
module.exports = router;
