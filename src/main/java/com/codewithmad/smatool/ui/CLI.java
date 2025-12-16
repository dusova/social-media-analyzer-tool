package com.codewithmad.smatool.ui;

import com.codewithmad.smatool.analyzer.SocialMediaAnalyzer;
import com.codewithmad.smatool.source.*;
import java.io.IOException;
import java.util.Scanner;

public class CLI {
    
    private final Scanner scanner;
    private final SocialMediaAnalyzer analyzer;

    public CLI() {
        this.scanner = new Scanner(System.in);
        this.analyzer = SocialMediaAnalyzer.getInstance();
    }

    public void start() {
        printBanner();
        
        try {
            selectDataSource();
            showAnalysisMenu();
        } catch (Exception e) {
            System.err.println("\nHATA: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private void printBanner() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                          ║");
        System.out.println("║        SOSYAL MEDYA ANALİZ ARACI (SMATool)               ║");
        System.out.println("║        Nesne Tabanlı Programlama Projesi                 ║");
        System.out.println("║        @dusova - @fatihcobann - @cydigitdeveloper        ║");
        System.out.println("║                                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private void selectDataSource() throws IOException, InterruptedException {
        System.out.println("┌────────────────────────────────────────┐");
        System.out.println("│   VERİ KAYNAĞI SEÇİMİ                  │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.println("1. Web API (Hosting)");
        System.out.println("2. JSON Dosyası");
        System.out.println("3. Sahte Veri (DummyAPI - Offline Test)");
        System.out.println("4. Reddit API (Subreddit/Topic Analizi)");
        System.out.println();
        System.out.print("Seçiminiz (1-4): ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\nVeri yükleniyor...");

        switch (choice) {
            case 1:
                System.out.print("API URL'sini girin: ");
                String url = scanner.nextLine();
                if (url.trim().isEmpty()) {
                    url = "https://social.codewithmad.com/api/data.json";
                }
                analyzer.loadFromSource(new ApiSource(url));
                System.out.println("Web API'den yüklendi!");
                break;

            case 2:
                System.out.print("JSON dosya yolu: ");
                String path = scanner.nextLine();
                analyzer.loadFromSource(new JsonFileSource(path));
                System.out.println("JSON dosyasından yüklendi!");
                break;

            case 3:
                analyzer.loadFromSource(new DummySource());
                System.out.println("Test verisi yüklendi (DummyAPI)!");
                break;

            case 4:
                loadFromReddit();
                break;

            default:
                throw new IllegalArgumentException("Geçersiz seçim!");
        }

        System.out.println("Toplam " + analyzer.getTotalPosts() + " gönderi yüklendi.\n");
        Thread.sleep(1000);
    }

    private void loadFromReddit() throws IOException {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│   REDDIT API SEÇENEKLERİ               │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.println("1. Subreddit'ten çek (örn: technology, programming)");
        System.out.println("2. Konu araması yap (tüm Reddit'te ara)");
        System.out.print("\nSeçiminiz (1-2): ");
        
        int redditChoice = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Subreddit/Konu adı girin: ");
        String topic = scanner.nextLine().trim();
        
        if (topic.isEmpty()) {
            topic = "technology"; 
        }
        
        if (topic.startsWith("r/")) {
            topic = topic.substring(2);
        }
        
        System.out.print("Kaç post çekilsin? (max 100, varsayılan 50): ");
        String limitStr = scanner.nextLine().trim();
        int limit = 50;
        if (!limitStr.isEmpty()) {
            try {
                limit = Integer.parseInt(limitStr);
            } catch (NumberFormatException e) {
                System.out.println("Geçersiz sayı, 50 kullanılıyor.");
            }
        }
        
        String searchType = (redditChoice == 2) ? "search" : "subreddit";
        
        System.out.println("\nReddit'ten veri çekiliyor: " + 
            (searchType.equals("search") ? "Arama: " : "r/") + topic + "...");
        
        analyzer.loadFromSource(new RedditSource(topic, searchType, limit));
        System.out.println("Reddit'ten başarıyla yüklendi!");
    }

    private void showAnalysisMenu() throws IOException {
        while (true) {
            System.out.println("\n┌────────────────────────────────────────┐");
            System.out.println("│   ANALİZ MENÜSÜ                        │");
            System.out.println("└────────────────────────────────────────┘");
            System.out.println("1. Keyword Analizi");
            System.out.println("2. Duygu Analizi (Sentiment)");
            System.out.println("3. En Aktif Kullanıcılar");
            System.out.println("4. İstatistiksel Özet");
            System.out.println("5. Tüm Analizleri Göster");
            System.out.println("6. Rapor Kaydet (TXT)");
            System.out.println("0. Çıkış");
            System.out.println();
            System.out.print("Seçiminiz (0-6): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: analyzer.printKeywordAnalysis(); break;
                case 2: analyzer.printSentimentAnalysis(); break;
                case 3: analyzer.printActiveUsers(5); break;
                case 4: analyzer.printStatistics(); break;
                case 5:
                    analyzer.printKeywordAnalysis();
                    analyzer.printSentimentAnalysis();
                    analyzer.printActiveUsers(5);
                    analyzer.printStatistics();
                    break;
                case 6:
                    System.out.print("Dosya adı (örn: rapor.txt): ");
                    String filename = scanner.nextLine();
                    if (filename.trim().isEmpty()) {
                        filename = "rapor_" + System.currentTimeMillis() + ".txt";
                    }
                    analyzer.saveReport(filename);
                    break;
                case 0:
                    System.out.println("\n╔══════════════════════════════════════╗");
                    System.out.println("║   Görüşmek üzere!                    ║");
                    System.out.println("╚══════════════════════════════════════╝");
                    return;
                default:
                    System.out.println("Geçersiz seçim!");
            }

            System.out.println("\nDevam için Enter'a basın...");
            scanner.nextLine();
        }
    }
}