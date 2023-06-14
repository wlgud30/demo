package com.example.demo.util.redis;

import com.example.demo.exception.ApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.demo.enums.ExceptionEnum.INTERNAL_SERVER_ERROR;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public final ObjectMapper objectMapper;

    public <T>Optional<T> getData(String key,Class<T> classType){
        var jsonData = (String)redisTemplate.opsForValue().get(key);
        try{
            if(StringUtils.hasText(jsonData)){
                return Optional.ofNullable(objectMapper.readValue(jsonData,classType));
            }
            return Optional.empty();
        }catch (JsonProcessingException e){
            throw new ApiException(INTERNAL_SERVER_ERROR);
        }
    }

    public void setData(String key,Object data){
        try{
            var jsonData = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().set(key,jsonData);
        }catch (JsonProcessingException e){
            throw new ApiException(INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteData(String key){
        redisTemplate.delete(key);
    }

    public void setData(String key, Object data, long time, TimeUnit timeUnit){
        try{
            var jsonData = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().set(key,jsonData,time,timeUnit);
        }catch (JsonProcessingException e){
            throw new ApiException(INTERNAL_SERVER_ERROR);
        }
    }
}
