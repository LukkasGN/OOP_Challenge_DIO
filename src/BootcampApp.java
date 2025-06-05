import java.time.LocalDate;
import java.util.*;

/**
 * Modelo de domínio para um Bootcamp.
 * Demonstra Abstração, Encapsulamento, Herança e Polimorfismo.
 */
public class BootcampApp {
    public static void main(String[] args) {
        // Cria conteúdos
        Curso cursoJava = new Curso("Curso Java", "Fundamentos de Java", 8);
        Curso cursoPOO = new Curso("Curso POO", "Paradigma Orientado a Objetos", 6);
        Mentoria mentoriaCarreira = new Mentoria("Mentoria de Carreira", "Orientação profissional", LocalDate.now());

        // Cria bootcamp e adiciona conteúdos
        Bootcamp bootcamp = new Bootcamp("Bootcamp Java", "Aprenda Java do zero ao avançado");
        bootcamp.getConteudos().addAll(Arrays.asList(cursoJava, cursoPOO, mentoriaCarreira));

        // Cria devs e inscreve no bootcamp
        Dev devAlice = new Dev("Alice");
        Dev devBruno = new Dev("Bruno");
        devAlice.inscreverBootcamp(bootcamp);
        devBruno.inscreverBootcamp(bootcamp);

        // Progresso dos devs
        devAlice.progredir();
        devAlice.progredir();
        devBruno.progredir();

        // Exibe status
        System.out.println("Conteúdos inscritos de Alice: " + devAlice.getConteudosInscritos());
        System.out.println("Conteúdos concluídos de Alice: " + devAlice.getConteudosConcluidos());
        System.out.println("XP total de Alice: " + devAlice.calcularTotalXp());

        System.out.println("\nConteúdos inscritos de Bruno: " + devBruno.getConteudosInscritos());
        System.out.println("Conteúdos concluídos de Bruno: " + devBruno.getConteudosConcluidos());
        System.out.println("XP total de Bruno: " + devBruno.calcularTotalXp());
    }
}

abstract class Conteudo {
    protected static final double XP_PADRAO = 10d;

    private String titulo;
    private String descricao;

    public Conteudo(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public abstract double calcularXp();

    // Getters e Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    @Override
    public String toString() {
        return String.format("%s (XP: %.1f)", titulo, calcularXp());
    }
}

class Curso extends Conteudo {
    private int cargaHoraria; // em horas

    public Curso(String titulo, String descricao, int cargaHoraria) {
        super(titulo, descricao);
        this.cargaHoraria = cargaHoraria;
    }

    @Override
    public double calcularXp() {
        return XP_PADRAO * cargaHoraria;
    }

    // Getters e Setters
    public int getCargaHoraria() { return cargaHoraria; }
    public void setCargaHoraria(int cargaHoraria) { this.cargaHoraria = cargaHoraria; }
}

class Mentoria extends Conteudo {
    private LocalDate data;

    public Mentoria(String titulo, String descricao, LocalDate data) {
        super(titulo, descricao);
        this.data = data;
    }

    @Override
    public double calcularXp() {
        return XP_PADRAO + 20d;
    }

    // Getters e Setters
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
}

class Bootcamp {
    private String nome;
    private String descricao;
    private final LocalDate dataInicial = LocalDate.now();
    private final LocalDate dataFinal = dataInicial.plusDays(45);

    private Set<Dev> devsInscritos = new HashSet<>();
    private Set<Conteudo> conteudos = new LinkedHashSet<>();

    public Bootcamp(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDate getDataInicial() { return dataInicial; }
    public LocalDate getDataFinal() { return dataFinal; }
    public Set<Dev> getDevsInscritos() { return devsInscritos; }
    public Set<Conteudo> getConteudos() { return conteudos; }
}

class Dev {
    private String nome;
    private Set<Conteudo> conteudosInscritos = new LinkedHashSet<>();
    private Set<Conteudo> conteudosConcluidos = new LinkedHashSet<>();

    public Dev(String nome) {
        this.nome = nome;
    }

    public void inscreverBootcamp(Bootcamp bootcamp) {
        this.conteudosInscritos.addAll(bootcamp.getConteudos());
        bootcamp.getDevsInscritos().add(this);
    }

    public void progredir() {
        Optional<Conteudo> conteudo = this.conteudosInscritos.stream().findFirst();
        if (conteudo.isPresent()) {
            this.conteudosConcluidos.add(conteudo.get());
            this.conteudosInscritos.remove(conteudo.get());
        } else {
            System.err.println("Você não está matriculado em nenhum conteúdo!");
        }
    }

    public double calcularTotalXp() {
        return this.conteudosConcluidos
                   .stream()
                   .mapToDouble(Conteudo::calcularXp)
                   .sum();
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Set<Conteudo> getConteudosInscritos() { return conteudosInscritos; }
    public Set<Conteudo> getConteudosConcluidos() { return conteudosConcluidos; }

    @Override
    public String toString() {
        return nome;
    }
}
