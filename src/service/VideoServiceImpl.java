package service;

import model.Video;
import repository.VideoRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class VideoServiceImpl implements VideoService {
    private final VideoRepository repository;

    public VideoServiceImpl(VideoRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("O repositório não pode ser nulo.");
        }
        this.repository = repository;
    }

    @Override
    public void addVideo(Video video) {
        if (video == null) {
            throw new IllegalArgumentException("O vídeo não pode ser nulo.");
        }
        repository.save(video);
    }

    @Override
    public List<Video> listVideos() {
        return repository.findAll().stream().sorted(Comparator.comparing(Video::getTitulo)).collect(Collectors.toUnmodifiableList());
    }
}