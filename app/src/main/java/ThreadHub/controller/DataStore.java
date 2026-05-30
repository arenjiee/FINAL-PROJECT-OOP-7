package ThreadHub.controller;

import ThreadHub.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {

    private static DataStore instance;

    private final List<User>         users         = new ArrayList<>();
    private final List<Produk>       produkList    = new ArrayList<>();
    private final List<Transaksi>    transaksiList = new ArrayList<>();
    private final List<OutfitBundle> outfitList    = new ArrayList<>(); // TAMBAHAN

    private static final String FILE_PRODUK = "data_produk.dat";
    private static final String FILE_TRANSAKSI = "data_transaksi.dat";
    private static final String FILE_USER = "data_user.dat";
    private static final String FILE_OUTFIT = "data_outfit.dat"; // TAMBAHAN

    private DataStore() {
        muatDataUser();      
        muatDataProduk(); 
        muatDataTransaksi(); 
        muatDataOutfit(); // TAMBAHAN
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    // ─── User ────────────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private void muatDataUser() {
        File file = new File(FILE_USER);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                List<User> dataTersimpan = (List<User>) ois.readObject();
                users.addAll(dataTersimpan);
            } catch (Exception e) {
                initUsers();
                simpanDataUser();
            }
        } else {
            initUsers();
            simpanDataUser();
        }
    }

    private void initUsers() {
        users.add(new Admin(1, "admin", "admin123", "Administrator"));
        users.add(new Buyer(2, "aren",  "arenjiee123",  "Arenjiee"));
        users.add(new Buyer(3, "rifat",  "rifat123",  "patkasmang"));
    }

    public User login(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.cekPassword(password))
                .findFirst()
                .orElse(null);
    }

    public List<User> getAllUsers() { return users; }

    public void simpanDataUser() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_USER))) {
            oos.writeObject(users);
        } catch (Exception e) {
            System.out.println("Gagal menyimpan data user: " + e.getMessage());
        }
    }

    public void tambahUser(User u) {
        users.add(u);
        simpanDataUser();
    }

    public void hapusUser(int id) {
        users.removeIf(u -> u.getId() == id);
        simpanDataUser();
    }

    public int generateUserId() {
        return users.stream().mapToInt(User::getId).max().orElse(0) + 1;
    }
}