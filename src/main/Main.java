package main;

import management.VideoManagement;
import model.Video;
import repository.FileVideoRepository;
import service.VideoService;
import service.VideoServiceImpl;
import strategy.CategorySearchStrategy;
import strategy.TitleSearchStrategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final FileVideoRepository fr = new FileVideoRepository("./video.txt");
    private static final VideoService videoService = new VideoServiceImpl(fr);
    private static final TitleSearchStrategy searchStrategy = new TitleSearchStrategy();
    private static final CategorySearchStrategy category = new CategorySearchStrategy();
    private static final VideoManagement videoManagement = new VideoManagement(videoService, fr);

    public static void main(String[] args) {
        int opcao;
        do {
            menu();
            opcao = sc.nextInt();
            sc.nextLine();

            escolha(opcao);
        } while (opcao != 9);
        sc.close();
    }

    private static void menu() {
        System.out.println("\n=== Sistema de Gerenciamento de Vídeos ===");
        System.out.println("1. Adicionar vídeo");
        System.out.println("2. Listar vídeos");
        System.out.println("3. Pesquisar vídeo por título");
        System.out.println("4. Pesquisar vídeo por categoria");
        System.out.println("5. Editar vídeo");
        System.out.println("6. Excluir vídeo");
        System.out.println("7. Ordenar vídeos por data de publicação");
        System.out.println("8. Gerar relatório de estatísticas");
        System.out.println("9. Sair");
        System.out.print("Escolha uma opção: ");

    }

    private static void escolha(int es) {
        switch (es) {
            case 1:
                adicionarVideo();
                break;
            case 2:
                listarVideos();
                break;
            case 3:
                pesquisarVideo();
                break;
            case 4:
pesquisarPorCategoria();
                break;
            case 5:
editarVideo();
                break;
            case 6:
excluirVideo();
                break;
            case 7:
                ordenarPorData();
                break;
            case 8:
gerarRelatorioEstatisticas();
                break;
            case 9:
                System.out.println("Saindo do sistema. Até mais!");
                break;
            default:
                System.out.println("Digite uma opção válida! ");
        }
    }

    private static void adicionarVideo() {
        try {
            System.out.print("Digite o título do vídeo: ");
            String titulo = sc.nextLine();

            System.out.print("Digite a descrição do vídeo: ");
            String descricao = sc.nextLine();
            System.out.print("Digite a duração do vídeo (em minutos): ");
            int duracao = sc.nextInt();
            sc.nextLine();

            System.out.print("Digite a categoria do vídeo: ");
            String categoria = sc.nextLine();

            System.out.print("Digite a data de publicação (dd/MM/yyyy): ");
            String dataPublicacao = sc.nextLine();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dp = sdf.parse(dataPublicacao);

            Video video = new Video(titulo, descricao, duracao, categoria, dp);
            videoService.addVideo(video);
            System.out.println("Vídeo adicionado com sucesso!");

        } catch (ParseException e) {
            System.out.println("Erro ao adicionar vídeo: Formato de data inválido.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao adicionar vídeo: " + e.getMessage());
        }
    }

    private static void listarVideos() {
        List<Video> videos = videoService.listVideos();
        if (videos.isEmpty()) {
            System.out.println("Nenhum vídeo cadastrado.");
        } else {
            System.out.println("\n=== Lista de Vídeos ===");
            videos.forEach(video -> System.out.println(video.toString()));
        }
    }

    private static void pesquisarVideo() {
        System.out.print("Digite o título ou parte do título para pesquisar: ");
        String query = sc.nextLine();

        List<Video> videos = searchStrategy.search(videoService.listVideos(), query);
        if (videos.isEmpty()) {
            System.out.println("Nenhum vídeo encontrado com o título informado.");
        } else {
            System.out.println("\n=== Resultados da Pesquisa ===");
            videos.forEach(video -> System.out.println(video.toString()));

        }
    }

    private static void pesquisarPorCategoria() {
        System.out.print("Digite a categoria para pesquisar: ");
        String query = sc.nextLine();

        List<Video> videos = category.search(videoService.listVideos(), query);
        if (videos.isEmpty()) {
            System.out.println("Nenhum vídeo encontrado na categoria informada.");
        } else {
            System.out.println("\n=== Resultados da Pesquisa por Categoria ===");
            videos.forEach(video -> System.out.println(video.toString()));
        }
    }

    private static void editarVideo() {
        System.out.print("Digite o título do vídeo que deseja editar: ");
        String titulo = sc.nextLine();
        System.out.print("Digite o novo título (ou pressione Enter para manter o atual): ");
        String novoTitulo = sc.nextLine();

        System.out.print("Digite a nova descrição (ou pressione Enter para manter a atual): ");
        String novaDescricao = sc.nextLine();

        System.out.print("Digite a nova duração (ou 0 para manter a atual): ");
        int novaDuracao = sc.nextInt();
        sc.nextLine();

        System.out.print("Digite a nova categoria (ou pressione Enter para manter a atual): ");
        String novaCategoria = sc.nextLine();

        System.out.print("Digite a nova data de publicação (dd/MM/yyyy) (ou pressione Enter para manter a atual): ");
        String novaDataPublicacao = sc.nextLine();

        Date novaData = null;
        if (!novaDataPublicacao.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                novaData = sdf.parse(novaDataPublicacao);
            } catch (ParseException e) {
                System.out.println("Formato de data inválido. A data não será alterada.");
            }
        }

        videoManagement.editVideo(titulo, novoTitulo.isEmpty() ? null : novoTitulo,
                novaDescricao.isEmpty() ? null : novaDescricao,
                novaDuracao,
                novaCategoria.isEmpty() ? null : novaCategoria,
                novaData);
    }

    private static void excluirVideo() {
        System.out.print("Digite o título do vídeo que deseja excluir: ");
        String titulo = sc.nextLine();
        videoManagement.deleteVideo(titulo);
    }

    private static void ordenarPorData() {
        List<Video> videosOrdenados = videoManagement.sortVideosByDate();
        if (videosOrdenados.isEmpty()) {
            System.out.println("Nenhum vídeo cadastrado.");
        } else {
            System.out.println("\n=== Vídeos Ordenados por Data de Publicação ===");
            videosOrdenados.forEach(video -> System.out.println(video.toString()));
        }
    }

    private static void gerarRelatorioEstatisticas() {
        videoManagement.generateStatisticsReport();
    }
}