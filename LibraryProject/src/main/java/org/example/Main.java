package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.function.Function;

public class Main {

    private static List<Book> books = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static List<IssueBook> issues = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // 👇 seed inicial: um admin para você conseguir logar
        users.add(new User("admin123", Role.ADMIN));
        users.add(new User("user123", Role.USER));
        // IDs gerados: admin = 1, user = 2

        int option;
        do {
            System.out.println("\n=== MENU ===");
            System.out.println("1 - Adicionar livro");
            System.out.println("2 - Listar livros");
            System.out.println("3 - Deletar livro");
            System.out.println("4 - Reduzir quantidade");
            System.out.println("5 - Buscar por nome");
            System.out.println("6 - Buscar por autor");
            System.out.println("7 - Cadastrar usuário");
            System.out.println("8 - Listar usuários");
            System.out.println("9 - Emitir livro (somente Admin)");
            System.out.println("10 - Listar empréstimos");
            System.out.println("11 - Sair");
            System.out.print("Escolha: ");

            option = lerInt();

            switch (option) {
                case 1 -> adicionarLivro();
                case 2 -> listarLivros();
                case 3 -> deletarLivro();
                case 4 -> reduzirQuantidade();
                case 5 -> buscarPorNome();
                case 6 -> buscarPorAutor();
                case 7 -> cadastrarUsuario();
                case 8 -> listarUsuarios();
                case 9 -> emitirLivro();
                case 10 -> listarEmprestimos();
                case 11 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }
        } while (option != 11);

