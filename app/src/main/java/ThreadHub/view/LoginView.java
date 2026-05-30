package ThreadHub.view;

import ThreadHub.controller.DataStore;
import ThreadHub.model.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class LoginView {

    private final Stage stage;
    private Label errorLabel;

    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public HBox buildLayout() {
        // branding panel 
        VBox brandPanel = new VBox(12);
        brandPanel.setAlignment(Pos.CENTER);
        brandPanel.setPrefWidth(400);
        brandPanel.setStyle("-fx-background-color: " + StyleKit.ACCENT + ";");
        brandPanel.setPadding(new Insets(60));

        // --- BAGIAN LOGO ---
        ImageView logoView = new ImageView();
        try {
            // Pastikan gambar dengan nama "logo_threadhub.png" ada di dalam folder src/main/resources
            Image img = new Image(getClass().getResourceAsStream("/logo_threadhub.png"));
            logoView.setImage(img);
            logoView.setFitWidth(250);
            logoView.setPreserveRatio(true);
            logoView.setSmooth(true);
        } catch (Exception e) {
            System.out.println("Gagal memuat gambar logo: " + e.getMessage());
        }

        Label appName = new Label("ThreadHub");
        appName.setFont(Font.font("Times New Roman", FontWeight.BOLD, 48)); 
        appName.setTextFill(Color.WHITE);
        VBox.setMargin(appName, new Insets(-25, 0, 0, 0)); 

        Label tagline = new Label("Temukan gaya terbaikmu.\nBelanja pakaian kualitas premium.");
        tagline.setFont(Font.font(StyleKit.FONT_FAMILY, 15));
        tagline.setTextFill(Color.rgb(255, 255, 255, 0.85));
        tagline.setTextAlignment(TextAlignment.CENTER);
        tagline.setWrapText(true);

        // Memasukkan logoView dan appName ke dalam panel
        brandPanel.getChildren().addAll(logoView, appName, tagline);

        // form login panel Utama
        VBox formPanel = new VBox(25);
        formPanel.setAlignment(Pos.CENTER);
        formPanel.setPadding(new Insets(60, 70, 60, 70));
        formPanel.setStyle("-fx-background-color: " + StyleKit.DARK_BG + ";");
        formPanel.setPrefWidth(440);

        Label welcomeLabel = StyleKit.titleLabel("Selamat Datang!", 26);
        Label subLabel     = StyleKit.mutedLabel("Masuk untuk melanjutkan ke ThreadHub");
        subLabel.setFont(Font.font(StyleKit.FONT_FAMILY, 14));

        // Wadah Input pembatas lebar maksimal komponen agar proporsional saat fullscreen
        VBox inputContainer = new VBox(14);
        inputContainer.setMaxWidth(320); 
        inputContainer.setAlignment(Pos.CENTER_LEFT);

        // Username field
        Label usernameLabel = new Label("Username");
        usernameLabel.setTextFill(Color.web(StyleKit.TEXT_MUTED));
        usernameLabel.setFont(Font.font(StyleKit.FONT_FAMILY, 13));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Masukkan username");
        styleTextField(usernameField);

        // Password field
        Label passwordLabel = new Label("Password");
        passwordLabel.setTextFill(Color.web(StyleKit.TEXT_MUTED));
        passwordLabel.setFont(Font.font(StyleKit.FONT_FAMILY, 13));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Masukkan password");
        styleTextField(passwordField);

        // Error label
        errorLabel = new Label("");
        errorLabel.setTextFill(Color.web(StyleKit.ACCENT));
        errorLabel.setFont(Font.font(StyleKit.FONT_FAMILY, 13));

        // Tombol login
        Button loginBtn = StyleKit.primaryButton("Masuk");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle(loginBtn.getStyle() + "-fx-font-size: 15px; -fx-padding: 12 0;");

        loginBtn.setOnAction(e -> handleLogin(
                usernameField.getText().trim(),
                passwordField.getText()
        ));

        passwordField.setOnAction(e -> handleLogin(
                usernameField.getText().trim(),
                passwordField.getText()
        ));

        // Tautan Daftar Akun
        HBox registerBox = new HBox(5);
        registerBox.setAlignment(Pos.CENTER);
        Label lblBawah = new Label("Belum punya akun?");
        lblBawah.setTextFill(Color.web(StyleKit.TEXT_MUTED));
        lblBawah.setFont(Font.font(StyleKit.FONT_FAMILY, 13));

        Hyperlink linkDaftar = new Hyperlink("Daftar di sini");
        linkDaftar.setTextFill(Color.web(StyleKit.ACCENT));
        linkDaftar.setFont(Font.font(StyleKit.FONT_FAMILY, FontWeight.BOLD, 13));
        linkDaftar.setBorder(Border.EMPTY);
        linkDaftar.setOnAction(e -> showRegisterDialog());
        registerBox.getChildren().addAll(lblBawah, linkDaftar);
        VBox.setMargin(registerBox, new Insets(5, 0, 0, 0));

        inputContainer.getChildren().addAll(
                usernameLabel, usernameField,
                passwordLabel, passwordField,
                errorLabel,
                loginBtn,
                registerBox
        );

        formPanel.getChildren().addAll(welcomeLabel, subLabel, inputContainer);

        HBox root = new HBox(brandPanel, formPanel);
        HBox.setHgrow(formPanel, Priority.ALWAYS);

        return root;
    }

    // Menjaga kompatibilitas jika dipanggil dari file Main eksternal
    public Scene buildScene() {
        return new Scene(buildLayout(), 840, 560);
    }

    private void handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("⚠  Username dan password tidak boleh kosong.");
            return;
        }
        User user = DataStore.getInstance().login(username, password);
        if (user == null) {
            errorLabel.setText("⚠  Username atau password salah.");
            return;
        }
        errorLabel.setText("");
        if ("admin".equals(user.getRole())) {
            new app.admin.AdminDashboardView(stage, (app.model.Admin) user).show();
        } else {
            new app.buyer.BuyerDashboardView(stage, (app.model.Buyer) user).show();
        }
    }

    private void showRegisterDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Daftar Akun Baru");
        dialog.initOwner(stage);

        VBox form = new VBox(14);
        form.setPadding(new Insets(30));
        form.setPrefWidth(350);
        form.setStyle("-fx-background-color: " + StyleKit.DARK_BG + ";");

        Label title = StyleKit.titleLabel("Buat Akun Pembeli", 20);

        TextField tfNama = new TextField();
        tfNama.setPromptText("Nama Lengkap (Contoh: Budi Santoso)");
        styleTextField(tfNama);

        TextField tfUsername = new TextField();
        tfUsername.setPromptText("Username (tanpa spasi)");
        styleTextField(tfUsername);

        PasswordField pfPassword = new PasswordField();
        pfPassword.setPromptText("Password");
        styleTextField(pfPassword);

        Label errLabel = new Label("");
        errLabel.setTextFill(Color.web(StyleKit.ACCENT));
        errLabel.setFont(Font.font(StyleKit.FONT_FAMILY, 12));

        Button btnDaftar = StyleKit.primaryButton("Daftar Sekarang");
        btnDaftar.setMaxWidth(Double.MAX_VALUE);
        btnDaftar.setOnAction(e -> {
            String nama = tfNama.getText().trim();
            String username = tfUsername.getText().trim();
            String password = pfPassword.getText().trim();

            if (nama.isEmpty() || username.isEmpty() || password.isEmpty()) {
                errLabel.setText("Semua kolom wajib diisi.");
                return;
            }

            if (username.contains(" ")) {
                errLabel.setText("Username tidak boleh mengandung spasi.");
                return;
            }

            DataStore ds = DataStore.getInstance();
            boolean exists = ds.getAllUsers().stream()
                    .anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
            if (exists) {
                errLabel.setText("Username sudah terpakai, pilih yang lain.");
                return;
            }

            int newId = ds.generateUserId();
            Buyer pembeliBaru = new Buyer(newId, username, password, nama);
            ds.tambahUser(pembeliBaru);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pendaftaran Berhasil");
            alert.setHeaderText(null);
            alert.setContentText("Akun berhasil dibuat! Silakan login menggunakan username dan password Anda.");
            alert.showAndWait();
            
            dialog.close();
        });

        form.getChildren().addAll(
                title, StyleKit.hSeparator(),
                new Label("Nama Lengkap") {{ setTextFill(Color.web(StyleKit.TEXT_MUTED)); setFont(Font.font(12)); }},
                tfNama,
                new Label("Username") {{ setTextFill(Color.web(StyleKit.TEXT_MUTED)); setFont(Font.font(12)); }},
                tfUsername,
                new Label("Password") {{ setTextFill(Color.web(StyleKit.TEXT_MUTED)); setFont(Font.font(12)); }},
                pfPassword,
                errLabel, btnDaftar
        );

        dialog.setScene(new Scene(form));
        dialog.setResizable(false);
        dialog.showAndWait();
    }

    private void styleTextField(TextField tf) {
        tf.setStyle(
            "-fx-background-color: " + StyleKit.CARD_BG + ";" +
            "-fx-text-fill: " + StyleKit.TEXT_PRIMARY + ";" +
            "-fx-prompt-text-fill: " + StyleKit.TEXT_MUTED + ";" +
            "-fx-border-color: " + StyleKit.BORDER + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10 14;" +
            "-fx-font-size: 14px;"
        );
        tf.focusedProperty().addListener((obs, old, focused) -> {
            if (focused) {
                tf.setStyle(
                    "-fx-background-color: " + StyleKit.CARD_BG + ";" +
                    "-fx-text-fill: " + StyleKit.TEXT_PRIMARY + ";" +
                    "-fx-prompt-text-fill: " + StyleKit.TEXT_MUTED + ";" +
                    "-fx-border-color: " + StyleKit.ACCENT + ";" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 10 14;" +
                    "-fx-font-size: 14px;"
                );
            } else {
                tf.setStyle(
                    "-fx-background-color: " + StyleKit.CARD_BG + ";" +
                    "-fx-text-fill: " + StyleKit.TEXT_PRIMARY + ";" +
                    "-fx-prompt-text-fill: " + StyleKit.TEXT_MUTED + ";" +
                    "-fx-border-color: " + StyleKit.BORDER + ";" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 10 14;" +
                    "-fx-font-size: 14px;"
                );
            }
        });
    }

    public void show() {
        stage.setTitle("ThreadHub — Login");
        
        HBox layout = buildLayout();
        if (stage.getScene() != null) {
            stage.getScene().setRoot(layout);
        } else {
            Scene scene = new Scene(layout, 840, 560);
            stage.setScene(scene);
        }
        
        stage.setResizable(true);    
        stage.setMaximized(true);    
        stage.show();
    }
}