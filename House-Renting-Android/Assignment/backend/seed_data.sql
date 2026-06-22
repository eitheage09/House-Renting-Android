 -- ===================================================
 -- 租房 App 测试数据
 -- 使用前确保已执行 schema.sql
 -- mysql -u root -p < backend/seed_data.sql
 -- ===================================================
 
 USE rent_house;
 
 -- ==================== 用户数据 ====================
 INSERT INTO users (email, password, name, gender, age, mobile, status, last_login_time) VALUES
 ('alice@test.com', '123456', 'Alice Wang', 'Female', 25, '13800138001', 'online', UNIX_TIMESTAMP() * 1000),
 ('bob@test.com', '123456', 'Bob Li', 'Male', 28, '13800138002', 'offline', UNIX_TIMESTAMP() * 1000 - 3600000),
 ('charlie@test.com', '123456', 'Charlie Zhang', 'Male', 30, '13800138003', 'online', UNIX_TIMESTAMP() * 1000),
 ('diana@test.com', '123456', 'Diana Chen', 'Female', 24, '13800138004', 'online', UNIX_TIMESTAMP() * 1000),
 ('eva@test.com', '123456', 'Eva Liu', 'Female', 27, '13800138005', 'offline', UNIX_TIMESTAMP() * 1000 - 7200000);
 
 -- ==================== 房源数据 ====================
 INSERT INTO rooms (room_id, room_name, room_place, room_detail, photo_json, room_size, room_type, num_of_bed, num_of_bath, home_amenities, room_price, latitude, longitude, user_id) VALUES
 ('room001', '阳光单间 - 近地铁站', '广州市天河区中山大道100号', '精装修单间，采光好，配有空调、洗衣机、WiFi。步行5分钟到地铁站，周边生活便利。', '[]', 25.0, 'Single', 1, 1, '["Wi-Fi","Air-Conditioner","Washing Machine"]', 1800.0, 23.1291, 113.2644, 'alice@test.com'),
 ('room002', '舒适主卧 - 带独立卫浴', '广州市番禺区大学城旁', '小区环境优美，主卧带独立卫生间和阳台。适合学生或上班族。', '[]', 30.0, 'Master', 1, 1, '["Wi-Fi","Air-Conditioner","TV"]', 2200.0, 23.0587, 113.3920, 'bob@test.com'),
 ('room003', '温馨次卧 - 合租', '深圳市南山区科技园附近', '次卧出租，室友均为IT从业者，素质高。小区安保完善，交通便利。', '[]', 18.0, 'Medium', 1, 1, '["Wi-Fi","Air-Conditioner","Washing Machine"]', 2500.0, 22.5431, 113.9530, 'alice@test.com'),
 ('room004', '整租一居室', '广州市海珠区广州塔旁', '整租一居室公寓，高层景观好。全新装修，家具家电齐全，拎包入住。', '[]', 45.0, 'Master', 1, 1, '["Wi-Fi","Air-Conditioner","TV","Washing Machine"]', 3500.0, 23.1065, 113.3249, 'charlie@test.com'),
 ('room005', '单人公寓 - 独立厨房', '深圳市福田区CBD', '精品单人公寓，带独立厨房和卫生间。适合白领单身人士。', '[]', 35.0, 'Single', 1, 1, '["Wi-Fi","Air-Conditioner","TV","Washing Machine"]', 3800.0, 22.5215, 114.0579, 'diana@test.com'),
 ('room006', '豪华主卧 - 大户型', '广州市白云区万达广场旁', '200平米大平层中的豪华主卧，带衣帽间和独立卫浴。小区有游泳池和健身房。', '[]', 40.0, 'Master', 1, 1, '["Wi-Fi","Air-Conditioner","TV","Washing Machine"]', 3000.0, 23.1580, 113.2665, 'eva@test.com'),
 ('room007', '经济单间 - 女生优先', '广州市越秀区北京路', '经济实惠单间，位于老城区，生活气息浓厚。仅限女生，室友均为女生。', '[]', 15.0, 'Single', 1, 1, '["Wi-Fi","Washing Machine"]', 1200.0, 23.1280, 113.2660, 'diana@test.com'),
 ('room008', '招合租室友 - 宠物友好', '深圳市宝安区壹方城附近', '三房两厅找合租，可养宠物。小区环境好，近地铁。', '[]', 20.0, 'Medium', 1, 1, '["Wi-Fi","Air-Conditioner","TV","Washing Machine"]', 2000.0, 22.5549, 113.8937, 'bob@test.com');
 
 -- ==================== 收藏数据 ====================
 INSERT INTO favorites (favorite_id, user_id, room_id) VALUES
 ('fav001', 'alice@test.com', 'room003'),
 ('fav002', 'alice@test.com', 'room005'),
 ('fav003', 'bob@test.com', 'room001'),
 ('fav004', 'charlie@test.com', 'room005'),
 ('fav005', 'charlie@test.com', 'room002'),
 ('fav006', 'diana@test.com', 'room001'),
 ('fav007', 'eva@test.com', 'room004'),
 ('fav008', 'eva@test.com', 'room006');
 
 -- ==================== 聊天室数据 ====================
 INSERT INTO chat_rooms (chat_room_id, last_sender, last_message, last_message_time, user_ids, is_read) VALUES
 ('cr001', 'alice@test.com', '好的，那明天下午见', UNIX_TIMESTAMP() * 1000 - 600000, '["alice@test.com","bob@test.com"]', true),
 ('cr002', 'charlie@test.com', '请问房子还在吗？', UNIX_TIMESTAMP() * 1000 - 1800000, '["charlie@test.com","alice@test.com"]', false),
 ('cr003', 'diana@test.com', '价格可以再商量吗', UNIX_TIMESTAMP() * 1000 - 3600000, '["diana@test.com","bob@test.com"]', true);
 
 -- ==================== 聊天消息数据 ====================
 INSERT INTO chats (chat_id, chat_room_id, sender_id, message, message_time, is_read) VALUES
 ('chat001', 'cr001', 'alice@test.com', '你好，我对你的房间感兴趣', UNIX_TIMESTAMP() * 1000 - 3600000, true),
 ('chat002', 'cr001', 'bob@test.com', '你好，欢迎来看房，这周末有空吗', UNIX_TIMESTAMP() * 1000 - 3000000, true),
 ('chat003', 'cr001', 'alice@test.com', '周末有空，周六下午3点可以吗', UNIX_TIMESTAMP() * 1000 - 2400000, true),
 ('chat004', 'cr001', 'bob@test.com', '可以的，周六下午3点见', UNIX_TIMESTAMP() * 1000 - 1800000, true),
 ('chat005', 'cr001', 'alice@test.com', '好的，那明天下午见', UNIX_TIMESTAMP() * 1000 - 600000, true),
 ('chat006', 'cr002', 'charlie@test.com', '请问房子还在吗？', UNIX_TIMESTAMP() * 1000 - 1800000, false),
 ('chat007', 'cr003', 'diana@test.com', '你好，我想看看那个单间', UNIX_TIMESTAMP() * 1000 - 7200000, true),
 ('chat008', 'cr003', 'bob@test.com', '随时可以来看，今天有空吗', UNIX_TIMESTAMP() * 1000 - 5400000, true),
 ('chat009', 'cr003', 'diana@test.com', '今天有点忙，周末可以吗', UNIX_TIMESTAMP() * 1000 - 4500000, true),
 ('chat010', 'cr003', 'diana@test.com', '价格可以再商量吗', UNIX_TIMESTAMP() * 1000 - 3600000, true);
