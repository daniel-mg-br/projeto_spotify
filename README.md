# Projeto Spotify
<body style = "text-align: justify;">
<h3>Integrantes do grupo</h3>
<ul>
	<li>Daniel Mendonça Garica - 202504988</li>
	<li>Gean Pablo Pereira Camargo - 202501707</li>
	<li>Paulo César Ribeiro Soares - 202501291</li>
</ul>

<h3>Esta seção Readme está dividida em quatro partes:</h3>
<ol>
	<li>Organização de pacotes e código</l1>
	<li>Definição do projeto e requisitos</li>
	<li>Atores e casos de uso</li>
	<li>Classes conceituais</li>
</ol>

<p><strong>O tópico 1 citado acima tem como objetivo auxiliar no acompanhamento do trabalho final, explicitando os pacotes, onde estão as classes e sobre a organização e desenvolvimento do projeto</strong></p>

<h1>Organização de pacotes e código</h1>

<h2>Hierarquia de pacotes do projeto</h2>
<p>A pasta 'src' é a pasta raiz, onde todo o código fonte está localizado, ela possui os seguintes pacotes principais:

<ul>
	<li>Controller: pacote que compõem as classes "controladoras", ou seja que fazem o papel de 	intermediário entre o usuário da aplicação e o banco de dados. Garantem que o usuário não manipule 	diretamente o banco de dados do sistema;</li>
	<br>
	<li>Database: pacote onde estão localizadas as classes relacionadas ao banco de dados, como a classe 	que estabelece a conexão JDBC e as classes referentes às operações do banco de dados (seleção, 	inserção, etc.);</li>
	<br>
	<li>Main: pacote que contém a classe principal (main);</li>
	<br>
	<li>Model: pacote referente às classes "modelo", sendo elas Conteudo e Usuario, e suas filhas / 	classes associadas (como Ouvinte, Criador, Musica, Playlist, entre outras);</li>
	<br>
	<li>View: pacote referente à Interface Gráfica do Usuário (GUI), onde estarão localizadas as classes 	com as janelas e painéis, com seus botões e demais elementos. A GUI será implementada por meio da 	biblioteca Swing do Java, com o auxílio do pluggin Window Builder.</li>
</ul>

<p>A pasta 'media' contém os documentos referentes ao projeto, ou seja: o documento exclusivo para a definição e para os requisitos do projeto; o PNG do diagrama de casos de uso; e o PNG do diagrama de classes. O documento 'trabalho_spotify (1).pdf' também possui os dois diagramas.</p>

<h2>Considerações sobre a organização do código</h2>
<ul>
	<li>As definições do projeto e a implementação inicial das classes - seguindo o enunciado da primeira 	entrega - estão na branch 'main', podendo ser acessadas a qualquer momento. Porém, as próximas 	implementações e progresso ocorrerão na branch 'develop', então será necessário alterar a 	visualização da branch (no próprio GitHub) para acompanhá-las;</li>
	<br>
	<li>Alguns métodos envolvendo a manipulação de dados de usuários e conteúdos são temporários, e 	alguns estão incompletos, visto que a integração com banco de dados e a interface gráfica serão 	implementadas posteriormente;</li>
	<br>
</ul>

<h1>Definição e requisitos do projeto</h1>

<h2>Definição do problema</h2>
	<p> Sistema de gerenciamento de usuários, podendo ser ouvintes e artistas, e conteúdos – consistindo 	de 	músicas, álbuns, playlists e podcasts – ambos possuindo informações e status próprios. Permite 	manipulação de dados eficiente por meio de interface gráfica Swing e banco de dados SQLite. Pensado  	para serviço de streaming (Spotify, Deezer, SoundCloud, etc).</p>

<h2>Requisitos do projeto</h2>
	<ul>
		<li>Adicionar Usuários: adicionar registros de Ouvintes e Criador;</li>
		<li>Remover Usuários: remover registros de Ouvintes e Criador;</li>
		<li>Consultar Usuários: consultar dados de registros dos Usuários;</li>
		<li>Adicionar músicas ao banco de músicas do sistema;</li>
		<li>Remover músicas do banco de músicas do sistema;</li>
		<li>Criar playlists para os Usuários escutarem;</li>
		<li>Criar álbuns para os Artistas “lançarem”;</li>
		<li>Adicionar músicas disponíveis às playlists dos Usuários;</li>
		<li>Remover músicas nas playlists dos Usuários;</li>
		<li>Adicionar músicas aos álbuns dos Criadores;</li>
		<li>Remover músicas nos Álbuns dos Criadores;</li>
		<li>Consultar playlists e álbuns;</li>
		<li>Compartilhar playlists e lançar álbuns;</li>
		<li>Armazenamento em banco de dados com JDBC e SQLite</li>
		<li>Interface gráfica com Swing;</li>
	</ul>
	
<h1>Atores e casos de uso</h1>
	<ol>
		<li>
			Ator: Usuário - Representa o usuário real do sistema, seja ele Administrador, Criador ou 			Ouvinte.
			<ul>
				<li>Alterar dados pessoais</li>
				<li>Consultar dados pessoais</li>
			</ul>
		</li>
		<br>
		<li>
			Ator: Administrador - Responsável pela gestão global do sistema, incluindo contas e o acervo 			musical.
			<ul>
				<li>Herda do Ator Usuário;</li>
				<li>Adicionar usuários;</li>
				<li>Remover usuários;</li>
				<li>Consultar usuários;</li>
				<li>Consultar conteúdos;</li>
				<li>Remover conteúdos.</li>
			</ul>
		</li>
		<br>
		<li>
			Ator: Ouvinte - Representa o ouvinte comum do sistema.
			<ul>
				<li>Criar e excluir playlists;</li>
				<li>Adicionar músicas disponíveis às playlists;</li>
				<li>Remover músicas das playlists;</li>
				<li>Consultar playlists e álbuns/podcasts;</li>
				<li>Adicionar e remover playlists e álbuns/podcast favoritos</li>
				<li>Compartilhar playlists.</li>
				<li>Herda do Ator Usuário;</li>
			</ul>
		</li>
		<br>
		<li>
			Ator: Criador - Representa o criador de conteúdo.
			<ul>
				<li>Herda do ator Usuário;</li>
				<li>Criar / excluir álbuns e podcasts;</li>
				<li>Adicionar músicas aos álbuns e episódios aos podcasts;</li>
				<li>Remover músicas dos Álbuns e episódios dos podcasts;</li>
				<li> Lançar álbuns.</li>
			</ul>
		</li>
	</ol>
	
<h1>Classes conceituais</h1>
	<p>Classes conceituais para representar o problema e o relacionamento entre elas:</p>
	<ul>
		<li>Classe Conta: classe que representa a conta a qual os usuários estão vinculados, assim, há uma 		associação entre Conta e Usuário;</li>
		<br>
		<li>Classe Usuário: classe abstrata para representar os usuários do sistema. É classe mãe das 		classes Ouvinte, Criador e Administrador;</li>
		<br>
		<li>Classe Conteúdo: classe abstrata que representa os conteúdos gerenciados pelo sistema. Pode 		ser do tipo Música e do tipo Episódio. Também mantém uma relação de agregação com as classes 		Playlist, Álbum e Podcast;</li>
		<br>
		<li>Classe Administrador: classe filha de Usuario, é responsável pelo cadastro e suspensão de 		usuários (ouvintes ou criadores), pela consulta de conteúdos e pela remoção de conteúdo 		impróprio.</li>
	</ul>
</body>