var express = require("express");
var router = express.Router();

// Rota para a página inicial ("/")
router.get("/", function (req, res) {
    // Renderiza a página "index" (presumivelmente um arquivo HTML ou template do seu motor de visualização)
    res.render("index");
});

// Exporta o roteador para ser utilizado em outros arquivos
module.exports = router;