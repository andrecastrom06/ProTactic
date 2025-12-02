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
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.ClubeRepository;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.JogadorRepository;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.repositorio.UsuarioRepository;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.servico.CadastroDeAtletaService;
import dev.com.protactic.dominio.principal.feature_01_cadastro_atleta.servico.LoginService;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.repositorio.FisicoRepository;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.repositorio.PlanejamentoCargaSemanalRepositoryMock;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.servico.PlanejamentoCargaSemanalService;
import dev.com.protactic.dominio.principal.feature_02_carga_semanal.servico.PlanejamentoFisicoService;
import dev.com.protactic.dominio.principal.feature_03_registro_lesao.repositorio.LesaoRepository;
import dev.com.protactic.dominio.principal.feature_03_registro_lesao.repositorio.RegistroLesoesRepository;
import dev.com.protactic.dominio.principal.feature_03_registro_lesao.servico.RegistroLesoesServico;
import dev.com.protactic.dominio.principal.feature_04_esquema_escalacao.repositorio.EscalacaoRepository;
import dev.com.protactic.dominio.principal.feature_04_esquema_escalacao.servico.DefinirEsquemaTaticoService;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.repositorio.ContratoRepository;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.repositorio.PropostaRepository;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.servico.ContratoService;
import dev.com.protactic.dominio.principal.feature_05_proposta_contratacao.servico.PropostaService;
import dev.com.protactic.dominio.principal.feature_06_inscricao_competicao.repositorio.RegistroInscricaoRepository;
import dev.com.protactic.dominio.principal.feature_06_inscricao_competicao.servico.RegistroInscricaoService;
import dev.com.protactic.dominio.principal.feature_07_definir_capitao.repositorio.CapitaoRepository;
import dev.com.protactic.dominio.principal.feature_07_definir_capitao.servico.CapitaoService;
import dev.com.protactic.dominio.principal.feature_08_registro_cartoes.repositorio.RegistroCartoesRepository;
import dev.com.protactic.dominio.principal.feature_08_registro_cartoes.repositorio.SuspensaoRepository;
import dev.com.protactic.dominio.principal.feature_08_registro_cartoes.servico.RegistroCartoesService;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.repositorio.NotaRepository;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.repositorio.PartidaRepository;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.servico.NotaService;
import dev.com.protactic.dominio.principal.feature_09_atribuicao_notas.servico.PartidaService;
import dev.com.protactic.dominio.principal.feature_10_treino_tatico.repositorio.SessaoTreinoRepository;
import dev.com.protactic.dominio.principal.feature_10_treino_tatico.servico.SessaoTreinoService;
import dev.com.protactic.dominio.principal.feature_11_premiacao_interna.repositorio.PremiacaoRepository;
import dev.com.protactic.dominio.principal.feature_11_premiacao_interna.servico.PremiacaoService;
import dev.com.protactic.dominio.principal.feature_12_dispensa_rescisao.servico.ContratacaoServico;
import dev.com.protactic.dominio.principal.feature_12_dispensa_rescisao.servico.DispensaService;
import dev.com.protactic.dominio.principal.feature_12_dispensa_rescisao.servico.DispensaServiceProxy;
import dev.com.protactic.dominio.principal.feature_12_dispensa_rescisao.servico.IDispensaService;
import dev.com.protactic.dominio.principal.feature_12_dispensa_rescisao.servico.IDispensaService;
import dev.com.protactic.dominio.principal.feature_12_dispensa_rescisao.servico.DispensaServiceProxy;


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
    public ContratoService contratoService(ContratoRepository contratoRepo) {
        return new ContratoService(contratoRepo);
    }

    @Bean
    public PropostaService propostaService(
            PropostaRepository propostaRepo, 
            ContratoRepository contratoRepo,
            JogadorRepository jogadorRepo,
            ClubeRepository clubeRepo,    
            IDispensaService dispensaService, 
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
    public PartidaService partidaService(PartidaRepository partidaRepo, ClubeRepository clubeRepo, JogadorRepository jogadorRepo) {
        return new PartidaService(partidaRepo, clubeRepo, jogadorRepo);
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
    @Bean
    public IDispensaService dispensaService(
            ContratoRepository contratoRepo,
            JogadorRepository jogadorRepo, 
            ClubeRepository clubeRepo) {
        
        // 1. Cria o objeto Real
        DispensaService servicoReal = new DispensaService(contratoRepo, jogadorRepo, clubeRepo);
        
        // 2. Embrulha no Proxy e retorna para o Spring injetar
        return new DispensaServiceProxy(servicoReal);
    }
}