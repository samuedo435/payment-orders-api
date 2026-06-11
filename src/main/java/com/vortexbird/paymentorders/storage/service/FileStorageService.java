package com.vortexbird.paymentorders.storage.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Contrato para almacenamiento de archivos.
 */
public interface FileStorageService {

    /**
     * Almacena un archivo asociado a una orden.
     *
     * @param file archivo recibido
     * @param orderId identificador de la orden
     * @return ruta almacenada
     */
    String storeFile(
            MultipartFile file,
            Long orderId
    );
}