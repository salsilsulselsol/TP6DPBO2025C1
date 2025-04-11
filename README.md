# TP5DPBO2025C1 - Flappy Bird  
Tugas Praktikum 5 Dasar Pemrograman Berbasis Objek (DPBO)  

# Janji  
Saya Faisal Nur Qolbi dengan NIM 2311399 mengerjakan Tugas Praktikum 6 dalam mata kuliah Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.  

# Desain Program  
Program ini merupakan implementasi game *Flappy Bird* menggunakan Java Swing dengan fitur GUI dan manajemen status permainan. Program menggunakan konsep OOP untuk mengelola objek pemain, pipa, dan logika permainan.  

## Class  
1. **App** - Class utama untuk menjalankan game.  
2. **FlappyBird** - Class inti yang mengatur logika permainan, GUI, dan interaksi pemain.  
3. **Player** - Model untuk karakter burung (posisi, kecepatan, dan gambar).  
4. **Pipe** - Model untuk pipa (posisi, ukuran, dan gambar).  
5. **Start** - GUI untuk tampilan awal/menu utama.  

## Atribut  
### Class FlappyBird  
- `gameState` - Status permainan (START_SCREEN, PLAYING, GAME_OVER).  
- `player` - Objek Player (burung).  
- `pipes` - Daftar pipa yang aktif di layar.  
- `score` - Skor pemain.  
- `gravity` - Gaya gravitasi yang memengaruhi burung.  
- `pixelFont` - Font khusus untuk teks bertema *pixel*.  

### Class Player  
- `posX`, `posY` - Posisi burung.  
- `width`, `height` - Ukuran burung.  
- `velocityY` - Kecepatan vertikal burung.  
- `image` - Gambar sprite burung.  

### Class Pipe  
- `posX`, `posY` - Posisi pipa.  
- `width`, `height` - Ukuran pipa.  
- `image` - Gambar sprite pipa.  
- `velocityX` - Kecepatan horizontal pipa.  

### Class Start  
- `mainPanel` - Panel untuk menu awal.  
- `birdIconLabel` - Animasi burung di menu.  
- `pixelFont` - Font khusus untuk teks menu.  

## Metode Utama  
### Class FlappyBird  
- `placePipes()` - Membuat pipa baru dengan pola acak.  
- `draw()` - Merender semua komponen game (pemain, pipa, skor).  
- `collision()` - Mendeteksi tabungan antara burung dan pipa.  
- `gameOver()` - Mengubah status ke GAME_OVER.  
- `restartGame()` - Mengatur ulang permainan.  

### Class Start  
- `startBirdAnimation()` - Animasi burung di menu utama.  
- `startGame()` - Memulai permainan dan menutup menu.  

### Class App  
- `startGame()` - Membuka window permainan.  

# Alur Program  
1. **Menu Awal**:  
   - Program dimulai dengan tampilan menu utama (Class `Start`).  
   - Pemain dapat menekan **SPACE** atau klik tombol **START** untuk memulai.  
   - Burung di menu melakukan animasi naik-turun.  

2. **Permainan**:  
   - Burung bergerak dengan gravitasi. Tekan **SPACE** untuk membuatnya melompat.  
   - Pipa muncul secara acak dengan celah yang dapat dilewati.  
   - Skor bertambah setiap kali berhasil melewati pipa.  

3. **Game Over**:  
   - Jika burung menabrak pipa atau jatuh, tampilan **GAME OVER** muncul.  
   - Tekan **R** untuk mengulang permainan.  
   - Skor direset, dan permainan kembali ke status awal.  

4. **Validasi**:  
   - Input kosong (tidak menekan SPACE) di menu awal akan membiarkan game tetap di status START_SCREEN.  

# Dokumentasi  
<Soon>
