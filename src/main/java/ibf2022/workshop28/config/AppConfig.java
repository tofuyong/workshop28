// package ibf2022.workshop28.config;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.mongodb.core.MongoTemplate;

// import com.mongodb.client.MongoClients;

// @Configuration
// public class AppConfig {
//     @Value("${mongo.url}")
//     private String mongoUrl;

//     @Bean
//     public MongoTemplate createMongoTemplate() {
//         return new MongoTemplate(MongoClients.create(mongoUrl), "boardgames");
//     }
// }