        sc.close();
    }

    // ---------- CARREGAR DADOS ----------
    private static void carregarDados() {
        books  = Persistence.loadBooks();
        users  = Persistence.loadUsers();
        issues = Persistence.loadIssues();

        // Sincroniza contadores estáticos para evitar colisão de IDs
        books.stream().mapToInt(Book::getBookId).max().ifPresent(Book::syncNextId);
        users.stream().mapToInt(User::getUserId).max().ifPresent(User::syncNextId);

        // Se primeira execução (sem usuários), cria seeds
        if (users.isEmpty()) {
            users.add(new User("admin123", Role.ADMIN));
            users.add(new User("user123", Role.USER));
            Persistence.saveUsers(users);
            System.out.println("ℹ Seed inicial criado (admin=1, user=2).");
        }
    }


    // ---------- 1. ADICIONAR ----------
    private static void adicionarLivro() {
        System.out.print("Nome do livro: ");
        String nome = sc.nextLine();
        System.out.print("Autor: ");
        String autor = sc.nextLine();
        System.out.print("Preço: ");
        double preco = lerDouble();
        System.out.print("Quantidade: ");
        int qtd = lerInt();

        Book novo = new Book(nome, autor, preco, qtd);
        books.add(novo);
        Persistence.saveBooks(books);   // 👈 persiste

        System.out.println("✔ Livro adicionado com ID " + novo.getBookId());
    }

    // ---------- 2. LISTAR ----------
    private static void listarLivros() {
        if (books.isEmpty()) {
            System.out.println("⚠ Nenhum livro cadastrado.");
            return;
        }
        for (Book b : books) {
            System.out.println(b);
        }
    }

    // ---------- 3. DELETAR ----------
    private static void deletarLivro() {
        System.out.print("ID do livro a deletar: ");
        int id = lerInt();
        Book alvo = buscarPorId(id);
        if (alvo == null) {
            System.out.println("⚠ Livro com ID " + id + " não encontrado.");
            return;
        }
        books.remove(alvo);
        Persistence.saveBooks(books);   // 👈
        System.out.println("✔ Livro \"" + alvo.getBookName() + "\" removido.");
    }

    // ---------- 4. REDUZIR QUANTIDADE ----------
    private static void reduzirQuantidade() {
        System.out.print("ID do livro: ");
        int id = lerInt();
        Book alvo = buscarPorId(id);
        if (alvo == null) { System.out.println("⚠ não encontrado."); return; }

        System.out.print("Quantidade a subtrair: ");
        int qtd = lerInt();
        if (qtd <= 0) { System.out.println("⚠ deve ser positiva."); return; }
        if (qtd > alvo.getQuantity()) { System.out.println("⚠ estoque insuficiente."); return; }

        alvo.setQuantity(alvo.getQuantity() - qtd);
        Persistence.saveBooks(books);
        System.out.println("✔ Novo estoque: " + alvo.getQuantity());
    }

    // ---------- helpers ----------
    private static Book buscarPorId(int id) {
        for (Book b : books) {
            if (b.getBookId() == id) return b;
        }
        return null;
    }

    private static int lerInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Valor inválido, digite um inteiro: ");
            }
        }
    }

    private static double lerDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.print("Valor inválido, digite um número: ");
            }
        }
    }

    // ---------- 5. BUSCAR POR NOME ----------
    private static void buscarPorNome() {
        System.out.print("Digite o nome (ou parte dele): ");
        String termo = sc.nextLine();

        List<Book> encontrados = buscar(Book::getBookName, termo);

        if (encontrados.isEmpty()) {
            System.out.println("📕 Book Absent");
            return;
        }
        System.out.println("✔ " + encontrados.size() + " resultado(s):");
        encontrados.forEach(System.out::println);
    }

    // ---------- 6. BUSCAR POR AUTOR ----------
    private static void buscarPorAutor() {
        System.out.print("Digite o nome do autor (ou parte): ");
        String termo = sc.nextLine();

        List<Book> encontrados = buscar(Book::getWriterName, termo);

        if (encontrados.isEmpty()) {
            System.out.println("✍ Writer Absent");
            return;
        }
        System.out.println("✔ " + encontrados.size() + " resultado(s):");
        encontrados.forEach(System.out::println);
    }

    // ---------- BUSCA GENÉRICA ----------
    /**
     * Busca por substring case-insensitive em qualquer campo String do Book.
     * @param campo  função que extrai o campo do Book (getBookName ou getWriterName)
     * @param termo  trecho digitado pelo usuário
     * @return lista de livros que contêm o termo no campo
     */
    private static List<Book> buscar(Function<Book, String> campo, String termo) {
        String termoNormalizado = termo.toLowerCase(Locale.ROOT);
        List<Book> encontrados = new ArrayList<>();

        for (Book b : books) {
            String valor = campo.apply(b);
            if (valor != null && valor.toLowerCase(Locale.ROOT).contains(termoNormalizado)) {
                encontrados.add(b);
            }
        }
        return encontrados;
    }

    // ---------- 7. CADASTRAR USUÁRIO ----------
    private static void cadastrarUsuario() {
        System.out.print("Senha: ");
        String senha = sc.nextLine();
        System.out.print("Role (ADMIN/USER): ");
        String roleStr = sc.nextLine().trim().toUpperCase(Locale.ROOT);
        Role role;
        try {
            role = Role.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            System.out.println("⚠ Role inválida.");
            return;
        }
        User u = new User(senha, role);
        users.add(u);
        Persistence.saveUsers(users);   // 👈
        System.out.println("✔ Usuário criado com ID " + u.getUserId());
    }


    // ---------- 8. LISTAR USUÁRIOS ----------
    private static void listarUsuarios() {
        if (users.isEmpty()) {
            System.out.println("⚠ Nenhum usuário cadastrado.");
            return;
        }
        users.forEach(System.out::println);
    }

    /// ---------- 9. EMITIR LIVRO ----------
    private static void emitirLivro() {
        User admin = login();
        if (admin == null || admin.getRole() != Role.ADMIN) {
            System.out.println("⚠ Acesso negado.");
            return;
        }
        System.out.println("✔ Bem-vindo, admin #" + admin.getUserId());

        System.out.print("ID do livro: ");
        int bookId = lerInt();
        Book livro = buscarPorId(bookId);
        if (livro == null) { System.out.println("⚠ livro não encontrado."); return; }

        System.out.print("ID do usuário destinatário: ");
        int userId = lerInt();
        User leitor = buscarUsuarioPorId(userId);
        if (leitor == null) { System.out.println("⚠ usuário não encontrado."); return; }
        if (leitor.getRole() == Role.ADMIN) {
            System.out.println("⚠ Emita para um USER."); return;
        }

        for (IssueBook ib : issues) {
            if (ib.getBookId() == bookId) {
                System.out.println("⚠ já emitido para userId=" + ib.getUserId()); return;
            }
        }
        if (livro.getQuantity() <= 0) {
            System.out.println("⚠ estoque zerado."); return;
        }

        livro.setQuantity(livro.getQuantity() - 1);
        issues.add(new IssueBook(bookId, userId));

        // Persiste AMBOS: livro (estoque) e issue (novo empréstimo)
        Persistence.saveBooks(books);      // 👈
        Persistence.saveIssues(issues);    // 👈

        System.out.println("✔ Livro \"" + livro.getBookName() +
                "\" emitido para userId=" + userId +
                " | Estoque restante: " + livro.getQuantity());
    }

    // ---------- 10. LISTAR EMPRÉSTIMOS ----------
    private static void listarEmprestimos() {
        if (issues.isEmpty()) {
            System.out.println("⚠ Nenhum empréstimo ativo.");
            return;
        }
        issues.forEach(System.out::println);
    }

    // ---------- LOGIN ----------
    private static User login() {
        System.out.print("userId: ");
        int id = lerInt();
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        User u = buscarUsuarioPorId(id);
        if (u == null || !u.getPassword().equals(senha)) {
            System.out.println("⚠ Credenciais inválidas.");
            return null;
        }
        return u;
    }

    // ---------- helpers ----------
    private static User buscarUsuarioPorId(int id) {
        for (User u : users) {
            if (u.getUserId() == id) return u;
        }
        return null;
    }
}