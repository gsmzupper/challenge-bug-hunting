package repository;

import model.Video;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileVideoRepository implements VideoRepository {
    private final File file;

    public FileVideoRepository(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("O caminho do arquivo não pode ser nulo ou vazio.");
        }
        this.file = new File(filePath);

    }

    @Override
    public void save(Video video) {
        if (video == null) {
            throw new IllegalArgumentException("O vídeo não pode ser nulo.");
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(video.toString());
            bw.newLine();
        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao salvar o vídeo: " + e.getMessage(), e);
    }

    @Override
    public List<Video> findAll() {
        List<Video> videos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                Video video = Video.fromString(line);
                videos.add(video);
            } catch (IllegalArgumentException e) {
System.err.println("Erro ao processar linha: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao ler os vídeos: " + e.getMessage(), e);
        }
        return videos;
    }
}