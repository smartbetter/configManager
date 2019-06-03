package com.example.server.manager.web.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.example.server.manager.web.interceptor.AuthInterceptor;
import com.example.server.manager.web.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final List<String> EXCLUDE_PATH_PATTERNS = Arrays.asList("/login/**", "/json/user/**",
            "/illegal.html", "/js/plugins/layui/**", "/css/busi/login.css", "/css/busi/illegal.css");

    /**
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LoginInterceptor loginInterceptor = new LoginInterceptor();
        AuthInterceptor authInterceptor = new AuthInterceptor();
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATH_PATTERNS);
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATH_PATTERNS);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new AllEncompassingFormHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        converters.add(fastJsonHttpMessageConverter());
    }

    /**
     * 返回json时的类型转换器
     */
    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

        List<MediaType> list = new ArrayList<MediaType>();
        // 避免IE出现下载JSON文件的情况
        list.add(MediaType.valueOf("text/html;charset=UTF-8"));
        list.add(MediaType.valueOf("application/json;charset=UTF-8"));
        converter.setSupportedMediaTypes(list);

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        // 格式化Date并转String
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");

        // Integer、Long、Double转String
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(Integer.class, ToStringSerializer.instance);
        serializeConfig.put(Integer.TYPE, ToStringSerializer.instance);
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        serializeConfig.put(Double.class, ToStringSerializer.instance);
        serializeConfig.put(Double.TYPE, ToStringSerializer.instance);
        fastJsonConfig.setSerializeConfig(serializeConfig);

        // BigDecimal转String
        ValueFilter valueFilter = new ValueFilter() {
            @Override
            public Object process(Object object, String name, Object value) {
                if(value instanceof BigDecimal) {
                    return((BigDecimal)value).toPlainString();
                }
                return value;
            }
        };
        fastJsonConfig.setSerializeFilters(valueFilter);

        converter.setFastJsonConfig(fastJsonConfig);

        return converter;
    }
}
