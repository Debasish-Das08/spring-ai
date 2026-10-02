package dev.ai.rag.ragdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class RagConfig {

    private static final Logger log = LoggerFactory.getLogger(RagConfig.class);

    @Value("vectorestore.json")
    private String vectoreStoreName;

    @Value("classpath:/docs/AsianGames.txt")
    private Resource faq;

    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel)
    {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File vectorFile = getVectorFile();
        if(vectorFile.exists())
        {
            log.info("vector File exist");
            simpleVectorStore.load(vectorFile);
        }else {
            log.info("Vector file doesnot exist");
            TextReader textReader = new TextReader(faq);
            textReader.getCustomMetadata().put("filename", "AsianGames.txt");
            List<Document> documents = textReader.get();
            TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder().build();
            List<Document> splitDocument = tokenTextSplitter.apply(documents);
            simpleVectorStore.add(splitDocument);
            simpleVectorStore.save(vectorFile);
        }

        return simpleVectorStore;
    }

    private File getVectorFile()
    {
        Path path = Paths.get("src", "main", "resources", "data");
        String absolutePath = path.toFile().getAbsoluteFile()+ "/" + vectoreStoreName;
        return new File(absolutePath);
    }

}
