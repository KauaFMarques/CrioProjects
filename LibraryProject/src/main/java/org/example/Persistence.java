package org.example;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Persistence {

    // ⚠️ Em produção, use variável de ambiente / keystore.
    // Aqui usamos chave fixa de 16 bytes (AES-128) para fins didáticos.
    private static final String SECRET = "MySecretKey12345"; // 16 chars
    private static final String ALGORITHM = "AES";

    private static final String USER_FILE  = "userDetails.txt";
    private static final String BOOK_FILE  = "bookDetails.txt";
    private static final String ISSUE_FILE = "issueBook.txt";

    // ---------- AES ----------
    private static SecretKeySpec key() {
        byte[] k = SECRET.getBytes();
        byte[] key16 = new byte[16];
        System.arraycopy(k, 0, key16, 0, Math.min(k.length, 16));
        return new SecretKeySpec(key16, ALGORITHM);
    }

    private static byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key());
        return cipher.doFinal(data);
    }

    private static byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key());
        return cipher.doFinal(data);
    }

    // ---------- serialização ----------
    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
        }
        return bos.toByteArray();
    }

    @SuppressWarnings("unchecked")
    private static <T> T deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (T) ois.readObject();
        }
    }

    // ---------- persistência genérica ----------
    private static void save(String file, Object data) {
        try {
            byte[] raw = serialize(data);
            byte[] enc = encrypt(raw);
            Files.write(Path.of(file), enc);
        } catch (Exception e) {
            System.err.println("⚠ Erro ao salvar " + file + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T load(String file, T fallback) {
        Path p = Path.of(file);
        if (!Files.exists(p)) return fallback;
        try {
            byte[] enc = Files.readAllBytes(p);
            byte[] raw = decrypt(enc);
            return (T) deserialize(raw);
        } catch (Exception e) {
            System.err.println("⚠ Erro ao carregar " + file + ": " + e.getMessage());
            return fallback;
        }
    }

    // ---------- APIs específicas ----------
    public static void saveBooks(List<Book> books)      { save(BOOK_FILE,  books); }
    public static void saveUsers(List<User> users)      { save(USER_FILE,  users); }
    public static void saveIssues(List<IssueBook> issues){ save(ISSUE_FILE, issues); }

    @SuppressWarnings("unchecked")
    public static List<Book> loadBooks() {
        return load(BOOK_FILE, new java.util.ArrayList<Book>());
    }

    @SuppressWarnings("unchecked")
    public static List<User> loadUsers() {
        return load(USER_FILE, new java.util.ArrayList<User>());
    }

    @SuppressWarnings("unchecked")
    public static List<IssueBook> loadIssues() {
        return load(ISSUE_FILE, new java.util.ArrayList<IssueBook>());
    }
}