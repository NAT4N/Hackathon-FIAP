# Roteiro de pitch — SUS Flow

**Duração prevista:** 7 minutos e 30 segundos.
**Uso:** abrir `Pitch-SUS-Flow.html` no navegador, usar as setas para avançar e gravar a tela/apresentação.

## Slide 1 — Abertura (0:00–0:45)

> Olá, nós somos a equipe responsável pelo SUS Flow. Nosso projeto nasce de uma pergunta simples: como usar tecnologia para tornar a triagem mais ágil sem retirar do profissional de saúde a decisão clínica?
> A nossa resposta é um copiloto de acolhimento que organiza dados, sugere perguntas e ajuda a encaminhar o paciente. A decisão de risco continua humana, transparente e baseada em regras.

**Transição:** “Antes de falar da solução, vamos contextualizar a dor que ela enfrenta.”

## Slide 2 — Problema (0:45–1:35)

> No atendimento público, o acolhimento precisa equilibrar velocidade e segurança. Um profissional recebe uma queixa em linguagem livre, precisa coletar sinais vitais, identificar sintomas relevantes e definir o próximo passo.
> Quando informações ficam incompletas ou dispersas, o processo perde padronização. E depois da triagem, o encaminhamento e o agendamento também precisam acompanhar a prioridade do paciente.

**Mensagem-chave:** não alegar que a solução resolve todo o SUS; delimitar o foco ao acolhimento, triagem e encaminhamento.

## Slide 3 — Solução (1:35–2:25)

> O SUS Flow reúne três etapas em um único fluxo de API: cadastro e consulta de pacientes, triagem clínica e agendamento.
> O profissional registra a queixa, revisa as informações e realiza a triagem. Depois, o sistema pode apoiar o agendamento de consulta ou exame considerando a prioridade derivada da triagem.

**Transição:** “O principal diferencial está em como usamos inteligência artificial nesse processo.”

## Slide 4 — IA responsável (2:25–3:25)

> Nossa IA não dá diagnóstico e não escolhe a cor de risco do paciente. Ela atua como assistente do profissional.
> Com Ollama executado localmente, a aplicação analisa a queixa e o contexto informado e retorna sintomas sugeridos, perguntas complementares, alertas para conferência e campos possivelmente ausentes.
> Em seguida, o profissional valida esses dados. Somente depois as regras determinísticas do domínio classificam o risco. Isso torna a decisão rastreável e evita delegar uma decisão clínica ao modelo de linguagem.

**Frase de impacto:** “A IA reduz esquecimento; as regras garantem consistência; o profissional mantém a autonomia.”

## Slide 5 — Fluxo (3:25–4:10)

> Este é o fluxo completo: a queixa entra no sistema, a IA organiza sugestões, o profissional valida, as regras classificam o risco e o sistema permite o encaminhamento para a agenda.
> Caso o Ollama não esteja disponível, o sistema degrada de forma segura: não interrompe a triagem e retorna a análise vazia. A operação essencial continua funcionando.

## Slide 6 — MVP e arquitetura (4:10–5:05)

> O MVP foi construído com Java 21 e Spring Boot. Usamos PostgreSQL e Flyway para persistência e versionamento do banco, JWT para autenticação, Docker para subir o ambiente completo e OpenAPI/Postman para demonstrar a API.
> A arquitetura separa controllers, casos de uso, regras de domínio e adaptadores externos. Por isso, a regra de risco não fica acoplada ao Ollama, e a IA pode ser desligada sem quebrar a aplicação.

## Slide 7 — Impacto e validação (5:05–6:00)

> Não queremos prometer impacto clínico sem um piloto real. Por isso, definimos métricas que devem ser medidas em uma implantação controlada: tempo até a classificação, completude dos dados da triagem e rastreabilidade do encaminhamento.
> A hipótese é que o copiloto reduza omissões no acolhimento e ajude a padronizar a coleta de informações, sempre com revisão humana.

## Slide 8 — Próximos passos e encerramento (6:00–7:30)

> Como próximos passos, evoluiríamos para um piloto em uma UBS ou UPA, com autorização baseada na unidade de saúde, histórico clínico, auditoria das sugestões de IA e observabilidade de métricas operacionais.
> O SUS Flow é um MVP de back-end que demonstra uma ideia central: tecnologia pode apoiar quem cuida, tornando o atendimento mais organizado, seguro e humano. Obrigado — agora vamos demonstrar o sistema funcionando.

## Dicas de gravação

1. Utilizem tela cheia no navegador (`F11`) e avancem com as setas.
2. Evitem ler os slides: eles são visuais; a narrativa está neste roteiro.
3. Reservem aproximadamente 30 segundos de margem para pausas e trocas de apresentador.
4. Após o pitch, gravem separadamente o vídeo de MVP usando a collection Postman.
