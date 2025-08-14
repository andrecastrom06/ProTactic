import requests
from pathlib import Path
import pandas as pd

API_KEY = "913572987f0c369f9a5f42391628abf9"  # substitua pela sua chave
TEAM_ID = 123                                 # Sport Recife
SEASON = 2023                                 # ajuste se quiser
OUTFILE = Path("testeAPI.xlsx")

BASE_URL = "https://v3.football.api-sports.io/players"
HEADERS = {"x-apisports-key": API_KEY, "Accept": "application/json"}


def flatten_dict(d, parent_key="", sep="_"):
    """Achata um dicionário aninhado para um nível só."""
    items = []
    for k, v in (d or {}).items():
        new_key = f"{parent_key}{sep}{k}" if parent_key else k
        if isinstance(v, dict):
            items.extend(flatten_dict(v, new_key, sep=sep).items())
        else:
            items.append((new_key, v))
    return dict(items)


def fetch_all_players(team_id: int, season: int):
    players = []
    page = 1
    while True:
        params = {"team": team_id, "season": season, "page": page}
        r = requests.get(BASE_URL, headers=HEADERS, params=params, timeout=30)
        if r.status_code != 200:
            raise SystemExit(f"Erro {r.status_code}: {r.text}")

        payload = r.json()
        resp = payload.get("response", [])
        paging = payload.get("paging", {}) or {}
        if not resp:
            break

        for item in resp:
            player_data = flatten_dict(item.get("player", {}))
            stats_data = flatten_dict((item.get("statistics") or [{}])[0])
            combined = {**player_data, **stats_data}
            players.append(combined)

        current = paging.get("current", page)
        total = paging.get("total", page)
        if current >= total:
            break
        page += 1

    return players


def save_xlsx(players):
    if not players:
        return

    # Criar DataFrame com todas as colunas possíveis
    df = pd.DataFrame(players)

    # Dicionário de renomeação
    rename_map = {
        "age": "idade",
        "birth_country": "pais_nascimento",
        "birth_date": "data_nascimento",
        "birth_place": "cidade_nascimento",
        "cards_red": "cartoes_vermelhos",
        "cards_yellow": "cartoes_amarelos",
        "cards_yellowred": "cartoes_amarelo_vermelho",
        "dribbles_attempts": "dribles_tentados",
        "dribbles_past": "dribles_sofridos",
        "dribbles_success": "dribles_sucesso",
        "duels_total": "duelos_totais",
        "duels_won": "duelos_vencidos",
        "firstname": "primeiro_nome",
        "fouls_committed": "faltas_cometidas",
        "fouls_drawn": "faltas_sofridas",
        "games_appearences": "jogos_disputados",
        "games_captain": "capitao",
        "games_lineups": "titularidades",
        "games_minutes": "minutos_jogados",
        "games_number": "numero_camisa",
        "games_position": "posicao",
        "games_rating": "avaliacao",
        "goals_assists": "assistencias",
        "goals_conceded": "gols_sofridos",
        "goals_saves": "defesas",
        "goals_total": "gols_marcados",
        "height": "altura",
        "id": "id_jogador",
        "injured": "lesionado",
        "lastname": "sobrenome",
        "league_country": "pais_liga",
        "league_flag": "bandeira_liga",
        "league_id": "id_liga",
        "league_logo": "logo_liga",
        "league_name": "nome_liga",
        "league_season": "temporada",
        "name": "nome_completo",
        "nationality": "nacionalidade",
        "passes_accuracy": "precisao_passes",
        "passes_key": "passes_chave",
        "passes_total": "passes_totais",
        "penalty_commited": "penaltis_cometidos",
        "penalty_missed": "penaltis_perdidos",
        "penalty_saved": "penaltis_defendidos",
        "penalty_scored": "penaltis_convertidos",
        "penalty_won": "penaltis_sofridos",
        "photo": "foto",
        "shots_on": "chutes_no_alvo",
        "shots_total": "chutes_totais",
        "substitutes_bench": "banco_reservas",
        "substitutes_in": "substituicoes_entrada",
        "substitutes_out": "substituicoes_saida",
        "tackles_blocks": "bloqueios",
        "tackles_interceptions": "interceptacoes",
        "tackles_total": "desarmes_totais",
        "team_id": "id_time",
        "team_logo": "logo_time",
        "team_name": "nome_time",
        "weight": "peso"
    }

    # Renomear colunas existentes
    df = df.rename(columns=rename_map)

    # Salvar para Excel
    df.to_excel(OUTFILE, index=False)


def main():
    players = fetch_all_players(TEAM_ID, SEASON)

    # Exemplo no terminal (primeiro jogador já com nomes originais)
    if players:
        print("Exemplo de jogador retornado (original):")
        for k, v in list(players[0].items()):
            print(f"{k}: {v}")
        print(f"... total de {len(players[0])} campos no exemplo.")

    save_xlsx(players)
    print(f"XLSX gerado com {len(players)} jogadores: {OUTFILE.resolve()}")


if __name__ == "__main__":
    main()