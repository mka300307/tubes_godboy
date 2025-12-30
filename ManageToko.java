import java.util.Scanner;
import java.util.ArrayList;

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

// ===================== MODEL AdminKasir =====================
class AdminKasir {
    private String nama;
    private String pass;
    private int role; // 1 = Super Admin, 2 = Admin/Kasir

    public AdminKasir(String nama, String pass, int role) {
        this.nama = nama;
        this.pass = pass;
        this.role = role;
    }

    public String getNama() {
        return nama;
    }

    public String getPass() {
        return pass;
    }

    public int getRole() {
        return role;
    }
}

// ===================== MODEL PRODUK =====================
class ModelProduct {
    private String namaProduk;
    private int stok;
    private int harga;

    public ModelProduct(String namaProduk, int stok, int harga){
        this.namaProduk = namaProduk;
        this.stok = stok;
        this.harga = harga;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }
}

// ===================== MODEL TRANSAKSI =====================
class ModelTransaksi {
    ItemTransaksi[] items = new ItemTransaksi[50];
    int itemCount = 0;
    int total = 0;
    String namaAdmin;

    public ModelTransaksi(String namaAdmin) {
        this.namaAdmin = namaAdmin;
    }

    public void addItem(ItemTransaksi item) {
        items[itemCount++] = item;
        total += item.harga * item.jumlah;
    }
}

// ===================== APLIKASI TOKO =====================
public class ManageToko {

    Scanner sc = new Scanner(System.in);

    ArrayList<AdminKasir> adminKasir = new ArrayList<>();
    AdminKasir currentUser;

    ArrayList<ModelProduct> products = new ArrayList<>();

    ModelTransaksi[] history = new ModelTransaksi[100];
    int historyIndex = 0;

    public static void main(String[] args) {
        new ManageToko().runThis();
    }

    void runThis() {
        adminKasir.add(new AdminKasir("admin", "123", 1));
        adminKasir.add(new AdminKasir("ical", "111", 2));
        products.add(new ModelProduct("Indomie", 6, 3500));
        products.add(new ModelProduct("Kecap", 10, 10000));


        loginUser();

        while (true) {
            int pilih = menuApps();

            if (currentUser.getRole() == 1) {
                switch (pilih) {
                    case 1 -> buatAdmin();
                    case 2 -> addProduct();
                    case 3 -> showProduct();
                    case 4 -> addStok();
                    case 5 -> transaksi();
                    case 6 -> showHistory();
                    case 7 -> stopApp();
                    case 8 -> logout();
                }
            } else {
                switch (pilih) {
                    case 1 -> showProduct();
                    case 2 -> addStok();
                    case 3 -> transaksi();
                    case 4 -> showHistory();
                    case 5 -> stopApp();
                    case 6 -> logout();
                }
            }
        }
    }

    // ===================== LOGIN =====================
    void loginUser() {
        sc.nextLine();

        while (true) {
            System.out.print("Username : ");
            String user = sc.nextLine();

            System.out.print("Password : ");
            String pass = sc.nextLine();

            for (AdminKasir a : adminKasir) {
                if (user.equals(a.getNama()) && pass.equals(a.getPass())) {
                    currentUser = a;
                    System.out.println("Login sebagai "
                            + (a.getRole() == 1 ? "Super Admin" : "Admin/Kasir"));
                    return;
                }
            }
            System.out.println("Login gagal!");
        }
    }

    // ===================== MENU =====================
    int menuApps() {
        System.out.println("\n=== MENU TOKO ===");
        if (currentUser.getRole() == 1) {
            System.out.println("1. Buat Admin");
            System.out.println("2. Add Product");
            System.out.println("3. Show Product");
            System.out.println("4. Add Stok");
            System.out.println("5. Transaksi");
            System.out.println("6. History");
            System.out.println("7. Stop");
            System.out.println("8. Logout");
        } else {
            System.out.println("1. Show Product");
            System.out.println("2. Add Stok");
            System.out.println("3. Transaksi");
            System.out.println("4. History");
            System.out.println("5. Stop");
            System.out.println("6. Logout");
        }
        System.out.print("Pilih : ");
        return sc.nextInt();
    }

