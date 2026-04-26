package io.github.lucas_goncalves_tech.tech_equipment_manager.core.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.jspecify.annotations.NonNull;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, @NonNull HttpHeaders headers, @NonNull HttpStatusCode statusCode, @NonNull WebRequest request) {
        ProblemDetail problem = buildProblem("Argumento inválidos", "urn:error:badRequest", "Erro nos parâmetros enviados", statusCode);
        List<Map<String, String>> invalidParams = exception
                .getBindingResult()
                .getFieldErrors().stream()
                .map((error) -> Map.of("field", error.getField(), "reason", (error.getDefaultMessage() != null) ? error.getDefaultMessage() : "")).toList();
        problem.setProperty("invalid_params", invalidParams);

        return handleExceptionInternal(exception, problem, headers, statusCode, request);
    }

    @ExceptionHandler(DomainInvalidException.class)
    public ProblemDetail handleDomainInvalid(DomainInvalidException exception) {
        return buildProblem("Dominio inválido", "urn:error:badRequest", exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthentication(AuthenticationException exception) {
        return buildProblem("Acesso não autorizado", "urn:error:unauthorized", exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(BadCredentialsException exception, WebRequest request) {
        ProblemDetail problem = buildProblem(
                "Credenciais Inválidas.",
                "urn:error:unauthorized",
                "E-mail ou senha inválidos.",
                HttpStatus.UNAUTHORIZED
        );

        return handleExceptionInternal(exception, problem, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ProblemDetail handleTokenExpired(TokenExpiredException exception) {
        return buildProblem(
                "Token Expirado",
                "urn:error:unauthorized",
                "O token fornecido expirou. Por favor, realize a autenticação novamente.",
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(SignatureVerificationException.class)
    public ProblemDetail handleSignatureVerification(SignatureVerificationException exception) {
        return buildProblem(
                "Assinatura Inválida",
                "urn:error:unauthorized",
                "A assinatura do token não é válida.",
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ProblemDetail handleGenericJwtVerification(JWTVerificationException exception) {
        return buildProblem(
                "Token Inválido",
                "urn:error:unauthorized",
                "O token fornecido é inválido ou malformado.",
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(DisabledException.class)
    public ProblemDetail handleDisabled(DisabledException exception) {
        return buildProblem(
                "Conta Desativada",
                "urn:error:forbidden",
                "Sua conta está inativa no sistema.",
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(LockedException.class)
    public ProblemDetail handleLocked(LockedException exception) {
        return buildProblem(
                "Conta Bloqueada",
                "urn:error:forbidden",
                "Sua conta foi bloqueada por segurança.",
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException exception) {
        return buildProblem("Acesso negado", "urn:error:forbidden", exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException exception) {
        return buildProblem("Recurso não encontrado", "urn:error:notFound", exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessConflictException.class)
    public ProblemDetail handleBusinessConflict(BusinessConflictException exception) {
        return buildProblem("Conflito de estado", "urn:error:conflict", exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleInternal(RuntimeException exception) {
        return buildProblem("Error interno no servidor", "urn:error:internalError", "Ocorreu um erro interno!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ProblemDetail buildProblem(String title, String type, String detail, HttpStatusCode statusCode) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(statusCode, detail);
        problem.setTitle(title);
        problem.setType(URI.create(type));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

}
