package org.authx.server.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant


data class ErrorDetails(
        val timestamp: Instant = Instant.now(),
        val message: Map<String, String>
)


@RestControllerAdvice
class GlobalExceptionHandler {
    val log = LogFactory.getLog(this::class.java)

    @RequestMapping(produces = arrayOf("application/json"))
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ErrorDetails {
        log.warn("参数校验失败，message=${ex.bindingResult}")
        return ex.bindingResult.format()
    }

    @RequestMapping(produces = arrayOf("application/json"))
    @ExceptionHandler(RuntimeException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleRuntimeException(ex: RuntimeException): String {
        log.warn("运行时异常，message=${ex.message}")
        return ex.message!!
    }

    private fun BindingResult.format(): ErrorDetails = ErrorDetails(message = this.allErrors.associateBy(
            {
                when (it) {
                    is FieldError -> it.field
                    is ObjectError -> (it.arguments!!.last() as Array<String>).joinToString(",")
                    else -> ""
                }
            },
            { it.defaultMessage!! }
    ))

}