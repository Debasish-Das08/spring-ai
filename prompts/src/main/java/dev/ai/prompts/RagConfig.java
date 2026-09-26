package dev.ai.prompts;

import com.openai.models.vectorstores.VectorStore;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class RagConfig {

    @Value("classpath:/docs/Cricket_rules_FAQ_for_RAG.pdf")
    private Resource resource;
//
//    @Bean
//    SimpleVectorStore
}
