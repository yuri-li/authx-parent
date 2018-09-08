package org.authx.account.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Configuration
class JsonConfig(val instantFormatter: InstantFormatter) {

    /**
     * Intant 日期格式化
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Primary
    fun serializingObjectMapper(): ObjectMapper {
        val formatter = DateTimeFormatter.ofPattern(instantFormatter.dateFormat)
        val zoneId = ZoneId.of(instantFormatter.timeZone)

        val objectMapper = ObjectMapper()
        val javaTimeModule = JavaTimeModule()
        javaTimeModule.addSerializer(object : StdSerializer<Instant>(Instant::class.java) {
            override fun serialize(value: Instant, gen: JsonGenerator, serializers: SerializerProvider) = gen.writeString(value.atZone(zoneId).format(formatter))
        })
        javaTimeModule.addDeserializer(Instant::class.java, object : JsonDeserializer<Instant>() {
            override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): Instant = Instant.from(formatter.parse(parser.valueAsString))
        })
        objectMapper.registerModule(javaTimeModule)
        objectMapper.registerKotlinModule()
        return objectMapper
    }
}
@Component
@ConfigurationProperties("spring.jackson")
class InstantFormatter{
    lateinit var dateFormat:String
    lateinit var timeZone: String
}