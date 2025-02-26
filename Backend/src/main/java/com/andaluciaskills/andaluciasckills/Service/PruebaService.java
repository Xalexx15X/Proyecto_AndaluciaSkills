package com.andaluciaskills.andaluciasckills.Service;

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacion;
import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacionItem;
import com.andaluciaskills.andaluciasckills.Dto.DtoItem;
import com.andaluciaskills.andaluciasckills.Dto.DtoPrueba;
import com.andaluciaskills.andaluciasckills.Entity.Evaluacion;
import com.andaluciaskills.andaluciasckills.Entity.EvaluacionItem;
import com.andaluciaskills.andaluciasckills.Entity.Prueba;
import com.andaluciaskills.andaluciasckills.Error.PruebaNotFoundException;
import com.andaluciaskills.andaluciasckills.Mapper.EvaluacionMapper;
import com.andaluciaskills.andaluciasckills.Mapper.ItemMapper;
import com.andaluciaskills.andaluciasckills.Mapper.PruebaMapper;
import com.andaluciaskills.andaluciasckills.Repository.PruebaRepository;
import com.andaluciaskills.andaluciasckills.Repository.ItemRepository;
import com.andaluciaskills.andaluciasckills.Repository.EvaluacionRepository;
import com.andaluciaskills.andaluciasckills.Repository.EvaluacionItemRepository;
import com.andaluciaskills.andaluciasckills.Service.base.PruebaBaseService;

