package com.example.demo.config.swagger;

import com.example.demo.dto.ResponseDto;
import com.example.demo.util.responseUtil.CustomFailResponseAnnotations;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Component
public class CustomOperationCustomizer implements OperationCustomizer {
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        var method = handlerMethod.getMethod().getDeclaredAnnotations();
        var responses = operation.getResponses();
        for (var i : method) {
            if(i.annotationType().equals(CustomFailResponseAnnotations.class)){
                for(var j : ((CustomFailResponseAnnotations) i).value()){
                    var ano =  j;
                    var response = responses.getOrDefault(ano.exception().getStatus().value()+"",new ApiResponse());
                    var content = response.getContent();
                    if(content==null){
                        content = new Content();
                    }
                    var schema = new Schema<>();
                    schema.$ref("#/components/schemas/ResponseDto");
                    var exception = ano.exception();
                    var errorResponse = new ResponseDto<Void>(exception);
                    var sc = content.get("application/json");
                    if(sc == null){
                        sc = new io.swagger.v3.oas.models.media.MediaType().schema(schema);
                    }
                    var example = new Example();
                    example.value(errorResponse);
                    sc.addExamples(errorResponse.getMessage(),example);
                    content.put("application/json",sc);
                    response.content(content);
                    responses.addApiResponse(ano.exception().getStatus().value()+"",response);
                }
            }
        }
        operation.responses(responses);
        return operation;
    }
}
