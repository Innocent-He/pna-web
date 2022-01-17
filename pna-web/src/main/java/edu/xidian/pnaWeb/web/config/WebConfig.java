package edu.xidian.pnaWeb.web.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author He
 * @Date 2021/10/3 19:11
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
	/**
	 * 注入一个ServerEndpointExporter,该Bean会自动注册使用@ServerEndpoint注解申明的websocket endpoint
	 */
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		// 注册Sa-Token的路由拦截器
//		registry.addInterceptor(new SaRouteInterceptor())
//				.addPathPatterns("/**")
//				.excludePathPatterns("/user/doLogin");
//	}

	/**
	 * 解决CORS跨域问题
	 *
	 * @param registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://127.0.0.1:8080", "http://82.157.55.18")
				.allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
				.allowCredentials(true)
				.maxAge(3600 * 24)
				.allowedHeaders("*");
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();

		List<MediaType> fastMediaTypes = new ArrayList<>();

		// 处理中文乱码问题
		fastJsonConfig.setCharset(Charset.forName("UTF-8"));
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		// 设置时间格式
		fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");

		fastMediaTypes.add(MediaType.APPLICATION_JSON);
		fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);

		// 在转换器中添加配置信息
		fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
		HttpMessageConverter converter = fastJsonHttpMessageConverter;

		StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
		stringConverter.setDefaultCharset(Charset.forName("UTF-8"));
		stringConverter.setSupportedMediaTypes(fastMediaTypes);
		converters.add(stringConverter);
		converters.add(converter);
	}

}
