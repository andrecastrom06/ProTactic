package dev.com.protactic.apresentacao.principal;

import static org.springframework.boot.SpringApplication.run;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import dev.com.protactic.aplicacao.principal.clube.*;
import dev.com.protactic.aplicacao.principal.competicao.CompeticaoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.competicao.CompeticaoServicoAplicacao;
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
import dev.com.protactic.aplicacao.principal.usuario.UsuarioRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.usuario.UsuarioServicoAplicacao;
import dev.com.protactic.aplicacao.principal.fisico.FisicoRepositorioAplicacao;
import dev.com.protactic.aplicacao.principal.fisico.FisicoServicoAplicacao;
import dev.com.protactic.dominio.principal.login.LoginService; 
import dev.com.protactic.dominio.principal.login.UsuarioRepository;
import dev.com.protactic.dominio.principal.contrato.ContratoService; 

import dev.com.protactic.dominio.principal.capitao.*;
import dev.com.protactic.dominio.principal.cadastroAtleta.*;
import dev.com.protactic.dominio.principal.definirEsquemaTatico.*;
import dev.com.protactic.dominio.principal.dispensa.*;
import dev.com.protactic.dominio.principal.lesao.*;
import dev.com.protactic.dominio.principal.nota.*;
import dev.com.protactic.dominio.principal.partida.PartidaRepository;
import dev.com.protactic.dominio.principal.planejamentoCargaSemanal.PlanejamentoCargaSemanalRepositoryMock;
import dev.com.protactic.dominio.principal.planejamentoCargaSemanal.PlanejamentoCargaSemanalService;
import dev.com.protactic.dominio.principal.planejamentoFisico.FisicoRepository;
import dev.com.protactic.dominio.principal.planejamentoFisico.PlanejamentoFisicoService;
import dev.com.protactic.dominio.principal.premiacaoInterna.*;
import dev.com.protactic.dominio.principal.proposta.*;
import dev.com.protactic.dominio.principal.registroCartoesSuspensoes.*;
import dev.com.protactic.dominio.principal.registroInscricaoAtleta.*;
import dev.com.protactic.dominio.principal.treinoTatico.*;

@SpringBootApplication
@ComponentScan(basePackages = "dev.com.protactic")

@EnableJpaRepositories(basePackages = "dev.com.protactic.infraestrutura.persistencia.jpa")
@EntityScan(basePackages = "dev.com.protactic.infraestrutura.persistencia.jpa")
public class AplicacaoBackend {

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

   @Bean
    public CadastroDeAtletaService cadastroDeAtletaService(
            JogadorRepository jogadorRepo,
            ClubeRepository clubeRepo, 
            ContratoRepository contratoRepo) {
        return new CadastroDeAtletaService(jogadorRepo, clubeRepo, contratoRepo);
    }

    @Bean
    public RegistroInscricaoService registroInscricaoService(
            RegistroInscricaoRepository repository,
            JogadorRepository jogadorRepository,
            RegistroLesoesRepository lesoesRepository) {    
        return new RegistroInscricaoService(repository, jogadorRepository, lesoesRepository);
    }

    @Bean
    public UsuarioServicoAplicacao usuarioServicoAplicacao(UsuarioRepositorioAplicacao repositorio) {
        return new UsuarioServicoAplicacao(repositorio);
    }

    @Bean
    public CapitaoService capitaoService(
            CapitaoRepository capitaoRepo,
            JogadorRepository jogadorRepo) { 
        return new CapitaoService(capitaoRepo, jogadorRepo); 
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
            JogadorRepository jogadorRepo,
            ClubeRepository clubeRepo,    
            DispensaService dispensaService,
            CadastroDeAtletaService cadastroDeAtletaService) {
        
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
    public LoginService loginService(
            UsuarioRepository usuarioRepository, 
            
            ClubeRepository clubeRepository) {  
        
        return new LoginService(usuarioRepository, clubeRepository); 
    }
    
    @Bean
    public PremiacaoService premiacaoService(
            PremiacaoRepository premiacaoRepo,
            JogadorRepository jogadorRepo) { 
        return new PremiacaoService(premiacaoRepo, jogadorRepo);
    }

    @Bean
    public NotaService notaService(NotaRepository notaRepo) {
        return new NotaService(notaRepo);
    }

    @Bean
    public RegistroLesoesServico registroLesoesServico(
            RegistroLesoesRepository lesoesRepo,
            JogadorRepository jogadorRepo,      
            LesaoRepository lesaoRepo) {        
        return new RegistroLesoesServico(lesoesRepo, jogadorRepo, lesaoRepo); 
    }
    @Bean
    public RegistroCartoesService registroCartoesService(
            RegistroCartoesRepository cartaoRepository, 
            SuspensaoRepository suspensaoRepository) {
        
        return new RegistroCartoesService(cartaoRepository, suspensaoRepository);
    }

    @Bean
    public DefinirEsquemaTaticoService definirEsquemaTaticoService(
            EscalacaoRepository escalacaoRepo,
            JogadorRepository jogadorRepo,
            RegistroLesoesRepository lesoesRepo,
            RegistroCartoesService cartoesService
            ) {
        return new DefinirEsquemaTaticoService(
            escalacaoRepo, 
            jogadorRepo,
            lesoesRepo,
            cartoesService
        );
    }

    @Bean
    public SessaoTreinoService sessaoTreinoService(
            SessaoTreinoRepository sessaoRepo,
            PartidaRepository partidaRepo,      
            JogadorRepository jogadorRepo) {    
        return new SessaoTreinoService(sessaoRepo, partidaRepo, jogadorRepo); 
    }

    @Bean
    public PlanejamentoCargaSemanalRepositoryMock planejamentoCargaSemanalRepositoryMock() {
        return new PlanejamentoCargaSemanalRepositoryMock();
    }

    @Bean
    public PlanejamentoCargaSemanalService planejamentoCargaSemanalService(
            PlanejamentoCargaSemanalRepositoryMock repository,
            JogadorRepository jogadorRepository) { 
        return new PlanejamentoCargaSemanalService(repository, jogadorRepository); 
    }

    @Bean
    public FisicoServicoAplicacao fisicoServicoAplicacao(FisicoRepositorioAplicacao repositorio) {
        return new FisicoServicoAplicacao(repositorio);
    }
    
    @Bean
    public PlanejamentoFisicoService planejamentoFisicoService(
            FisicoRepository fisicoRepository,
            JogadorRepository jogadorRepository,
            PlanejamentoCargaSemanalService planejamentoCargaSemanalService) {
        return new PlanejamentoFisicoService(
            fisicoRepository, 
            jogadorRepository, 
            planejamentoCargaSemanalService
        );
    }
    @Bean
    public CompeticaoServicoAplicacao competicaoServicoAplicacao(CompeticaoRepositorioAplicacao repositorio) {
        return new CompeticaoServicoAplicacao(repositorio);
    }

    public static void main(String[] args) {
        run(AplicacaoBackend.class, args);
    }
}
