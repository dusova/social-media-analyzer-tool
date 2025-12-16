package com.codewithmad.smatool.source;

import com.codewithmad.smatool.model.Post;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class RedditSource implements DataSource {
    
    private final String topic;
    private final String searchType;
    private final int limit;
    
    private static final String REDDIT_BASE_URL = "https://www.reddit.com";
    // Reddit bot korumasını aşmak için tarayıcı benzeri User-Agent kullanıyoruz
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    
    /**
     * Subreddit'ten veri çekmek için constructor
     * @param topic Subreddit adı (örn: "technology", "programming")
     */
    public RedditSource(String topic) {
        this(topic, "subreddit", 100);
    }
    
    /**
     * Tam özellikli constructor
     * @param topic Aranacak konu veya subreddit adı
     * @param searchType "subreddit" veya "search" (genel arama)
     * @param limit Çekilecek maksimum post sayısı (max 100)
     */
    public RedditSource(String topic, String searchType, int limit) {
        this.topic = topic;
        this.searchType = searchType;
        this.limit = Math.min(limit, 100); 
    }
    
    @Override
    public List<Post> fetch() throws IOException {
        String apiUrl = buildApiUrl();
        
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        
        int status = conn.getResponseCode();
        if (status != 200) {
            throw new IOException("Reddit API Hatası: HTTP " + status + 
                " - Subreddit bulunamadı veya erişim engellendi olabilir.");
        }
        
        try (Reader reader = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)) {
            return parseRedditResponse(reader);
        } finally {
            conn.disconnect();
        }
    }
    

    private String buildApiUrl() throws IOException {
        String encodedTopic = URLEncoder.encode(topic, StandardCharsets.UTF_8.toString());
        
        if ("search".equals(searchType)) {
            return String.format("%s/search.json?q=%s&limit=%d&sort=relevance&t=week", 
                REDDIT_BASE_URL, encodedTopic, limit);
        } else {
            return String.format("%s/r/%s/hot.json?limit=%d", 
                REDDIT_BASE_URL, encodedTopic, limit);
        }
    }

    private List<Post> parseRedditResponse(Reader reader) {
        List<Post> posts = new ArrayList<>();
        
        JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
        JsonObject data = root.getAsJsonObject("data");
        JsonArray children = data.getAsJsonArray("children");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .withZone(ZoneId.systemDefault());
        
        for (JsonElement child : children) {
            JsonObject postData = child.getAsJsonObject().getAsJsonObject("data");
            
            try {
                Post post = parsePost(postData, formatter);
                if (post != null && post.getMetin() != null && !post.getMetin().isEmpty()) {
                    posts.add(post);
                }
            } catch (Exception e) {
                System.err.println("Post parse hatası: " + e.getMessage());
            }
        }
        
        return posts;
    }
    

    private Post parsePost(JsonObject postData, DateTimeFormatter formatter) {
        String title = getJsonString(postData, "title");
        String selftext = getJsonString(postData, "selftext");
        
        String metin = title;
        if (selftext != null && !selftext.isEmpty() && !selftext.equals("[removed]")) {
            metin = title + " - " + selftext;
        }
        
        if (metin.length() > 500) {
            metin = metin.substring(0, 497) + "...";
        }
        
        String kullanici = getJsonString(postData, "author");
        String subreddit = getJsonString(postData, "subreddit_name_prefixed");
        
        String tarih = "";
        if (postData.has("created_utc") && !postData.get("created_utc").isJsonNull()) {
            long timestamp = postData.get("created_utc").getAsLong();
            tarih = formatter.format(Instant.ofEpochSecond(timestamp));
        }
        
        Post post = new Post(kullanici, subreddit, metin);
        
        try {
            java.lang.reflect.Field tarihField = Post.class.getDeclaredField("tarih");
            tarihField.setAccessible(true);
            tarihField.set(post, tarih);
            
            java.lang.reflect.Field idField = Post.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(post, getJsonString(postData, "id"));
        } catch (Exception e) {

            System.err.println("Post alan ayarlama hatası: " + e.getMessage());
        }
        
        return post;
    }
    
    private String getJsonString(JsonObject obj, String key) {
        if (obj.has(key) && !obj.get(key).isJsonNull()) {
            return obj.get(key).getAsString();
        }
        return "";
    }
    
    public String getTopic() {
        return topic;
    }
    
    public String getSearchType() {
        return searchType;
    }
}
