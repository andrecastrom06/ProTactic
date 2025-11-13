package dev.com.protactic.apresentacao.principal;

import static org.springframework.boot.SpringApplication.run;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

// --- (INÍCIO DA CORREÇÃO) ---
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
// --- (FIM DA CORREÇÃO) ---

// Importa todos os Serviços de APLICAÇÃO (Queries)
import dev.com.protactic.aplicacao.principal.clube.*;
import dev.com.protactic.aplicacao.principal.contrato.*;
import dev.com.protactic.aplicacao.principal.escalacao.*;
import dev.com.protactic.aplicacao.principal.inscricaoatleta.*;
import dev.com.protactic.aplicacao.principal.jogador.*;
import dev.com.protactic.aplicacao.principal.lesao.*;
import dev.com.protactic.aplicacao.principal.nota.*;
import dev.com.protactic.aplicacao.principal.partida.*;
import dev.com.protactic.aplicacao.principal.premiacao.*;
import dev.com.protactic.aplicacao.principal.proposta.*;
import dev.com.protactic.aplicacao.principal.registrocartao.*;
import dev.com.protactic.aplicacao.principal.sessaotreino.*;
import dev.com.protactic.aplicacao.principal.fisico.FisicoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.fisico.FisicoServicoAplicacao;
import dev.com.protactic.dominio.principal.planejamentoFisico.FisicoRepository;
import dev.com.protactic.dominio.principal.contrato.ContratoService; // <-- ADICIONA ESTE

// Importa Repositórios e Serviços de DOMÍNIO
import dev.com.protactic.dominio.principal.capitao.*;
import dev.com.protactic.dominio.principal.cadastroAtleta.*;
import dev.com.protactic.dominio.principal.definirEsquemaTatico.*;
import dev.com.protactic.dominio.principal.dispensa.*;
import dev.com.protactic.dominio.principal.lesao.*;
import dev.com.protactic.dominio.principal.nota.*;
import dev.com.protactic.dominio.principal.planejamentoCargaSemanal.PlanejamentoCargaSemanalRepositoryMock;
import dev.com.protactic.dominio.principal.planejamentoCargaSemanal.PlanejamentoCargaSemanalService;
import dev.com.protactic.dominio.principal.premiacaoInterna.*;
import dev.com.protactic.dominio.principal.proposta.*;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.*;
import dev.com.protactic.dominio.principal.registroInscricaoAtleta.*;
import dev.com.protactic.dominio.principal.treinoTatico.*;

@SpringBootApplication
@ComponentScan(basePackages = "dev.com.protactic")

// --- (INÍCIO DA CORREÇÃO) ---
// Força o Spring a procurar Repositórios e Entidades dentro do módulo de infraestrutura
@EnableJpaRepositories(basePackages = "dev.com.protactic.infraestrutura.persistencia.jpa")
@EntityScan(basePackages = "dev.com.protactic.infraestrutura.persistencia.jpa")
// --- (FIM DA CORREÇÃO) ---
public class AplicacaoBackend {

    // --- SERVIÇOS DE APLICAÇÃO ---
    @Bean
    public JogadorServicoAplicacao jogadorServicoAplicacao(JogadorRepositorioAplicacao repositorio) {
        return new JogadorServicoAplicacao(repositorio);
    }
    @Bean
    public ClubeServicoAplicacao clubeServicoAplicacao(ClubeRepositorioAplicacao repositorio) {
        return new ClubeServicoAplicacao(repositorio);
    }
    @Bean
    public ContratoServicoAplicacao contratoServicoAplicacao(ContratoRepositorioAplicacao repositorio) {
        return new ContratoServicoAplicacao(repositorio);
    }
    @Bean
    public EscalacaoServicoAplicacao escalacaoServicoAplicacao(EscalacaoRepositorioAplicacao repositorio) {
        return new EscalacaoServicoAplicacao(repositorio);
    }
    @Bean
    public InscricaoAtletaServicoAplicacao inscricaoAtletaServicoAplicacao(InscricaoAtletaRepositorioAplicacao repositorio) {
        return new InscricaoAtletaServicoAplicacao(repositorio);
    }
    @Bean
    public LesaoServicoAplicacao lesaoServicoAplicacao(LesaoRepositorioAplicacao repositorio) {
        return new LesaoServicoAplicacao(repositorio);
    }
    @Bean
    public NotaServicoAplicacao notaServicoAplicacao(NotaRepositorioAplicacao repositorio) {
        return new NotaServicoAplicacao(repositorio);
    }
    @Bean
    public PartidaServicoAplicacao partidaServicoAplicacao(PartidaRepositorioAplicacao repositorio) {
        return new PartidaServicoAplicacao(repositorio);
    }
    @Bean
    public PremiacaoServicoAplicacao premiacaoServicoAplicacao(PremiacaoRepositorioAplicacao repositorio) {
        return new PremiacaoServicoAplicacao(repositorio);
    }
    @Bean
    public PropostaServicoAplicacao propostaServicoAplicacao(PropostaRepositorioAplicacao repositorio) {
        return new PropostaServicoAplicacao(repositorio);
    }
    @Bean
    public RegistroCartaoServicoAplicacao registroCartaoServicoAplicacao(RegistroCartaoRepositorioAplicacao repositorio) {
        return new RegistroCartaoServicoAplicacao(repositorio);
    }
    @Bean
    public SessaoTreinoServicoAplicacao sessaoTreinoServicoAplicacao(SessaoTreinoRepositorioAplicacao repositorio) {
        return new SessaoTreinoServicoAplicacao(repositorio);
    }

