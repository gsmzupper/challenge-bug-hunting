package management;

import model.Video;
import repository.FileVideoRepository;
import service.VideoService;

import java.util.*;
import java.util.stream.Collectors;

public class VideoManagement {
    private final VideoService videoService;
    private final FileVideoRepository fileVideoRepository; // Adicionado

    public VideoManagement(VideoService videoService, FileVideoRepository fileVideoRepository) {
        if (videoService == null) {
            throw new IllegalArgumentException("O serviço de vídeos não pode ser nulo.");
        }
        if (fileVideoRepository == null) {
            throw new IllegalArgumentException("O repositório de vídeos não pode ser nulo.");
        }
        this.videoService = videoService;
        this.fileVideoRepository = fileVideoRepository; // Inicializado
    }

    public void editVideo(String titulo, String novoTitulo, String novaDescricao, int novaDuracao, String novaCategoria, Date novaDataPublicacao) {
        List<Video> videos = new ArrayList<>(videoService.listVideos());
        Optional<Video> videoOptional = videos.stream()
                .filter(video -> video.getTitulo().equalsIgnoreCase(titulo))
                .findFirst();

        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            videos.remove(video);
            Video videoEditado = new Video(
                    novoTitulo != null ? novoTitulo : video.getTitulo(),
                    novaDescricao != null ? novaDescricao : video.getDescricao(),
                    novaDuracao > 0 ? novaDuracao : video.getDuracao(),
                    novaCategoria != null ? novaCategoria : video.getCategoria(),
                    novaDataPublicacao != null ? novaDataPublicacao : video.getDataPublicacao()
            );
            videos.add(videoEditado);

            // Persistir as alterações no arquivo
            fileVideoRepository.saveAll(videos);

            System.out.println("Vídeo editado com sucesso.");
        } else {
            System.out.println("Vídeo com o título '" + titulo + "' não encontrado.");
        }
    }

    public void deleteVideo(String titulo) {
        List<Video> videos = new ArrayList<>(videoService.listVideos());
        Optional<Video> videoOptional = videos.stream()
                .filter(video -> video.getTitulo().equalsIgnoreCase(titulo))
                .findFirst();

        if (videoOptional.isPresent()) {
            videos.remove(videoOptional.get());

            // Persistir as alterações no arquivo
            fileVideoRepository.saveAll(videos);

            System.out.println("Vídeo removido com sucesso.");
        } else {
            System.out.println("Vídeo com o título '" + titulo + "' não encontrado.");
        }
    }

    public List<Video> sortVideosByDate() {
        return videoService.listVideos().stream()
                .sorted(Comparator.comparing(Video::getDataPublicacao))
                .collect(Collectors.toList());
    }

    public void generateStatisticsReport() {
        List<Video> videos = videoService.listVideos();
        int totalVideos = videos.size();
        int totalDuration = videos.stream()
                .mapToInt(Video::getDuracao)
                .sum();
        Map<String, Long> videosByCategory = videos.stream()
                .collect(Collectors.groupingBy(Video::getCategoria, Collectors.counting()));

        System.out.println("Relatório de Estatísticas:");
        System.out.println("Número total de vídeos: " + totalVideos);
        System.out.println("Duração total de todos os vídeos: " + totalDuration + " minutos");
        System.out.println("Quantidade de vídeos por categoria:");
        videosByCategory.forEach((categoria, quantidade) -> System.out.println("- " + categoria + ": " + quantidade + " vídeo(s)"));
    }
}