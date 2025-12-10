import java.util.Scanner;

// ===================== ITEM DALAM TRANSAKSI =====================
class ItemTransaksi {
    String namaProduk;
    int harga;
    int jumlah;

    public ItemTransaksi(String namaProduk, int harga, int jumlah) {
        this.namaProduk = namaProduk;
        this.harga = harga;
        this.jumlah = jumlah;
    }
}

// ===================== MODEL PRODUK TOKO =====================
class ModelProduct {
    private String namaProduk;
    private int stok;
    private int harga;

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }
    public String getNamaProduk() {
        return namaProduk;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }
    public int getStok() {
        return stok;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }
    public int getHarga() {
        return harga;
    }
}

// ===================== MODEL TRANSAKSI =====================
class ModelTransaksi {
    ItemTransaksi[] items = new ItemTransaksi[50];
    int itemCount = 0;
    int total = 0;

    public void addItem(ItemTransaksi item){
        items[itemCount++] = item;
        total += item.harga * item.jumlah;
    }
}

// ===================== APLIKASI TOKO =====================
public class ManageToko {

    Scanner sc = new Scanner(System.in);

    ModelProduct[] products = new ModelProduct[100];
    int productIndex = 0;

    ModelTransaksi[] history = new ModelTransaksi[100];
    int historyIndex = 0;

    public static void main(String[] args) {
        ManageToko app = new ManageToko();
        app.runThis();
    }

    void runThis(){
        loginUser();  // wajib login dulu

        int pilih = 0;
        while (true) {
            pilih = menuApps();

            switch (pilih) {
                case 1: addProduct(); break;
                case 2: showProduct(); break;
                case 3: transaksi(); break;
                case 4: showHistory(); break;
                case 5: addStok(); break;
                case 6: stopApp(); return;
                case 7: logout(); break;
                default: System.out.println("Pilihan tidak valid!");
            }
        }
    }

    // =============== LOGIN SYSTEM =================
    void loginUser(){
        String usernameBenar = "admin";
        String passwordBenar = "123";

        int attempts = 0;
        boolean loginBerhasil = false;

        System.out.println("\n=== LOGIN TOKO ===");

        while (attempts < 3 && !loginBerhasil) {
            System.out.print("Username : ");
            String user = sc.nextLine();

            System.out.print("Password : ");
            String pass = sc.nextLine();

            if (user.equals(usernameBenar) && pass.equals(passwordBenar)) {
                System.out.println("Login berhasil!\n");
                loginBerhasil = true;
            } else {
                System.out.println("Username atau Password salah!\n");
                attempts++;
            }
        }

        if (!loginBerhasil) {
            System.out.println("Anda salah 3x! Aplikasi dihentikan.");
            System.exit(0);
        }
    }

    // ===================== MENU =====================
    int menuApps(){
        System.out.println("\n=== MENU TOKO ===");
        System.out.println("1. Add Product");
        System.out.println("2. Show Product");
        System.out.println("3. Transaksi");
        System.out.println("4. History Transaksi");
        System.out.println("5. Add Stok");
        System.out.println("6. Stop");
        System.out.println("7. Logout");
        System.out.print("Pilih menu : ");
        return sc.nextInt();
    }

    // ===================== ADD PRODUCT =====================
    void addProduct() {
        sc.nextLine();
        ModelProduct p = new ModelProduct();

        System.out.println("\n=== INPUT PRODUK ===");
        System.out.print("Nama produk : ");
        p.setNamaProduk(sc.nextLine());

        System.out.print("Harga produk : ");
        p.setHarga(sc.nextInt());

        System.out.print("Stok produk : ");
        p.setStok(sc.nextInt());

        products[productIndex++] = p;

        System.out.println("Produk berhasil ditambahkan!");
    }

