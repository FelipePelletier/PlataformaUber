-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: plataforma_uber
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `calificaciones`
--

DROP TABLE IF EXISTS `calificaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `calificaciones` (
                                  `id_calificacion` int NOT NULL AUTO_INCREMENT,
                                  `puntuacion` int NOT NULL,
                                  `comentario` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                  `fecha_calificacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  `id_viaje` int NOT NULL,
                                  `id_usuario_calificador` int NOT NULL,
                                  `id_usuario_calificado` int NOT NULL,
                                  PRIMARY KEY (`id_calificacion`),
                                  UNIQUE KEY `id_viaje` (`id_viaje`,`id_usuario_calificador`),
                                  KEY `id_usuario_calificador` (`id_usuario_calificador`),
                                  KEY `id_usuario_calificado` (`id_usuario_calificado`),
                                  CONSTRAINT `calificaciones_ibfk_1` FOREIGN KEY (`id_viaje`) REFERENCES `viajes` (`id_viaje`),
                                  CONSTRAINT `calificaciones_ibfk_2` FOREIGN KEY (`id_usuario_calificador`) REFERENCES `usuarios` (`id_usuario`),
                                  CONSTRAINT `calificaciones_ibfk_3` FOREIGN KEY (`id_usuario_calificado`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calificaciones`
--

LOCK TABLES `calificaciones` WRITE;
/*!40000 ALTER TABLE `calificaciones` DISABLE KEYS */;
INSERT INTO `calificaciones` VALUES (1,5,'Excelente conductor','2026-11-06 15:33:19',1,1,2);
/*!40000 ALTER TABLE `calificaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pagos`
--

DROP TABLE IF EXISTS `pagos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pagos` (
                         `id_pago` int NOT NULL AUTO_INCREMENT,
                         `importe` decimal(10,2) NOT NULL,
                         `fecha_pago` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         `metodo_pago` enum('EFECTIVO','TARJETA','TRANSFERENCIA') COLLATE utf8mb4_unicode_ci NOT NULL,
                         `estado` enum('PENDIENTE','APROBADO','RECHAZADO') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDIENTE',
                         `id_viaje` int NOT NULL,
                         PRIMARY KEY (`id_pago`),
                         UNIQUE KEY `id_viaje` (`id_viaje`),
                         CONSTRAINT `pagos_ibfk_1` FOREIGN KEY (`id_viaje`) REFERENCES `viajes` (`id_viaje`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagos`
--

LOCK TABLES `pagos` WRITE;
/*!40000 ALTER TABLE `pagos` DISABLE KEYS */;
INSERT INTO `pagos` VALUES (1,3500.00,'2026-11-06 15:31:17','TARJETA','APROBADO',1);
/*!40000 ALTER TABLE `pagos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
                            `id_usuario` int NOT NULL AUTO_INCREMENT,
                            `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `apellido` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `dni` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `email` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `telefono` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                            `activo` tinyint(1) NOT NULL DEFAULT '1',
                            `calificacion_promedio` decimal(3,2) NOT NULL DEFAULT '0.00',
                            `cantidad_calificaciones` int NOT NULL DEFAULT '0',
                            `tipo_usuario` enum('CLIENTE','CONDUCTOR','ADMINISTRADOR') COLLATE utf8mb4_unicode_ci NOT NULL,
                            `numero_licencia` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                            `disponible` tinyint(1) NOT NULL DEFAULT '0',
                            `id_vehiculo_seleccionado` int DEFAULT NULL,
                            PRIMARY KEY (`id_usuario`),
                            UNIQUE KEY `dni` (`dni`),
                            UNIQUE KEY `email` (`email`),
                            KEY `fk_usuario_vehiculo_seleccionado` (`id_vehiculo_seleccionado`),
                            CONSTRAINT `fk_usuario_vehiculo_seleccionado` FOREIGN KEY (`id_vehiculo_seleccionado`) REFERENCES `vehiculos` (`id_vehiculo`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Juan','Perez','40111222','juan@gmail.com','2235551111','hash_de_prueba_1',1,0.00,0,'CLIENTE',NULL,0,NULL),(2,'Pedro','Gomez','39111222','pedro@gmail.com','2235552222','hash_de_prueba_2',1,5.00,1,'CONDUCTOR','LIC000002',0,NULL),(3,'Martin','Lopez','35123456','martin.actualizado@gmail.com','2235555557','password123',0,0.00,0,'CLIENTE',NULL,0,NULL),(5,'Carlos','Fernandez','40999888','carlos.prueba@gmail.com','2235558888','password123',0,0.00,0,'CLIENTE',NULL,0,NULL),(6,'Admin','Principal','30000000','admin@plataformauber.com','2235000000','admin12345',1,0.00,0,'ADMINISTRADOR',NULL,0,NULL),(7,'felipe','pelletier','42282629','felipe@gmail.com','2234386474','felipe123',1,0.00,0,'CLIENTE',NULL,0,NULL),(8,'martin','saucedo','42123456','martin@gmail.com','2234112233','martin123',1,0.00,0,'CLIENTE',NULL,0,NULL),(10,'victoria','giacomello','43445566','vicky@gmail.com','2236778899','vicky123',1,0.00,0,'CLIENTE',NULL,0,NULL),(11,'Carlos','Conductor','40123456','carlos.conductor@gmail.com','2235551234','conductor123',1,0.00,0,'CONDUCTOR','LIC123456',1,NULL);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehiculos`
--

DROP TABLE IF EXISTS `vehiculos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehiculos` (
                             `id_vehiculo` int NOT NULL AUTO_INCREMENT,
                             `patente` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
                             `marca` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                             `modelo` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                             `anio` int NOT NULL,
                             `color` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
                             `tipo_vehiculo` enum('AUTO','MOTO') COLLATE utf8mb4_unicode_ci NOT NULL,
                             `disponible` tinyint(1) NOT NULL DEFAULT '1',
                             `id_conductor` int NOT NULL,
                             `cant_puertas` int DEFAULT NULL,
                             `cilindrada` int DEFAULT NULL,
                             PRIMARY KEY (`id_vehiculo`),
                             UNIQUE KEY `patente` (`patente`),
                             KEY `id_conductor` (`id_conductor`),
                             CONSTRAINT `vehiculos_ibfk_1` FOREIGN KEY (`id_conductor`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehiculos`
--

LOCK TABLES `vehiculos` WRITE;
/*!40000 ALTER TABLE `vehiculos` DISABLE KEYS */;
INSERT INTO `vehiculos` VALUES (1,'AB123CD','Toyota','Corolla',2022,'Blanco','AUTO',1,2,NULL,NULL),(2,'AB456EF','Toyota','Corolla',2022,'Blanco','AUTO',1,11,4,NULL),(3,'AC123FG','Ford','Fiesta',2021,'Gris','AUTO',1,11,5,NULL),(4,'AD789GH','Honda','Wave',2023,'Rojo','MOTO',1,11,NULL,110);
/*!40000 ALTER TABLE `vehiculos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `viajes`
--

DROP TABLE IF EXISTS `viajes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `viajes` (
                          `id_viaje` int NOT NULL AUTO_INCREMENT,
                          `origen` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
                          `destino` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
                          `fecha_hora` datetime NOT NULL,
                          `precio` decimal(10,2) NOT NULL,
                          `estado` enum('PENDIENTE','ACEPTADO','EN_CURSO','FINALIZADO','CANCELADO') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDIENTE',
                          `id_cliente` int NOT NULL,
                          `id_conductor` int DEFAULT NULL,
                          PRIMARY KEY (`id_viaje`),
                          KEY `id_cliente` (`id_cliente`),
                          KEY `id_conductor` (`id_conductor`),
                          CONSTRAINT `viajes_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `usuarios` (`id_usuario`),
                          CONSTRAINT `viajes_ibfk_2` FOREIGN KEY (`id_conductor`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `viajes`
--

LOCK TABLES `viajes` WRITE;
/*!40000 ALTER TABLE `viajes` DISABLE KEYS */;
INSERT INTO `viajes` VALUES (1,'Mar del Plata Centro','Playa Grande','2026-11-06 15:28:48',3500.00,'FINALIZADO',1,2);
/*!40000 ALTER TABLE `viajes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'plataforma_uber'
--

--
-- Dumping routines for database 'plataforma_uber'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-16 20:27:58
