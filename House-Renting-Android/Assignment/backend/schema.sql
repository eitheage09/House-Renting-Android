 CREATE DATABASE IF NOT EXISTS house-renting DEFAULT CHARACTER SET utf8mb4;
 USE house-renting;
 
 CREATE TABLE IF NOT EXISTS users (
     email VARCHAR(255) PRIMARY KEY,
     password VARCHAR(255) NOT NULL,
     name VARCHAR(255) DEFAULT '',
     photo LONGBLOB,
     gender VARCHAR(50) DEFAULT '',
     age INT DEFAULT 0,
     birthday BIGINT DEFAULT 0,
     mobile VARCHAR(50) DEFAULT '',
     status VARCHAR(50) DEFAULT 'online',
     last_login_time BIGINT DEFAULT 0,
     fcm_token VARCHAR(255) DEFAULT ''
 );
 
 CREATE TABLE IF NOT EXISTS rooms (
     room_id VARCHAR(255) PRIMARY KEY,
     room_name VARCHAR(255) DEFAULT '',
     room_place VARCHAR(255) DEFAULT '',
     room_detail TEXT,
     photo_json TEXT,
     room_size DOUBLE DEFAULT 0,
     room_type VARCHAR(50) DEFAULT '',
     num_of_bed INT DEFAULT 0,
     num_of_bath INT DEFAULT 0,
     home_amenities TEXT,
     room_price DOUBLE DEFAULT 0,
     latitude DOUBLE DEFAULT 0,
     longitude DOUBLE DEFAULT 0,
     user_id VARCHAR(255) DEFAULT ''
 );
 
 CREATE TABLE IF NOT EXISTS chats (
     chat_id VARCHAR(255) PRIMARY KEY,
     chat_room_id VARCHAR(255) DEFAULT '',
     sender_id VARCHAR(255) DEFAULT '',
     message TEXT,
     message_time BIGINT DEFAULT 0,
     is_read BOOLEAN DEFAULT FALSE,
     image LONGBLOB
 );
 
 CREATE TABLE IF NOT EXISTS chat_rooms (
     chat_room_id VARCHAR(255) PRIMARY KEY,
     last_sender VARCHAR(255) DEFAULT '',
     last_message TEXT,
     last_message_time BIGINT DEFAULT 0,
     user_ids TEXT,
     is_read BOOLEAN DEFAULT FALSE
 );
 
 CREATE TABLE IF NOT EXISTS favorites (
     favorite_id VARCHAR(255) PRIMARY KEY,
     user_id VARCHAR(255) DEFAULT '',
     room_id VARCHAR(255) DEFAULT ''
 );
