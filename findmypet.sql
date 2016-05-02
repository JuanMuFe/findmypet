-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 02-05-2016 a las 15:42:58
-- Versión del servidor: 10.1.13-MariaDB
-- Versión de PHP: 5.5.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `findmypet`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `announcement`
--

CREATE TABLE `announcement` (
  `id` int(11) NOT NULL,
  `description` varchar(200) NOT NULL,
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `announcement`
--

INSERT INTO `announcement` (`id`, `description`, `date`, `id_user`) VALUES
(1, 'Anuncio de una protectora de animales en Viladecans.', '2016-04-29 14:13:04', 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `city`
--

CREATE TABLE `city` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `postal_code` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `city`
--

INSERT INTO `city` (`id`, `name`, `postal_code`) VALUES
(1, 'Viladecans', '08840'),
(2, 'Gavà', '08850');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `city_province`
--

CREATE TABLE `city_province` (
  `id_city_province` int(11) NOT NULL,
  `id_city` int(11) NOT NULL,
  `id_province` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `city_province`
--

INSERT INTO `city_province` (`id_city_province`, `id_city`, `id_province`) VALUES
(1, 1, 8),
(2, 2, 8);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `notification`
--

CREATE TABLE `notification` (
  `id` int(11) NOT NULL,
  `active` tinyint(1) NOT NULL,
  `description` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `notification`
--

INSERT INTO `notification` (`id`, `active`, `description`) VALUES
(1, 1, 'Esto es una notificación para avisar del encuentro de un animal.');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `owner`
--

CREATE TABLE `owner` (
  `id` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `nif` varchar(9) NOT NULL,
  `birthdate` datetime NOT NULL,
  `registerdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `phone_number` varchar(15) NOT NULL,
  `address` varchar(100) NOT NULL,
  `entry_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `drop_out_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `id_city_province` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `owner`
--

INSERT INTO `owner` (`id`, `id_user`, `name`, `firstname`, `surname`, `nif`, `birthdate`, `registerdate`, `phone_number`, `address`, `entry_date`, `drop_out_date`, `id_city_province`) VALUES
(1, 3, 'Juan', 'Muñoz', 'Fernandez', '53314249L', '1994-03-18 00:00:00', '2016-04-29 00:00:00', '722189403', 'c/ Angel Arañó, Nº 74, 3º 4ª Derecha', '2016-04-29 00:00:00', '0000-00-00 00:00:00', 1),
(2, 5, 'Jordi', 'Catasus', 'Lajas', '53314248K', '1994-04-19 00:00:00', '2016-04-29 00:00:00', '685923487', 'C/ Indústria Nº 86, 4º 6ª', '2016-04-29 00:00:00', '0000-00-00 00:00:00', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `park`
--

CREATE TABLE `park` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `address` varchar(100) NOT NULL,
  `id_city_province` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `park`
--

INSERT INTO `park` (`id`, `name`, `address`, `id_city_province`) VALUES
(1, 'Pipican Torrente', 'Av. Torrente Ballester', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pet`
--

CREATE TABLE `pet` (
  `id` int(11) NOT NULL,
  `id_owner` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `race` varchar(50) NOT NULL,
  `image` varchar(200) NOT NULL,
  `description` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `pet`
--

INSERT INTO `pet` (`id`, `id_owner`, `name`, `race`, `image`, `description`) VALUES
(1, 1, 'Rufi', 'Yorkshire terrier', 'img/rufi.jpg', 'Un perro verde'),
(2, 2, 'Jambo', 'Mastín Azúl', 'img/jambo.jpg', 'Un mastín azul con cara de JRambo');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `profile`
--

CREATE TABLE `profile` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `profile`
--

INSERT INTO `profile` (`id`, `name`) VALUES
(1, 'admin'),
(2, 'normal');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `province`
--

CREATE TABLE `province` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `province`
--

INSERT INTO `province` (`id`, `name`) VALUES
(1, 'Araba/Álava'),
(2, 'Albacete'),
(3, 'Alicante/Alacant'),
(4, 'Almeria'),
(5, 'Ávila'),
(6, 'Badajoz'),
(7, 'Balears(Illes)'),
(8, 'Barcelona'),
(9, 'Burgos'),
(10, 'Cáceres'),
(11, 'Cádiz'),
(12, 'Castellón/Castelló'),
(13, 'Ciudad Real'),
(14, 'Córdoba'),
(15, 'Coruña (A)'),
(16, 'Cuenca'),
(17, 'Girona'),
(18, 'Granada'),
(19, 'Guadalajara'),
(20, 'Gipuzkoa'),
(21, 'Huelva'),
(22, 'Huesca'),
(23, 'Jaén'),
(24, 'León'),
(25, 'Lleida'),
(26, 'Rioja (La)'),
(27, 'Lugo'),
(28, 'Madrid'),
(29, 'Malaga'),
(30, 'Murcia'),
(31, 'Navarra'),
(32, 'Ourense'),
(33, 'Asturias'),
(34, 'Palencia'),
(35, 'Palmas (Las)'),
(42, 'Pontevedra'),
(43, 'Salamanca'),
(44, 'Santa Cruz de Tenerife'),
(45, 'Cantabria'),
(46, 'Segovia'),
(47, 'Sevilla'),
(48, 'Soria'),
(49, 'Tarragona'),
(50, 'Teruel'),
(51, 'Toledo'),
(52, 'Valencia/València'),
(53, 'Valladolid'),
(54, 'Bizkaia'),
(55, 'Zamora'),
(56, 'Zaragoza'),
(57, 'Ceuta'),
(58, 'Melilla');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `report`
--

CREATE TABLE `report` (
  `id` int(11) NOT NULL,
  `id_owner` int(11) NOT NULL,
  `id_pet` int(11) NOT NULL,
  `entry_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `location` varchar(50) NOT NULL,
  `extra` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `report`
--

INSERT INTO `report` (`id`, `id_owner`, `id_pet`, `entry_date`, `location`, `extra`) VALUES
(1, 1, 1, '2016-04-29 00:00:00', '59.97082369999999;23.773905300000024', 'NULL');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `id_profile` int(11) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `pswd` varchar(200) NOT NULL,
  `email` varchar(100) NOT NULL,
  `active` tinyint(1) NOT NULL,
  `image` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `user`
--

INSERT INTO `user` (`id`, `id_profile`, `user_name`, `pswd`, `email`, `active`, `image`) VALUES
(3, 1, 'admin', 'admin', 'admin@gmail.com', 1, 'img/admin.jpg'),
(4, 2, 'juan', 'juan', 'juan@gmail.com', 1, 'img/juan.jpg'),
(5, 2, 'jordi', 'jordi', 'jordi@gmail.com', 1, 'img/jordi.jpg');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user_notification`
--

CREATE TABLE `user_notification` (
  `id` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `id_notification` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `user_notification`
--

INSERT INTO `user_notification` (`id`, `id_user`, `id_notification`) VALUES
(1, 3, 1),
(2, 4, 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `announcement`
--
ALTER TABLE `announcement`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_user` (`id_user`);

--
-- Indices de la tabla `city`
--
ALTER TABLE `city`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `city_province`
--
ALTER TABLE `city_province`
  ADD PRIMARY KEY (`id_city_province`),
  ADD KEY `id_city` (`id_city`),
  ADD KEY `id_province` (`id_province`),
  ADD KEY `id_province_2` (`id_province`);

--
-- Indices de la tabla `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `owner`
--
ALTER TABLE `owner`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_user` (`id_user`),
  ADD KEY `id_city` (`id_city_province`);

--
-- Indices de la tabla `park`
--
ALTER TABLE `park`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_city` (`id_city_province`);

--
-- Indices de la tabla `pet`
--
ALTER TABLE `pet`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_owner` (`id_owner`);

--
-- Indices de la tabla `profile`
--
ALTER TABLE `profile`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `province`
--
ALTER TABLE `province`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `report`
--
ALTER TABLE `report`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_owner` (`id_owner`),
  ADD KEY `id_pet` (`id_pet`);

--
-- Indices de la tabla `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_profile` (`id_profile`);

--
-- Indices de la tabla `user_notification`
--
ALTER TABLE `user_notification`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_user` (`id_user`),
  ADD KEY `id_notification` (`id_notification`),
  ADD KEY `id_user_2` (`id_user`),
  ADD KEY `id_notification_2` (`id_notification`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `announcement`
--
ALTER TABLE `announcement`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT de la tabla `city`
--
ALTER TABLE `city`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT de la tabla `city_province`
--
ALTER TABLE `city_province`
  MODIFY `id_city_province` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT de la tabla `notification`
--
ALTER TABLE `notification`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT de la tabla `owner`
--
ALTER TABLE `owner`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT de la tabla `park`
--
ALTER TABLE `park`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT de la tabla `pet`
--
ALTER TABLE `pet`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT de la tabla `profile`
--
ALTER TABLE `profile`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT de la tabla `province`
--
ALTER TABLE `province`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;
--
-- AUTO_INCREMENT de la tabla `report`
--
ALTER TABLE `report`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT de la tabla `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT de la tabla `user_notification`
--
ALTER TABLE `user_notification`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `announcement`
--
ALTER TABLE `announcement`
  ADD CONSTRAINT `announcement_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `city_province`
--
ALTER TABLE `city_province`
  ADD CONSTRAINT `city_province_ibfk_1` FOREIGN KEY (`id_province`) REFERENCES `province` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `city_province_ibfk_2` FOREIGN KEY (`id_city`) REFERENCES `city` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `owner`
--
ALTER TABLE `owner`
  ADD CONSTRAINT `owner_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `owner_ibfk_2` FOREIGN KEY (`id_city_province`) REFERENCES `city_province` (`id_city_province`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `park`
--
ALTER TABLE `park`
  ADD CONSTRAINT `park_ibfk_1` FOREIGN KEY (`id_city_province`) REFERENCES `city_province` (`id_city_province`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `pet`
--
ALTER TABLE `pet`
  ADD CONSTRAINT `pet_ibfk_1` FOREIGN KEY (`id_owner`) REFERENCES `owner` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `report`
--
ALTER TABLE `report`
  ADD CONSTRAINT `report_ibfk_1` FOREIGN KEY (`id_owner`) REFERENCES `owner` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `report_ibfk_2` FOREIGN KEY (`id_pet`) REFERENCES `pet` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`id_profile`) REFERENCES `profile` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `user_notification`
--
ALTER TABLE `user_notification`
  ADD CONSTRAINT `user_notification_ibfk_1` FOREIGN KEY (`id_notification`) REFERENCES `notification` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `user_notification_ibfk_2` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
