# 📊 SMATool - Social Media Analysis Tool

<div align="center">

![Java](https://img.shields.io/badge/Java-11+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![Reddit API](https://img.shields.io/badge/Reddit-API-FF4500?style=for-the-badge&logo=reddit&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

**Sosyal medya verilerini analiz eden, nesne tabanlı programlama prensipleriyle geliştirilmiş Java uygulaması.**

[Özellikler](#-özellikler) •
[Kurulum](#-kurulum) •
[Kullanım](#-kullanım) •
[API Kaynakları](#-veri-kaynakları) •
[Mimari](#-proje-mimarisi) •
[İletişim](#-i̇letişim)

</div>

---

## 🎯 Proje Hakkında

**SMATool (Social Media Analysis Tool)**, sosyal medya platformlarından veri çekerek çeşitli analizler yapabilen bir komut satırı uygulamasıdır. Nesne Tabanlı Programlama (OOP) prensipleri kullanılarak geliştirilmiştir.

### Desteklenen Platformlar
- 🔴 **Reddit API** - Subreddit ve konu bazlı analiz
- 🌐 **Custom Web API** - Özel JSON API desteği
- 📁 **JSON Dosyası** - Yerel veri analizi
- 🧪 **Dummy Data** - Test ve geliştirme için sahte veri

---

## ✨ Özellikler

### 📈 Analiz Yetenekleri

| Özellik | Açıklama |
|---------|----------|
| **🔑 Keyword Analizi** | En sık kullanılan kelimeleri tespit eder ve görselleştirir |
| **😊 Duygu Analizi** | Pozitif, negatif ve nötr içerikleri sınıflandırır |
| **👥 Aktif Kullanıcılar** | En çok paylaşım yapan kullanıcıları listeler |
| **📊 İstatistikler** | Detaylı istatistiksel özet sunar |
| **💾 Rapor Kaydetme** | Tüm analizleri TXT formatında dışa aktarır |

### 🔌 Veri Kaynakları

- **Reddit API**: Herhangi bir subreddit'ten veya konu aramasından gerçek zamanlı veri çekme
- **Web API**: Özel JSON endpoint'lerinden veri alma
- **JSON Dosyası**: Yerel JSON dosyalarını analiz etme
- **Dummy Source**: Offline test için hazır veri seti

---

## 🚀 Kurulum

### Gereksinimler

- ☕ **Java 11** veya üzeri
- 📦 **Maven 3.6+** (veya dahil edilen Maven Wrapper)

### Adımlar

1. **Repository'yi klonlayın**
   ```bash
   git clone https://github.com/dusova/social-media-analyzer-tool/.git
   cd social-media-analyzer-tool
   ```

2. **Projeyi derleyin**
   ```bash
   # Windows
   .\mvnw.cmd clean compile
   
   # Linux/macOS
   ./mvnw clean compile
   ```

3. **Uygulamayı çalıştırın**
   ```bash
   # Windows
   .\mvnw.cmd exec:java
   
   # Linux/macOS
   ./mvnw exec:java
   ```

---

## 📖 Kullanım

### Başlangıç

Uygulama başlatıldığında aşağıdaki menü görüntülenir:

```
╔══════════════════════════════════════════════════════════╗
║        SOSYAL MEDYA ANALİZ ARACI (SMATool)               ║
╚══════════════════════════════════════════════════════════╝

┌────────────────────────────────────────┐
│   VERİ KAYNAĞI SEÇİMİ                  │
└────────────────────────────────────────┘
1. Web API (Hosting)
2. JSON Dosyası
3. Sahte Veri (DummyAPI - Offline Test)
4. Reddit API (Subreddit/Topic Analizi)
```

### 🔴 Reddit API Kullanımı

1. **Seçenek 4**'ü seçin
2. Subreddit veya arama modunu belirleyin
3. Konu/subreddit adını girin (örn: `technology`, `programming`, `python`)
4. Çekilecek post sayısını belirleyin (max 100)

**Örnek Subreddit'ler:**
- `technology` - Teknoloji haberleri
- `programming` - Programlama tartışmaları
- `python` - Python dili
- `java` - Java geliştirme
- `news` - Güncel haberler

### 📊 Analiz Menüsü

```
┌────────────────────────────────────────┐
│   ANALİZ MENÜSÜ                        │
└────────────────────────────────────────┘
1. Keyword Analizi
2. Duygu Analizi (Sentiment)
3. En Aktif Kullanıcılar
4. İstatistiksel Özet
5. Tüm Analizleri Göster
6. Rapor Kaydet (TXT)
0. Çıkış
```

### Örnek Çıktılar

**Keyword Analizi:**
```
╔════════════════════════════════════════╗
║    KEYWORD ANALİZİ (TOP 10)            ║
╚════════════════════════════════════════╝
 1. technology      │ 45 kez │ ████████████████████
 2. programming     │ 32 kez │ ██████████████
 3. software        │ 28 kez │ ████████████
```

**İstatistiksel Özet:**
```
╔════════════════════════════════════════╗
║    İSTATİSTİKSEL ÖZET                  ║
╚════════════════════════════════════════╝
Toplam gönderi      : 50
Benzersiz kullanıcı : 45
Toplam kelime       : 2787
Ortalama kelime     : 55
```

---

## 🏗 Proje Mimarisi

```
SMATool/
├── src/main/java/com/codewithmad/smatool/
│   ├── SMATool.java              # Ana sınıf (Entry point)
│   ├── analyzer/
│   │   └── SocialMediaAnalyzer.java   # Analiz motoru (Singleton)
│   ├── model/
│   │   └── Post.java             # Veri modeli
│   ├── source/
│   │   ├── DataSource.java       # Interface (Abstraction)
│   │   ├── ApiSource.java        # Web API implementasyonu
│   │   ├── JsonFileSource.java   # JSON dosya implementasyonu
│   │   ├── DummySource.java      # Test verisi implementasyonu
│   │   └── RedditSource.java     # Reddit API implementasyonu
│   └── ui/
│       └── CLI.java              # Komut satırı arayüzü
├── pom.xml                       # Maven yapılandırması
├── mvnw                          # Maven Wrapper (Linux/macOS)
└── mvnw.cmd                      # Maven Wrapper (Windows)
```

### 🎨 Tasarım Desenleri

| Desen | Kullanım |
|-------|----------|
| **Singleton** | `SocialMediaAnalyzer` - Tek analiz motoru instance'ı |
| **Strategy** | `DataSource` interface - Farklı veri kaynakları |
| **Factory** | Veri kaynağı seçimine göre nesne oluşturma |

### 📦 Bağımlılıklar

- **Gson 2.10.1** - JSON parsing için Google Gson kütüphanesi

---

## 🔧 Yapılandırma

### Özel API Kullanımı

Web API seçeneğinde kendi JSON API'nizi kullanabilirsiniz. API'nizin aşağıdaki formatta veri döndürmesi gerekir:

```json
[
  {
    "kullanici": "username",
    "displayName": "Display Name",
    "metin": "Post içeriği",
    "tarih": "2025-01-01",
    "id": "unique-id"
  }
]
```

---

## 🤝 Katkıda Bulunma

1. Bu repository'yi fork edin
2. Feature branch oluşturun (`git checkout -b feature/YeniOzellik`)
3. Değişikliklerinizi commit edin (`git commit -m 'Yeni özellik eklendi'`)
4. Branch'inizi push edin (`git push origin feature/YeniOzellik`)
5. Pull Request açın

---

## 📜 Lisans

Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına bakınız.

---

## 👨‍💻 İletişim

<div align="center">

### **Mustafa Arda Düşova**

[![Email](https://img.shields.io/badge/Email-arda@codewithmad.com-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:arda@codewithmad.com)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-mdusova-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/mdusova)
[![GitHub](https://img.shields.io/badge/GitHub-dusova-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/dusova)
[![Website](https://img.shields.io/badge/Website-codewithmad.com-00C7B7?style=for-the-badge&logo=netlify&logoColor=white)](https://codewithmad.com)

</div>

---

<div align="center">

**⭐ Bu projeyi beğendiyseniz yıldız vermeyi unutmayın! ⭐**

Made with ❤️ by [Mustafa Arda Düşova](https://github.com/dusova)

</div>
