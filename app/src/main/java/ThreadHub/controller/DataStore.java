package ThreadHub.controller;

import ThreadHub.model.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DataStore adalah kelas singleton yang menyimpan semua data aplikasi di memori.
 * Berfungsi sebagai "database" in-memory untuk ThreadHub.
 */
public class DataStore {

    private static DataStore instance;

    private final List<User>      users       = new ArrayList<>();
    private final List<Produk>    produkList  = new ArrayList<>();
    private final List<Transaksi> transaksiList = new ArrayList<>();

    private DataStore() {
        initSampleData();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    // ─── Seed Data ────────────────────────────────────────────────────────────

    private void initSampleData() {
        // Akun default
        users.add(new Admin(1, "admin", "admin123", "Administrator"));
        users.add(new Buyer(2, "aren",  "arenjiee123",  "Arenjiee"));
        users.add(new Buyer(3, "rifat",  "rifat123",  "patkasmang"));

        // Produk sample (Sudah ditambahkan Gender di parameter terakhir)
        produkList.add(new Produk(1, "Kaos Polos Premium",
                "Kaos bahan cotton combed 30s, lembut dan adem.",
                "Kaos", 89_000, 50, "M", "Putih", "PRIA"));
                
        produkList.add(new Produk(2, "Kemeja Flanel Kotak",
                "Kemeja flanel pola kotak klasik, cocok untuk casual.",
                "Kemeja", 175_000, 30, "L", "Merah-Hitam", "PRIA"));
                
        produkList.add(new Produk(3, "Blouse Floral Elegan",
                "Blouse berbahan jatuh dengan motif bunga yang cantik.",
                "Kemeja", 150_000, 25, "M", "Navy", "WANITA"));
                
        produkList.add(new Produk(4, "Rok Midi Plisket",
                "Rok plisket premium untuk gaya kasual dan formal.",
                "Rok", 120_000, 15, "All Size", "Hitam", "WANITA"));
                
        produkList.add(new Produk(5, "Kaos Karakter Superhero",
                "Kaos anak bahan katun dengan sablon karakter lucu.",
                "Kaos", 65_000, 20, "S", "Biru", "ANAK-ANAK"));
                
        produkList.add(new Produk(6, "Polo Shirt Pique",
                "Polo shirt bahan pique premium, formal casual.",
                "Kaos Polo", 145_000, 40, "M", "Biru Dongker", "PRIA"));
                
        produkList.add(new Produk(7, "Sepatu Sneakers Canvas",
                "Sepatu sneakers kasual ringan dan nyaman.",
                "Sepatu", 285_000, 18, "40", "Putih", "WANITA"));
                
        produkList.add(new Produk(8, "Topi Baseball Anak",
                "Topi pelindung panas dengan desain menarik.",
                "Topi", 45_000, 22, "All Size", "Merah", "ANAK-ANAK"));
                
        produkList.add(new Produk(9, "Gaun Pesta Malam",
                "Gaun anggun untuk acara formal malam hari.",
                "Gaun", 450_000, 10, "L", "Merah Maroon", "WANITA"));
                
        produkList.add(new Produk(10, "Celana Chino Slim",
                "Celana chino slim fit berbahan katun stretch.",
                "Celana", 220_000, 25, "32", "Cream", "PRIA"));
    }

    // ─── User ────────────────────────────────────────────────────────────────

    public User login(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.cekPassword(password))
                .findFirst()
                .orElse(null);
    }

    public List<User> getAllUsers() { return users; }

    // ─── Produk ──────────────────────────────────────────────────────────────

    public List<Produk> getAllProduk() { return produkList; }

    public List<Produk> cariProduk(String keyword) {
        String kw = keyword.toLowerCase();
        return produkList.stream()
                .filter(p -> p.getNama().toLowerCase().contains(kw)
                          || p.getKategori().toLowerCase().contains(kw)
                          || p.getWarna().toLowerCase().contains(kw)
                          || p.getGender().toLowerCase().contains(kw)) // Tambahan pencarian gender
                .toList();
    }

    public Produk getProdukById(int id) {
        return produkList.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void tambahProduk(Produk p) {
        produkList.add(p);
    }

    public void hapusProduk(int id) {
        produkList.removeIf(p -> p.getId() == id);
    }

    public int generateProdukId() {
        return produkList.stream().mapToInt(Produk::getId).max().orElse(0) + 1;
    }

    // ─── Transaksi ───────────────────────────────────────────────────────────

    public void tambahTransaksi(Transaksi t) {
        transaksiList.add(t);
    }

    public List<Transaksi> getTransaksiBuyer(Buyer buyer) {
        return transaksiList.stream()
                .filter(t -> t.getBuyer().getId() == buyer.getId())
                .toList();
    }

    public List<Transaksi> getAllTransaksi() { return transaksiList; }
}