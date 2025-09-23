package dev.com.protactic.dominio.principal;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

public class Definicao_capitaoFeature {

    @Dado("um jogador chamado {string}")
    public void um_jogador_chamado(String nomeJogador) {
        System.out.println("Criando jogador: " + nomeJogador);
    }

    @Dado("ele possui contrato {string} com o {string}")
    public void ele_possui_contrato_com_o(String statusContrato, String nomeClube) {
        System.out.println("Definindo contrato como " + statusContrato + " para o clube " + nomeClube);
    }

    @Dado("ele tem {string} de clube")
    public void ele_tem_de_clube(String tempoDeClube) {
        System.out.println("Definindo tempo de clube: " + tempoDeClube);
    }

    @Dado("sua minutagem é {string}")
    public void sua_minutagem_é(String tipoMinutagem) {
        System.out.println("Definindo minutagem como: " + tipoMinutagem);
    }

    @Quando("o treinador tenta definir {string} como capitão")
    public void o_treinador_tenta_definir_como_capitão(String nomeJogador) {
        System.out.println("Tentando definir " + nomeJogador + " como capitão.");
    }

    @Então("{string} deve ser definido como capitão do {string}")
    public void deve_ser_definido_como_capitão_do(String nomeJogador, String nomeClube) {
        System.out.println("Verificando se " + nomeJogador + " é o capitão do " + nomeClube);
    }

    @Então("{string} não deve ser definido como capitão do {string}")
    public void nao_deve_ser_definido_como_capitão_do(String nomeJogador, String nomeClube) {
        System.out.println("Verificando se " + nomeJogador + " NÃO é o capitão do " + nomeClube);
    }
}