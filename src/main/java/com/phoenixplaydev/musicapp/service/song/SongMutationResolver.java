package com.phoenixplaydev.musicapp.service.song;

import com.phoenixplaydev.musicapp.scalars.MultipartFileInput;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.FileOutputStream;
import java.io.IOException;

@Component
@Slf4j
public class SongMutationResolver implements GraphQLMutationResolver {

    @GraphQLMutation(name = "uploadSong")
    public void uploadSong(@GraphQLArgument(name = "file") MultipartFileInput file) throws IOException {
        log.info("GetSong");
        byte[] input = null;
        FileOutputStream fos = new FileOutputStream("C:\\Users\\phoen\\Desktop\\music.mp3");
        fos.write(input);
        fos.close();
        System.out.println("Музыка сохранена!");
    }

    @GraphQLMutation(name = "upload")
    public String upload(@ModelAttribute DataFetchingEnvironment environment)  {
        Part actualAvatar = environment.getArgument("file");
        log.info("File uploaded!");
        return "File uploaded successfully: ";
    }

}
