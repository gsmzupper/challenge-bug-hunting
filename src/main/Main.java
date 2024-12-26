package main;

import model.Video;
import repository.FileVideoRepository;
import service.VideoService;
import service.VideoServiceImpl;
import strategy.TitleSearchStrategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MainTemp {
    private static final Scanner sc = new Scanner(System.in);
    private static final VideoService videoService = new VideoServiceImpl(new FileVideoRepository("./video.txt"));
    private static final TitleSearchStrategy searchStrategy = new TitleSearchStrategy();

    public static void main(String[] args) {
        int opcao;
        do {
            menu();
            opcao = sc.nextInt();
            sc.nextLine();

            escolha(opcao);
        } while (opcao != 4);
        sc.close();
    }

    private static void menu() {
        System.out.println("\n=== Sistema de Gerenciamento de Vídeos ===");
        System.out.println("1. Adicionar vídeo");
        System.out.println("2. Listar vídeos");
        System.out.println("3. Pesquisar vídeo por título");
        System.out.println("4. Sair");
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
}
