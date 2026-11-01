package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static List<Book> books = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int option;
        do {
            System.out.println("\n=== MENU ===");
            System.out.println("1 - Adicionar livro");
            System.out.println("2 - Listar livros");
            System.out.println("3 - Deletar livro");
            System.out.println("4 - Reduzir quantidade");
            System.out.println("5 - Sair");
            System.out.print("Escolha: ");

            option = lerInt();

            switch (option) {
                case 1 -> adicionarLivro();
                case 2 -> listarLivros();
                case 3 -> deletarLivro();
                case 4 -> reduzirQuantidade();
                case 5 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }
        } while (option != 5);

        sc.close();
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
        System.out.println("✔ Livro \"" + alvo.getBookName() + "\" removido.");
    }

    // ---------- 4. REDUZIR QUANTIDADE ----------
    private static void reduzirQuantidade() {
        System.out.print("ID do livro: ");
        int id = lerInt();

        Book alvo = buscarPorId(id);
        if (alvo == null) {
            System.out.println("⚠ Livro com ID " + id + " não encontrado.");
            return;
        }

        System.out.print("Quantidade a subtrair: ");
        int qtd = lerInt();

        if (qtd <= 0) {
            System.out.println("⚠ A quantidade a subtrair deve ser positiva.");
            return;
        }
        if (qtd > alvo.getQuantity()) {
            System.out.println("⚠ Operação inválida: quantidade a subtrair (" + qtd +
                    ") é maior que o estoque (" + alvo.getQuantity() + ").");
            return;
        }

        alvo.setQuantity(alvo.getQuantity() - qtd);
        System.out.println("✔ Novo estoque de \"" + alvo.getBookName() +
                "\": " + alvo.getQuantity());
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
}