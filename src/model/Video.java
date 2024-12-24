package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Video {
    private final String titulo;
    private final String descricao;
    private final int duracao; // em minutos
    private final String categoria;
    private final Date dataPublicacao;

    public Video(String titulo, String descricao, int duracao, String categoria, Date dataPublicacao) {
        if (titulo == null || titulo.isEmpty()) {
            throw new IllegalArgumentException("O título não pode ser nulo ou vazio.");
        }
        if (descricao == null || descricao.isEmpty()) {
            throw new IllegalArgumentException("A descrição não pode ser nula ou vazia.");
        }
        if (duracao <= 0) {
            throw new IllegalArgumentException("A duração deve ser maior que zero.");
        }
        if (categoria == null || categoria.isEmpty()) {
            throw new IllegalArgumentException("A categoria não pode ser nula ou vazia.");
        }
        if (dataPublicacao == null) {
            throw new IllegalArgumentException("A data de publicação não pode ser nula.");
        }
        this.titulo = titulo;
        this.descricao = descricao;
        this.duracao = duracao;
        this.categoria = categoria;
        this.dataPublicacao = dataPublicacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getDuracao() {
        return duracao;
    }

    public String getCategoria() {
        return categoria;
    }

    public Date getDataPublicacao() {
        return dataPublicacao;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return titulo + ";" + descricao + ";" + duracao + ";" + categoria + ";" + sdf.format(dataPublicacao);
    }

    public static Video fromString(String linha) {
        try {
            String[] partes = linha.split(";");
            if (partes.length != 5) {
                throw new IllegalArgumentException("A linha não contém o número esperado de campos.");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return new Video(partes[0], partes[1], Integer.parseInt(partes[2]), partes[3], sdf.parse(partes[4]));
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao converter a string para um objeto Video: " + e.getMessage(), e);
        }
    }
}