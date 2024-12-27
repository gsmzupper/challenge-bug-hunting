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

        verificaArquivo();
    }


    private void verificaArquivo() {
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    System.out.println("Arquivo criado: " + file.getAbsoluteFile());
                } else {
                    System.err.println("Não foi possível criar o arquivo: " + file.getAbsoluteFile());
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao criar o arquivo: " + e.getMessage(), e);
        }

    }

    @Override
    public void save(Video video) {
        if (video == null) {
            throw new IllegalArgumentException("O vídeo não pode ser nulo.");
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(video.toString());
            bw.newLine();
            System.out.println("Vídeo salvo com sucesso no arquivo: " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao salvar o vídeo: " + e.getMessage(), e);
        }
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

    public void saveAll(List<Video> videos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            for (Video video : videos) {
                bw.write(video.toString());
                bw.newLine();
            }
            System.out.println("Arquivo atualizado com sucesso.");

        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao atualizar o arquivo: " + e.getMessage(), e);
        }
    }
}