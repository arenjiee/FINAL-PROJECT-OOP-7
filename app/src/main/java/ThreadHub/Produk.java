package ThreadHub;

public abstract class Produk {
    private String idBarang;
    private String jenis; 
    private double hargaDasar;
    private int stok;
    private String imagePath;

    public Produk(String idBarang, String jenis, double hargaDasar, int stok, String imagePath) {
        this.idBarang = idBarang;
        this.jenis = jenis;
        this.hargaDasar = hargaDasar;
        this.stok = stok;
        this.imagePath = imagePath;
    }

    public String getIdBarang() { return idBarang; }
    public String getJenis() { return jenis; }
    public double getHargaDasar() { return hargaDasar; }
    public int getStok() { return stok; }
    public String getImagePath() { return imagePath; }
    
    public void kurangiStok(int jumlah) {
        if(this.stok >= jumlah) this.stok -= jumlah;
    }

    public double getHargaAkhir() {
        return hitungHargaAkhir();
    }

    public abstract double hitungHargaAkhir();
    public abstract String tampilkanInfo();
}