import com.andaluciaskills.andaluciasckills.Entity.Item;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PruebaService implements PruebaBaseService {
    private final PruebaRepository pruebaRepository;
    private final PruebaMapper pruebaMapper;
    private final ItemRepository itemRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final EvaluacionItemRepository evaluacionItemRepository;
    private final ItemMapper itemMapper;
    private final EvaluacionMapper evaluacionMapper;
    private final EvaluacionService evaluacionService;

    @Override
    public DtoPrueba save(DtoPrueba dto) {
        Prueba prueba = pruebaMapper.toEntity(dto);
        Prueba savedPrueba = pruebaRepository.save(prueba);
        return pruebaMapper.toDto(savedPrueba);
    }

    @Override
    public Optional<DtoPrueba> findById(Integer id) {
        return pruebaRepository.findById(id)
                .map(pruebaMapper::toDto);
    }

    @Override
    public List<DtoPrueba> findAll() {
        return pruebaMapper.toDtoList(pruebaRepository.findAll());
    }

    @Override
    public DtoPrueba update(DtoPrueba dto) {
        Prueba prueba = pruebaMapper.toEntity(dto);
        Prueba updatedPrueba = pruebaRepository.save(prueba);
        return pruebaMapper.toDto(updatedPrueba);
    }

    @Override
    public void delete(Integer id) {
        pruebaRepository.deleteById(id);
    }

    public DtoPrueba crearPruebaConItems(DtoPrueba dtoPrueba, List<DtoItem> dtoItems) {
        try {
            // PASO 1: Crear y guardar la prueba
            DtoPrueba pruebaGuardada = save(dtoPrueba);
            System.out.println("Prueba creada con ID: " + pruebaGuardada.getIdPrueba());

            // PASO 2: Asignar el ID de la prueba a cada item
            dtoItems.forEach(item -> {
                item.setPrueba_id_Prueba(pruebaGuardada.getIdPrueba());
            });

            // PASO 3: Guardar los items (explicación detallada abajo)
            List<DtoItem> itemsGuardados = itemRepository.saveAll(dtoItems.stream()
                .map(itemMapper::toEntity)      // Convierte DTOs a entidades
                .collect(Collectors.toList()))   // Guarda en lista
                .stream()                       // Nuevo stream con items guardados
                .map(itemMapper::toDto)         // Convierte entidades a DTOs
                .collect(Collectors.toList());   // Guarda en lista final

            // PASO 4: Log de los IDs creados
            System.out.println("Items creados con IDs: " + 
                itemsGuardados.stream()
                    .map(item -> item.getIdItem().toString())  // Obtiene IDs
                    .collect(Collectors.joining(", ")));        // Une con comas

            return pruebaGuardada;
        } catch (Exception e) {
            System.err.println("Error en crearPruebaConItems: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public DtoEvaluacion crearEvaluacionParaPrueba(Integer pruebaId, Integer participanteId, Integer userId) {
        try {
            System.out.println("Creando evaluación con: pruebaId=" + pruebaId + 
                              ", participanteId=" + participanteId + 
                              ", userId=" + userId);

            DtoEvaluacion evaluacion = new DtoEvaluacion();
            evaluacion.setPrueba_idPrueba(pruebaId);
            evaluacion.setParticipante_idParticipante(participanteId);
            evaluacion.setUser_idUser(userId);
            evaluacion.setNotaFinal(0.0);

            // Guardar la evaluación
            DtoEvaluacion evaluacionGuardada = evaluacionService.save(evaluacion);
            System.out.println("Evaluación guardada: " + evaluacionGuardada);
            
            return evaluacionGuardada;
        } catch (Exception e) {
            System.err.println("Error en crearEvaluacionParaPrueba: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<DtoPrueba> findByEspecialidadId(Integer especialidadId) {
        List<Prueba> pruebas = pruebaRepository.findByEspecialidadId(especialidadId);
        return pruebaMapper.toDtoList(pruebas);
    }

    public DtoEvaluacion actualizarNotasEvaluacion(Integer evaluacionId, List<DtoEvaluacionItem> evaluacionItems) {
        try {
            // 1. Log para debugging
            System.out.println("Procesando evaluacionItems: " + evaluacionItems);
            
            // 2. Verifica si existe la evaluación y la obtiene
            Evaluacion evaluacion = evaluacionRepository.findById(evaluacionId)
                .orElseThrow(() -> new RuntimeException("Evaluación no encontrada"));
                
            // 3. Verifica que no tenga items ya evaluados
            if (evaluacionItemRepository.existsById(evaluacionId)) {
                throw new RuntimeException("Esta evaluación ya tiene items evaluados");
            }
            
            // 4. Guarda cada item de evaluación
            List<EvaluacionItem> evaluacionesGuardadas = evaluacionItems.stream()
                .map(dto -> {
                    EvaluacionItem entity = new EvaluacionItem();
                    entity.setValoracion(dto.getValoracion());
                    entity.setEvaluacion_idEvaluacion(evaluacionId);
                    entity.setItem_idItem(dto.getItem_idItem());
                    return evaluacionItemRepository.save(entity);
                })
                .collect(Collectors.toList());

            // 5. Aquí viene la parte importante: Calcular la nota final
            double notaFinal = evaluacionesGuardadas.stream()
                .mapToDouble(ei -> {  // Convierte cada EvaluacionItem a un valor double
                    // Obtiene el Item correspondiente
                    Item item = itemRepository.findById(ei.getItem_idItem())
                        .orElseThrow(() -> new RuntimeException("Item no encontrado"));
                    
                    // Calcula la nota ponderada:
                    // (valoración * peso) / 100
                    // Ejemplo: Si la valoración es 8 y el peso es 25%
                    // (8 * 25) / 100 = 2 puntos sobre la nota final
                    return (ei.getValoracion() * item.getPeso()) / 100.0;
                })
                .sum();  // Suma todos los valores calculados

            // 6. Actualiza y guarda la nota final en la evaluación
            evaluacion.setNotaFinal(notaFinal);
            evaluacion = evaluacionRepository.save(evaluacion);
            
            // 7. Convierte y devuelve el resultado
            return evaluacionMapper.toDto(evaluacion);

        } catch (Exception e) {
            System.err.println("Error al actualizar notas: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public boolean existeEvaluacion(Integer pruebaId, Integer participanteId) {
        return evaluacionRepository.existsByPruebaIdAndParticipanteId(pruebaId, participanteId
        );
    }

    public byte[] generarPlantillaEvaluacion(Integer pruebaId) throws IOException, com.itextpdf.text.DocumentException {
        // Busca la prueba por ID o lanza excepción si no existe
        Prueba prueba = pruebaRepository.findById(pruebaId)
            .orElseThrow(() -> new PruebaNotFoundException(pruebaId));
            
        // Obtiene todos los items asociados a la prueba
        List<Item> items = itemRepository.findByPruebaIdPrueba(pruebaId);

        // Crea un nuevo documento PDF
        Document document = new Document();
        // Stream para almacenar el PDF en memoria
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Configura el escritor PDF
        PdfWriter.getInstance(document, baos);
        // Abre el documento para escribir
        document.open();
        
        // Agrega el título centrado
        Paragraph titulo = new Paragraph("Plantilla de Evaluación");
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);
        document.add(new Paragraph("\n")); // Espacio

        // Agrega información de la prueba
        document.add(new Paragraph("Enunciado: " + prueba.getEnunciado()));
        document.add(new Paragraph("Puntuación Máxima: " + prueba.getPuntuacionMaxima()));
        document.add(new Paragraph("\n")); // Espacio
        
        // Crea tabla con 4 columnas
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100); // Ocupa todo el ancho

        // Configura el estilo de las cabeceras
        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setPadding(5);

        // Define y agrega las cabeceras
        String[] headers = {"Criterio de Evaluación", "Peso (%)", 
                           "Grados de Consecución", "Valoración"};
        for (String header : headers) {
            headerCell.setPhrase(new Phrase(header));
            table.addCell(headerCell);
        }
        
        // Si hay items, los procesa uno a uno
        if (items != null && !items.isEmpty()) {
            for (Item item : items) {
                System.out.println("Procesando item: " + item.getDescripcion());
                
                // Configura el estilo de las celdas
                PdfPCell cell = new PdfPCell();
                cell.setPadding(5);
                
                // Agrega descripción del item
                cell.setPhrase(new Phrase(item.getDescripcion()));
                table.addCell(cell);
                
                // Agrega peso del item
                cell.setPhrase(new Phrase(String.valueOf(item.getPeso())));
                table.addCell(cell);
                
                // Agrega grados de consecución
                cell.setPhrase(new Phrase(String.valueOf(item.getGradosConsecucion())));
                table.addCell(cell);
                
                // Agrega espacio para valoración
                cell.setPhrase(new Phrase("_______"));
                table.addCell(cell);
            }
        } else {
            System.out.println("No se encontraron items para la prueba"); // Debug
        }
        
        // Agrega la tabla al documento
        document.add(table);
        document.add(new Paragraph("\n")); // Espacio

        // Agrega el total alineado a la derecha
        Paragraph total = new Paragraph("Puntuación Total: _____________");
        total.setAlignment(Element.ALIGN_RIGHT);
        document.add(total);

        // Cierra el documento
        document.close();

        // Devuelve el PDF como array de bytes
        return baos.toByteArray();
    }
}