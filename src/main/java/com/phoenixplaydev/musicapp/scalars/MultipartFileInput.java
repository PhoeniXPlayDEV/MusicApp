package com.phoenixplaydev.musicapp.scalars;

import io.leangen.graphql.annotations.GraphQLInputField;
import io.leangen.graphql.annotations.GraphQLScalar;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@GraphQLScalar
@GraphQLType(name = "Upload")
public class MultipartFileInput implements Serializable {

    @GraphQLInputField
    private MultipartFile file;

}