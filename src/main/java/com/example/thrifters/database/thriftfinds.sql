-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 07, 2025 at 08:14 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `thriftfinds`
--

-- --------------------------------------------------------

--
-- Table structure for table `inventory_tracking`
--

CREATE TABLE `inventory_tracking` (
  `tracking_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `change_type` enum('addition','removal') NOT NULL,
  `quantity` int(11) NOT NULL,
  `change_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `order_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `total_amount` decimal(10,2) DEFAULT 0.00,
  `status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `user_id`, `order_date`, `total_amount`, `status`) VALUES
(28, 7, '2025-01-07 19:04:46', 0.00, 'In Cart');

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `order_item_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `total_amount` decimal(10,2) DEFAULT 0.00,
  `image` varchar(255) DEFAULT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`order_item_id`, `order_id`, `product_id`, `quantity`, `price`, `total_amount`, `image`, `user_id`) VALUES
(1, 1, 2, 1, 250.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/Red_Ballet_Flats.png', 1),
(2, 1, 11, 1, 240.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/Crochet_Beanie_Cream.png', 1),
(3, 1, 16, 1, 324.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/Canvas_Bag_Brown.png', 1),
(4, 1, 17, 1, 180.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/Lapel_Short_Sleeve_Beige.png', 1),
(5, 1, 13, 1, 200.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/High_Waist_Denim_Short.png', 1),
(8, 1, 3, 1, 200.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/Fairylace_Maxi_Skirt.png', 1),
(9, 1, 12, 1, 465.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/Kids_Boots_Timberlands.png', 1),
(10, 1, 8, 1, 200.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/My_Little_Pony_PJs.png', 1),
(11, 1, 16, 1, 324.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/Canvas_Bag_Brown.png', 1),
(12, 1, 3, 1, 200.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/Fairylace_Maxi_Skirt.png', 1),
(13, 1, 6, 1, 345.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/Pink_Lace_Bag.png', 1),
(14, 1, 2, 1, 250.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/Red_Ballet_Flats.png', 1),
(15, 2, 1, 1, 175.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/Babydoll_Plaid_Lace.png', 2),
(22, 3, 3, 1, 200.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/Fairylace_Maxi_Skirt.png', 3),
(27, 8, 21, 1, 178.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/Denim_Skirt_A-line.png', 2),
(28, 8, 20, 1, 265.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/My_Melody_Kids_Clothes.png', 2),
(29, 8, 19, 1, 245.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/Fairy_Lace_Maxi_Skirt.png', 2),
(30, 8, 18, 1, 800.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/New_Balance_550.png', 2),
(41, 15, 17, 1, 180.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/Lapel_Short_Sleeve_Beige.png', 4),
(50, 21, 8, 1, 200.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/My_Little_Pony_PJs.png', 2),
(52, 26, 1, 1, 175.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/Babydoll_Plaid_Lace.png', 6),
(53, 27, 1, 1, 175.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/Babydoll_Plaid_Lace.png', 7),
(54, 28, 1, 1, 175.00, 0.00, 'file:/C:/Users/User/Desktop/ThriftFinds/OOP/images/Babydoll_Plaid_Lace.png', 7);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `product_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `size` varchar(20) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `stock_level` int(11) NOT NULL,
  `category` varchar(50) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `image` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_id`, `name`, `description`, `size`, `type`, `price`, `stock_level`, `category`, `created_at`, `updated_at`, `image`) VALUES
(1, 'Babydoll Plaid Lace', 'A stylish top', 'Small', 'Cotton', 175.00, 1, 'Women', '2025-01-06 01:37:31', '2025-01-06 01:37:31', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\Babydoll_Plaid_Lace.png'),
(2, 'Red Ballet Flats', NULL, '37', 'Leather', 250.00, 2, 'Women', '2025-01-06 01:38:21', '2025-01-06 01:38:21', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\Red_Ballet_Flats.png'),
(3, 'Fairylace Maxi Skirt', NULL, '27 inches', 'Cotton', 200.00, 3, 'Women', '2025-01-06 01:39:09', '2025-01-06 01:39:09', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\Fairylace_Maxi_Skirt.png'),
(4, 'Grey Milkmaid Top', NULL, 'Small', 'Cotton', 140.00, 3, 'Women', '2025-01-06 01:39:55', '2025-01-06 01:39:55', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\Grey_Milkmaid_Top.png'),
(5, 'Off-shoulder Knitted Top', NULL, 'Small', 'Cotton ', 230.00, 1, 'Women', '2025-01-06 01:40:45', '2025-01-06 01:40:45', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\Off-shoulder_Knitted_Top.png'),
(6, 'Pink Lace Bag', NULL, NULL, NULL, 345.00, 2, 'Others', '2025-01-06 11:35:06', '2025-01-06 11:35:06', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\Pink_Lace_Bag.png'),
(7, 'Adidas Originals Low C', NULL, '42', NULL, 780.00, 1, 'Men', '2025-01-06 11:36:19', '2025-01-06 11:36:19', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\Adidas_Originals_Low_C.png'),
(8, 'My Little Pony PJs', NULL, NULL, NULL, 200.00, 1, 'Children', '2025-01-06 11:36:52', '2025-01-06 11:36:52', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\My_Little_Pony_PJs.png'),
(9, 'Baggy Quad Cargo Pants', NULL, '30', NULL, 209.00, 1, 'Men', '2025-01-06 11:37:39', '2025-01-07 15:33:37', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\Baggy_Skat_Quad_Cargo_Pants.png'),
(10, 'Bluey Bingo Toddler Boys', NULL, 'Small', NULL, 168.00, 1, 'Children', '2025-01-06 11:38:47', '2025-01-06 11:38:47', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\Bluey_Bingo_Toddler_Boys.png'),
(11, 'Crochet Beanie Cream', NULL, NULL, NULL, 240.00, 1, 'Others', '2025-01-06 11:39:27', '2025-01-06 11:39:27', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\Crochet_Beanie_Cream.png'),
(12, 'Kids Boots Timberlands', NULL, NULL, NULL, 465.00, 1, 'Children', '2025-01-06 11:40:11', '2025-01-06 11:40:11', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\Kids_Boots_Timberlands.png'),
(13, 'High Waist Denim Short', NULL, NULL, NULL, 200.00, 1, 'Women', '2025-01-06 11:40:41', '2025-01-06 11:40:41', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\High_Waist_Denim_Short.png'),
(14, 'Winter Vintage Leather', NULL, NULL, NULL, 390.00, 1, 'Men', '2025-01-06 11:41:19', '2025-01-06 11:41:19', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\Winter_Vintage_Leather.png'),
(15, 'Casual Adidas Sneakers Grey', NULL, NULL, NULL, 660.00, 1, 'Men', '2025-01-06 11:42:04', '2025-01-06 11:42:04', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\Casual_Adidas_Sneakers_Grey.png'),
(16, 'Canvas Bag Brown', NULL, NULL, NULL, 324.00, 1, 'Others', '2025-01-06 11:42:44', '2025-01-06 11:42:44', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\Canvas_Bag_Brown.png'),
(17, 'Lapel Short Sleeve Beige', NULL, NULL, NULL, 180.00, 1, 'Men', '2025-01-06 11:43:17', '2025-01-06 11:43:17', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\Lapel_Short_Sleeve_Beige.png'),
(18, 'New Balance 550', NULL, NULL, NULL, 800.00, 1, 'Men', '2025-01-06 11:43:51', '2025-01-06 11:43:51', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\New_Balance_550.png'),
(19, 'Fairy Lace Maxi Skirt', NULL, NULL, NULL, 245.00, 1, 'Women', '2025-01-06 11:44:34', '2025-01-06 11:44:34', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\Fairy_Lace_Maxi_Skirt.png'),
(20, 'My Melody Kids Clothes', NULL, NULL, NULL, 265.00, 1, 'Children', '2025-01-06 11:45:05', '2025-01-06 11:45:05', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\My_Melody_Kids_Clothes.png'),
(21, 'Denim Skirt A-line', NULL, '24', NULL, 178.00, 1, 'Women', '2025-01-06 11:45:35', '2025-01-06 11:45:35', 'C:\\Users\\User\\Desktop\\ThriftFinds\\OOP\\images\\Denim_Skirt_A-line.png');

-- --------------------------------------------------------

--
-- Table structure for table `sales_reports`
--

CREATE TABLE `sales_reports` (
  `report_id` int(11) NOT NULL,
  `report_date` date NOT NULL,
  `total_sales` decimal(10,2) NOT NULL,
  `total_orders` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `role` enum('admin','customer') NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `email`, `role`, `address`, `created_at`, `updated_at`) VALUES
(1, 'thriftfinds', 'thriftfinders', 'thriftfinds.contact@gmail.com', 'admin', 'LSPU Los Banos', '2025-01-06 13:41:19', '2025-01-06 13:41:19'),
(2, 'jei707', 'kookie', 'cjsanchez1402@gmail.com', 'customer', 'LSPU Los Banos', '2025-01-06 13:42:17', '2025-01-07 09:45:18'),
(3, 'tester', 'testing', 'tester@example.com', 'customer', NULL, '2025-01-07 10:26:11', '2025-01-07 10:26:11'),
(4, 'example', 'example', 'example@gmail.com', 'customer', NULL, '2025-01-07 18:18:37', '2025-01-07 18:18:37'),
(5, 'example2', 'example', 'example2@example.com', 'customer', NULL, '2025-01-07 18:43:19', '2025-01-07 18:43:19'),
(6, '12345', '12345', '12345@gmail.com', 'customer', NULL, '2025-01-07 18:59:56', '2025-01-07 18:59:56'),
(7, '123', '123', '123', 'customer', NULL, '2025-01-07 19:01:25', '2025-01-07 19:01:25');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `inventory_tracking`
--
ALTER TABLE `inventory_tracking`
  ADD PRIMARY KEY (`tracking_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`order_item_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `product_id` (`product_id`),
  ADD KEY `fk_user_id` (`user_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`);

--
-- Indexes for table `sales_reports`
--
ALTER TABLE `sales_reports`
  ADD PRIMARY KEY (`report_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `inventory_tracking`
--
ALTER TABLE `inventory_tracking`
  MODIFY `tracking_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `order_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=55;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `sales_reports`
--
ALTER TABLE `sales_reports`
  MODIFY `report_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