    // ===================== BUAT ADMIN =====================
    void buatAdmin() {
    sc.nextLine();
    System.out.print("Username : ");
    String nama = sc.nextLine();

    for (AdminKasir a : adminKasir) {
        if (a.getNama().equalsIgnoreCase(nama)) {
            System.out.println("Nama sudah terdaftar!");
            return; // HENTIKAN METHOD
        }
    }

    System.out.print("Password : ");
    String pass = sc.nextLine();

    System.out.println("Pilih Role (1/2): ");
    System.out.println("1. Super Admin");
    System.out.println("2. Admin");

    int role = sc.nextInt();

    adminKasir.add(new AdminKasir(nama, pass, role));
    System.out.println("Admin berhasil dibuat!");
}



    // ===================== PRODUK =====================
    void addProduct() {
        sc.nextLine();

        System.out.print("Nama produk : ");
        String namaProduk = sc.nextLine();

        System.out.print("Harga : ");
        int hargaProduk = sc.nextInt();

        System.out.print("Stok : ");
        int stok = sc.nextInt();

        products.add(new ModelProduct(namaProduk, hargaProduk,stok));
        System.out.println("Produk berhasil ditambahkan!");
    }

    void showProduct() {
        if (products.isEmpty()) {
            System.out.println("Belum ada produk.");
            return;
        }

        for (int i = 0; i < products.size(); i++) {
            ModelProduct p = products.get(i);
            System.out.println((i + 1) + ". " + p.getNamaProduk()
                    + " | Harga: " + p.getHarga()
                    + " | Stok: " + p.getStok());
        }
    }

    void addStok() {
        showProduct();
        System.out.print("Pilih produk : ");
        int idx = sc.nextInt() - 1;

        System.out.print("Tambah stok : ");
        int tambah = sc.nextInt();

        ModelProduct p = products.get(idx);
        p.setStok(p.getStok() + tambah);

        System.out.println("Stok berhasil ditambah!");
    }

    // ===================== TRANSAKSI =====================
    void transaksi() {
        ModelTransaksi tr = new ModelTransaksi(currentUser.getNama());

        System.out.print("Jumlah jenis barang : ");
        int jenis = sc.nextInt();

        for (int i = 0; i < jenis; i++) {
            showProduct();
            System.out.print("Pilih produk : ");
            int idx = sc.nextInt() - 1;

            System.out.print("Jumlah : ");
            int jumlah = sc.nextInt();

            ModelProduct p = products.get(idx);
            p.setStok(p.getStok() - jumlah);

            tr.addItem(new ItemTransaksi(
                    p.getNamaProduk(),
                    p.getHarga(),
                    jumlah));
        }

        history[historyIndex++] = tr;
        printStruk(tr);
    }

    void printStruk(ModelTransaksi tr) {
        System.out.println("\n=== STRUK BELANJA ===");
        for (int i = 0; i < tr.itemCount; i++) {
            ItemTransaksi it = tr.items[i];
            System.out.println(it.namaProduk + " x" + it.jumlah);
        }
        System.out.println("TOTAL : " + tr.total);
    }

    // ===================== HISTORY =====================
    void showHistory() {
        boolean ada = false;
        System.out.println("\n=== HISTORY TRANSAKSI ===");

        for (int i = 0; i < historyIndex; i++) {
            ModelTransaksi tr = history[i];

            if (currentUser.getRole() == 1 ||
                tr.namaAdmin.equals(currentUser.getNama())) {

                System.out.println("Transaksi ke-" + (i + 1)
                        + " | Admin: " + tr.namaAdmin
                        + " | Total: " + tr.total);
                ada = true;
            }
        }

        if (!ada) {
            System.out.println("Tidak ada transaksi.");
        }
    }

    // ===================== LOGOUT & STOP =====================
    void logout() {
        loginUser();
    }

    void stopApp() {
        System.out.println("Aplikasi dihentikan.");
        System.exit(0);
    }
}