    // --- SERVIÇOS DE DOMÍNIO ---
    @Bean
    public CadastroDeAtletaService cadastroDeAtletaService(
            JogadorRepository jogadorRepo,
            ClubeRepository clubeRepo, 
            ContratoRepository contratoRepo) {
        return new CadastroDeAtletaService(jogadorRepo, clubeRepo, contratoRepo);
    }

    @Bean
    public RegistroInscricaoService registroInscricaoService(
            RegistroInscricaoRepository repository) {     
        return new RegistroInscricaoService(repository);
    }

    @Bean
    public CapitaoService capitaoService(CapitaoRepository capitaoRepo) {
        return new CapitaoService(capitaoRepo);
    }

    @Bean
    public ContratacaoServico contratacaoServico(
            ClubeRepository clubeRepo,
            JogadorRepository jogadorRepo,
            ContratoRepository contratoRepo) {
        return new ContratacaoServico(clubeRepo, jogadorRepo, contratoRepo); 
    }

    @Bean
    public DispensaService dispensaService(
            ContratoRepository contratoRepo,
            JogadorRepository jogadorRepo, 
            ClubeRepository clubeRepo) {
        return new DispensaService(contratoRepo, jogadorRepo, clubeRepo);
    }

    @Bean
    public ContratoService contratoService(ContratoRepository contratoRepo) {
        return new ContratoService(contratoRepo);
    }

    @Bean
    public PropostaService propostaService(
            PropostaRepository propostaRepo, 
            ContratoRepository contratoRepo,
            JogadorRepository jogadorRepo, // <-- Adicionado
            ClubeRepository clubeRepo,     // <-- Adicionado
            DispensaService dispensaService, // <-- Adicionado
            CadastroDeAtletaService cadastroDeAtletaService) { // <-- Adicionado
        
        return new PropostaService(
            propostaRepo, 
            contratoRepo,
            jogadorRepo,
            clubeRepo,
            dispensaService,
            cadastroDeAtletaService
        );
    }

    @Bean
    public PremiacaoService premiacaoService(PremiacaoRepository premiacaoRepo) {
        return new PremiacaoService(premiacaoRepo);
    }

    @Bean
    public NotaService notaService(NotaRepository notaRepo) {
        return new NotaService(notaRepo);
    }

    @Bean
    public RegistroLesoesServico registroLesoesServico(RegistroLesoesRepository lesoesRepo) {
        return new RegistroLesoesServico(lesoesRepo);
    }

    @Bean
    public RegistroCartoesService registroCartoesService(
            RegistroCartoesRepository cartaoRepository, 
            SuspensaoRepository suspensaoRepository) {
        
        // Chama o único construtor que existe agora
        return new RegistroCartoesService(cartaoRepository, suspensaoRepository);
    }

    @Bean
    public DefinirEsquemaTaticoService definirEsquemaTaticoService(
            EscalacaoRepository escalacaoRepo) {
        return new DefinirEsquemaTaticoService(escalacaoRepo);
    }

    @Bean
    public SessaoTreinoService sessaoTreinoService(SessaoTreinoRepository sessaoRepo) {
        return new SessaoTreinoService(sessaoRepo);
    }

    @Bean
    public PlanejamentoCargaSemanalRepositoryMock planejamentoCargaSemanalRepositoryMock() {
        return new PlanejamentoCargaSemanalRepositoryMock();
    }

    @Bean
    public PlanejamentoCargaSemanalService planejamentoCargaSemanalService(
            PlanejamentoCargaSemanalRepositoryMock repository) {
        return new PlanejamentoCargaSemanalService(repository);
    }

    @Bean
    public FisicoServicoAplicacao fisicoServicoAplicacao(FisicoRepositorioAplicacao repositorio) {
        return new FisicoServicoAplicacao(repositorio);
    }

    // --- MÉTODO PRINCIPAL ---
    public static void main(String[] args) {
        run(AplicacaoBackend.class, args);
    }
}
