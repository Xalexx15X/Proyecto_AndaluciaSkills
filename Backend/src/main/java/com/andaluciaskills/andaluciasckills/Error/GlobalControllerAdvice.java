package com.andaluciaskills.andaluciasckills.Error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

    // ========== Especialidad Exceptions ==========
    
    /**
     * Maneja las excepciones cuando no se encuentra una especialidad.
     * @param ex La excepción lanzada cuando no se encuentra la especialidad
     * @return ResponseEntity con estado 404 y detalles del error
     */ 
    @ExceptionHandler(EspecialidadNotFoundException.class)
    public ResponseEntity<ApiError> handleEspecialidadNotFound(EspecialidadNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    
    /**
     * Maneja las excepciones cuando una búsqueda de especialidades no produce resultados.
     * @param ex La excepción lanzada cuando la búsqueda no tiene resultados
     * @return ResponseEntity con estado 404 y detalles del error
     */
    
    @ExceptionHandler(SearchEspecialidadNoResultException.class)
    public ResponseEntity<ApiError> handleSearchEspecialidadNoResult(SearchEspecialidadNoResultException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    /**
     * Maneja las excepciones de peticiones incorrectas relacionadas con especialidades.
     * @param ex La excepción lanzada cuando hay errores en la petición
     * @return ResponseEntity con estado 400 y detalles del error
    */
    
    @ExceptionHandler(EspecialidadBadRequestException.class)
    public ResponseEntity<ApiError> handleEspecialidadBadRequest(EspecialidadBadRequestException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    // ========== Evaluacion Exceptions ==========
            
    /**
     * Maneja las excepciones cuando no se encuentra una evaluación específica.
     * @param ex La excepción lanzada cuando no se encuentra la evaluación
     * @return ResponseEntity con estado 404 y detalles del error
     */
    @ExceptionHandler(EvaluacionNotFoundException.class)
    public ResponseEntity<ApiError> handleEvaluacionNotFound(EvaluacionNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    /**
     * Maneja las excepciones cuando una búsqueda de evaluaciones no produce resultados.
     * @param ex La excepción lanzada cuando la búsqueda no tiene resultados
     * @return ResponseEntity con estado 404 y detalles del error
     */
    @ExceptionHandler(SearchEvaluacionNoResultException.class)
    public ResponseEntity<ApiError> handleSearchEvaluacionNoResult(SearchEvaluacionNoResultException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    /**
     * Maneja las excepciones de peticiones incorrectas relacionadas con evaluaciones.
     * @param ex La excepción lanzada cuando hay errores en la petición
     * @return ResponseEntity con estado 400 y detalles del error
     */
    @ExceptionHandler(EvaluacionBadRequestException.class)
    public ResponseEntity<ApiError> handleEvaluacionBadRequest(EvaluacionBadRequestException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    // ========== EvaluacionItem Exceptions ==========

    /**
     * Maneja las excepciones cuando no se encuentra un ítem de evaluación.
     * @param ex La excepción lanzada cuando no se encuentra el ítem
     * @return ResponseEntity con estado 404 y detalles del error
     */
    @ExceptionHandler(EvaluacionItemNotFoundException.class)
    public ResponseEntity<ApiError> handleEvaluacionItemNotFound(EvaluacionItemNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    /**
     * Maneja las excepciones cuando una búsqueda de ítems de evaluación no produce resultados.
     * @param ex La excepción lanzada cuando la búsqueda no tiene resultados
     * @return ResponseEntity con estado 404 y detalles del error
     */
    @ExceptionHandler(SearchEvaluacionItemNoResultException.class)
    public ResponseEntity<ApiError> handleSearchEvaluacionItemNoResult(SearchEvaluacionItemNoResultException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    /**
     * Maneja las excepciones de peticiones incorrectas relacionadas con ítems de evaluación.
     * @param ex La excepción lanzada cuando hay errores en la petición
     * @return ResponseEntity con estado 400 y detalles del error
     */
    @ExceptionHandler(EvaluacionItemBadRequestException.class)
    public ResponseEntity<ApiError> handleEvaluacionItemBadRequest(EvaluacionItemBadRequestException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    // ========== Item Exceptions ==========

    /**
     * Maneja las excepciones cuando no se encuentra un ítem.
     * @param ex La excepción lanzada cuando no se encuentra el ítem
     * @return ResponseEntity con estado 404 y detalles del error
     */
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ApiError> handleItemNotFound(ItemNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    /**
     * Maneja las excepciones cuando una búsqueda de ítems no produce resultados.
     * @param ex La excepción lanzada cuando la búsqueda no tiene resultados
     * @return ResponseEntity con estado 404 y detalles del error
     */
    @ExceptionHandler(SearchItemNoResultException.class)
    public ResponseEntity<ApiError> handleSearchItemNoResult(SearchItemNoResultException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    /**
     * Maneja las excepciones de peticiones incorrectas relacionadas con ítems.
     * @param ex La excepción lanzada cuando hay errores en la petición
     * @return ResponseEntity con estado 400 y detalles del error
     */
    @ExceptionHandler(ItemBadRequestException.class)
    public ResponseEntity<ApiError> handleItemBadRequest(ItemBadRequestException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    // ========== Participante Exceptions ==========

    /**
     * Maneja las excepciones cuando no se encuentra un participante.
     * @param ex La excepción lanzada cuando no se encuentra el participante
     * @return ResponseEntity con estado 404 y detalles del error
     */
    @ExceptionHandler(ParticipanteNotFoundException.class)
    public ResponseEntity<ApiError> handleParticipanteNotFound(ParticipanteNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    /**
     * Maneja las excepciones cuando una búsqueda de participantes no produce resultados.
     * @param ex La excepción lanzada cuando la búsqueda no tiene resultados
     * @return ResponseEntity con estado 404 y detalles del error
     */
    @ExceptionHandler(SearchParticipanteNoResultException.class)
    public ResponseEntity<ApiError> handleSearchParticipanteNoResult(SearchParticipanteNoResultException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    /**
     * Maneja las excepciones de peticiones incorrectas relacionadas con participantes.
     * @param ex La excepción lanzada cuando hay errores en la petición
     * @return ResponseEntity con estado 400 y detalles del error
     */
    @ExceptionHandler(ParticipanteBadRequestException.class)
    public ResponseEntity<ApiError> handleParticipanteBadRequest(ParticipanteBadRequestException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    // ========== Prueba Exceptions ==========

    /**
     * Maneja las excepciones cuando no se encuentra una prueba.
     * @param ex La excepción lanzada cuando no se encuentra la prueba
     * @return ResponseEntity con estado 404 y detalles del error
     */
    @ExceptionHandler(PruebaNotFoundException.class)
    public ResponseEntity<ApiError> handlePruebaNotFound(PruebaNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    /**
     * Maneja las excepciones cuando una búsqueda de pruebas no produce resultados.
     * @param ex La excepción lanzada cuando la búsqueda no tiene resultados
     * @return ResponseEntity con estado 404 y detalles del error
     */
    @ExceptionHandler(SearchPruebaNoResultException.class)
    public ResponseEntity<ApiError> handleSearchPruebaNoResult(SearchPruebaNoResultException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    /**
     * Maneja las excepciones de peticiones incorrectas relacionadas con pruebas.
     * @param ex La excepción lanzada cuando hay errores en la petición
     * @return ResponseEntity con estado 400 y detalles del error
     */
    @ExceptionHandler(PruebaBadRequestException.class)
    public ResponseEntity<ApiError> handlePruebaBadRequest(PruebaBadRequestException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    // ========== User Exceptions ==========

    /**
     * Maneja las excepciones cuando no se encuentra un usuario.
     * @param ex La excepción lanzada cuando no se encuentra el usuario
     * @return ResponseEntity con estado 404 y detalles del error
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    /**
     * Maneja las excepciones cuando una búsqueda de usuarios no produce resultados.
     * @param ex La excepción lanzada cuando la búsqueda no tiene resultados
     * @return ResponseEntity con estado 404 y detalles del error
     */
    @ExceptionHandler(SearchUserNoResultException.class)
    public ResponseEntity<ApiError> handleSearchUserNoResult(SearchUserNoResultException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    /**
     * Maneja las excepciones de peticiones incorrectas relacionadas con usuarios.
     * @param ex La excepción lanzada cuando hay errores en la petición
     * @return ResponseEntity con estado 400 y detalles del error
     */
    @ExceptionHandler(UserBadRequestException.class)
    public ResponseEntity<ApiError> handleUserBadRequest(UserBadRequestException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    /**
     * Maneja cualquier excepción no controlada específicamente.
     * Proporciona una respuesta genérica de error interno del servidor.
     * @param ex La excepción no controlada
     * @return ResponseEntity con estado 500 y mensaje genérico de error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, 
            "Se ha producido un error interno en el servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
}