    // ===================== SHOW PRODUCTS =====================
    void showProduct(){
        System.out.println("\n=== DAFTAR PRODUK ===");

        if (productIndex == 0) {
            System.out.println("Belum ada produk!");
            return;
        }

        for (int i = 0; i < productIndex; i++) {
            System.out.println((i+1) + ". " + products[i].getNamaProduk()
                    + " | Harga: " + products[i].getHarga()
                    + " | Stok: " + products[i].getStok());
        }
    }

    // ===================== TRANSAKSI =====================
    void transaksi(){

        if (productIndex == 0) {
            System.out.println("Belum ada produk!");
            return;
        }

        ModelTransaksi tr = new ModelTransaksi();

        System.out.print("\nAda berapa jenis barang yang dibeli? : ");
        int jenis = sc.nextInt();

        for (int i = 0; i < jenis; i++) {

            System.out.println("\nPilih produk ke-" + (i+1));
            showProduct();

            System.out.print("Masukkan nomor produk : ");
            int pilih = sc.nextInt() - 1;

            if (pilih < 0 || pilih >= productIndex) {
                System.out.println("Produk tidak valid, ulangi!");
                i--;
                continue;
            }

            ModelProduct p = products[pilih];

            System.out.print("Jumlah beli : ");
            int jumlah = sc.nextInt();

            // VALIDASI STOK
            if (jumlah > p.getStok()) {
                System.out.println("Stok tidak cukup! Stok tersedia: " + p.getStok());
                i--;
                continue;
            }

            // Kurangi stok
            p.setStok(p.getStok() - jumlah);

            // Tambahkan item ke transaksi
            tr.addItem(new ItemTransaksi(p.getNamaProduk(), p.getHarga(), jumlah));
        }

        printStruk(tr);
        history[historyIndex++] = tr;  // simpan ke history
    }

    // ===================== CETAK STRUK =====================
    void printStruk(ModelTransaksi tr){
        System.out.println("\n=========== STRUK BELANJA ===========");

        for (int i = 0; i < tr.itemCount; i++) {
            ItemTransaksi it = tr.items[i];
            int subtotal = it.harga * it.jumlah;

            System.out.println(it.namaProduk + " x" + it.jumlah +
                    " @ " + it.harga + " = " + subtotal);
        }

        System.out.println("-------------------------------------");
        System.out.println("TOTAL BAYAR : " + tr.total);
        System.out.println("=====================================");
    }

    // ===================== SHOW HISTORY =====================
    void showHistory(){
        System.out.println("\n=== HISTORY TRANSAKSI ===");

        if (historyIndex == 0) {
            System.out.println("Belum ada transaksi!");
            return;
        }

        for (int i = 0; i < historyIndex; i++) {
            ModelTransaksi tr = history[i];

            System.out.println("\nTransaksi ke-" + (i+1));
            for (int j = 0; j < tr.itemCount; j++) {
                ItemTransaksi it = tr.items[j];
                System.out.println("- " + it.namaProduk + " x" + it.jumlah + " @ " + it.harga);
            }
            System.out.println("Total : " + tr.total);
        }
    }

    // ===================== FITUR ADD STOK =====================
    void addStok(){
        if (productIndex == 0) {
            System.out.println("Belum ada produk!");
            return;
        }

        System.out.println("\n=== ADD STOK PRODUK ===");
        showProduct();

        System.out.print("Pilih nomor produk : ");
        int pilih = sc.nextInt() - 1;

        if (pilih < 0 || pilih >= productIndex) {
            System.out.println("Produk tidak valid!");
            return;
        }

        System.out.print("Tambah stok sebanyak : ");
        int tambah = sc.nextInt();

        int stokBaru = products[pilih].getStok() + tambah;
        products[pilih].setStok(stokBaru);

        System.out.println("Stok berhasil ditambah! Stok sekarang: " + stokBaru);
    }

    // ===================== FITUR LOGOUT =====================
    void logout(){
        sc.nextLine(); // bersihkan buffer
        System.out.println("\n=== LOGOUT BERHASIL ===");
        loginUser();   // kembali ke login
    }

    // ===================== STOP APPS =====================
    void stopApp(){
        System.out.println("\nAplikasi dihentikan...");
    }
}
