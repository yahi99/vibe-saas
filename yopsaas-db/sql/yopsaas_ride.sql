use yopsaas;

drop table yopsaas_ride_order;
drop table yopsaas_ride_order_ext;
drop table yopsaas_ride_order_transaction_history;

create table yopsaas_ride_order(
  `ride_order_id` bigint(20) NOT NULL  DEFAULT '0',
  `yc_order_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'yc order_id',
  `product_type_id` int(11) NOT NULL DEFAULT '0' COMMENT '产品的ID',
  `fixed_product_id` int(11) NOT NULL DEFAULT '0' COMMENT '固定价格的产品ID',
  `is_asap` tinyint(4) NOT NULL DEFAULT '0' COMMENT  '是否是即时用订单 0:否 1:是',
  `source` int(11) not null DEFAULT '0' COMMENT  '下单渠道',
  `platform` tinyint(4) not null DEFAULT '0' COMMENT '下单平台',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '订单状态',
  `rc_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '当前订单风控状态，0:正常状态，1:未处理状态,2:已处理状态',
  `end_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否强制结束',
  `abnormal_mark` tinyint(4) NOT NULL DEFAULT '0' COMMENT '订单异常状态',
  `flag` bigint(20) NOT NULL DEFAULT '0' COMMENT '按位保存',

  `account_id` bigint(20) not null default 0 COMMENT '冗余账户id',
  `user_id` bigint(20) NOT NULL default 0 COMMENT '用户ID',
  `user_phone` varchar(20) not null DEFAULT '' COMMENT '用户的电话号码, 国际标准, 电话号码最多15个数字',
  `passenger_name` varchar(32) not null DEFAULT '' COMMENT '乘客的名称',
  `passenger_phone` varchar(20) not null DEFAULT '' COMMENT '乘客的电话号码, 国际标准, 电话号码最多15个数字',
  `corporate_id` bigint(20) NOT NULL default 0 COMMENT '企业ID',
  `corporate_dept_id` int(11) NOT NULL DEFAULT '0' COMMENT '企业账户关系id，即所属的最小账户级别id',

  `city` varchar(16) not null DEFAULT '' COMMENT '订单出发地所在城市(区域)',

  `reason_id`  int(11) NOT NULL DEFAULT '0' COMMENT '订单取消原因',
  `flight_number` varchar(20) not null DEFAULT '' COMMENT '航班号, 需要转换成大写字母',

  `create_time` int(11) NOT NULL default 0 COMMENT '创建订单的时间',
  `update_time` int(11) NOT NULL default 0 COMMENT '创建修改的时间',
  `init_time` int(11) NOT NULL default 0 COMMENT   '正确下单时间',
  `select_car_time` int(11) NOT NULL default 0 COMMENT  '允许派车的时间',
  `arrival_time` int(11) NOT NULL DEFAULT '0' COMMENT '就位的时间',
  `cancel_time` int(11) NOT NULL DEFAULT '0' COMMENT '取消时间',

  `car_id` int(11) NOT NULL DEFAULT '0' COMMENT '分配的车辆的ID',
  `car_type_id` int(11) NOT NULL DEFAULT '0' COMMENT '分配的车型的ID',
  `car_type_ids` varchar(50) NOT NULL DEFAULT '' COMMENT 'mixed car_type support',
  `car_type` varchar(64) NOT NULL DEFAULT '' COMMENT '分配的车型的名称, ',
  `car_brand` varchar(64) NOT NULL DEFAULT '' COMMENT '分配的车的牌子',

  `driver_id` int(11) NOT NULL DEFAULT '0' COMMENT '分配的司机的ID',
  `driver_phone` varchar(20) NOT NULL DEFAULT '' COMMENT '分配的司机的手机号码',
  `driver_name` varchar(32) NOT NULL DEFAULT ''COMMENT '分配的司机的名称',
  `vehicle_number` varchar(32) NOT NULL DEFAULT '' COMMENT '分配的车牌号码',

  `expect_start_time` int(11) NOT NULL DEFAULT '0' COMMENT '订单期望开始的时间',
  `expect_end_time` int(11) NOT NULL DEFAULT '0' COMMENT '预计结束时间',
  `time_length` int(11) NOT NULL DEFAULT '0' COMMENT  '订单预计的时长',

  `start_time` int(11) NOT NULL DEFAULT '0' COMMENT '订单实际开始的时间',
  `end_time` int(11) NOT NULL DEFAULT '0' COMMENT '订单实际结束的时间',
  `confirm_time` int(11) NOT NULL DEFAULT 0 COMMENT '车辆接单的时间',

  `start_position` varchar(255) NOT NULL DEFAULT '' COMMENT '开始的地点',
  `start_address` varchar(255) NOT NULL DEFAULT '' COMMENT '开始的详细地址备注',
  `end_position` varchar(255) NOT NULL DEFAULT '' COMMENT '到达的地点',
  `end_address` varchar(255) NOT NULL DEFAULT '' COMMENT '到达的详细地址备注',

  `expect_start_latitude` double NOT NULL default 0 COMMENT '期望的开始的纬度',
  `expect_start_longitude` double NOT NULL default 0 COMMENT '期望开始的经度',
  `expect_end_latitude` double NOT NULL default 0 COMMENT '期望到达的纬度',
  `expect_end_longitude` double NOT NULL default 0 COMMENT '期望到达的经度',

  `start_latitude` double NOT NULL DEFAULT '0' COMMENT '实际开始的纬度',
  `start_longitude` double NOT NULL DEFAULT '0' COMMENT '实际开始的经度',
  `end_latitude` double NOT NULL DEFAULT '0' COMMENT '实际到达的纬度',
  `end_longitude` double NOT NULL DEFAULT '0' COMMENT '实际到达的经度',

  `payment` int(10) not null DEFAULT 0 comment '支付方式',
  `pay_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '付款状态 1:未付款 2:部分付款 3:已付款',
  `pay_time` int(11) NOT NULL DEFAULT '0' COMMENT '支付时间',
  `first_recharge_transaction_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '首次支付时的事务ID',
  `first_recharge_amount` decimal(10,2) NOT NULL DEFAULT 0 ,

  `coupon_member_id` bigint(20) NOT NULL default 0 COMMENT '优惠券ID',
  `coupon_name` varchar(32) NOT NULL DEFAULT '',
  `coupon_type` tinyint(4) NOT NULL DEFAULT '0',
  `coupon_facevalue` decimal(8,2) NOT NULL DEFAULT '0.00',

  `discount` decimal(8,2) NOT NULL DEFAULT '0.00',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT  '当前订单的版本',
  `fee_version` int(11) NOT NULL DEFAULT '0' COMMENT '计费版本号',

  `refund_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '退款状态 0:无需退款 1:待退款 2:退款中 3:已退款 -1:退款失败',
  `pay_method` tinyint(4) NOT NULL DEFAULT '0' COMMENT '支付方法 1:预收 2:预授权',
  `balance_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT  '结算状态:1, 结算中 2,结算成功 3,结算失败',
  `payable` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否允许支付，1，可以，2不可以',
  `total_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '订单的总金额,服务结束后才能使用，服务结束前用 predict_amount',
  `deposit` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '用户实际已经支付的费用, 是现金，不包含信用',
  `loan_in_credit` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pay_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '当前实际应付金额',
  `min_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT  '最低收费金额',
  `origin_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '原始费用(不含优惠卷)',
  `origin_sharing_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '司机统计金额',
  `sharing_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '服务结束后，给司机的费用',

  `wx_order_sn` varchar(63) NOT NULL default '' COMMENT '微信订单编号',
  `pay_id` varchar(63) NOT NULL DEFAULT '' COMMENT '微信付款编号',
  `refund_amount` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '实际退款金额，（有可能退款金额小于实际支付金额）',
  `refund_type` varchar(63) NOT NULL DEFAULT 0 COMMENT '退款方式',
  `refund_content` varchar(127) NOT NULL DEFAULT '' COMMENT '退款备注',
  `refund_time` int(11) NOT NULL DEFAULT 0 COMMENT '退款时间',

  `actual_time_length` int(11) NOT NULL DEFAULT '0' COMMENT  '实际使用时长',
  `dependable_distance` int(11) NOT NULL DEFAULT '0',
  `mileage` int(11) NOT NULL DEFAULT 0 COMMENT  '司机手动输入里程数',
  `system_distance` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '系统记录的里程数',

  `alitongxin_secret_no_x` varchar(32) NOT NULL DEFAULT '' COMMENT '阿里小号',
  `alitongxin_subs_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '阿里小号ID',
  `alitongxin_status` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '阿里小号状态未绑定0，绑定1，解绑成功2，解绑失败3',

  `passenger_session_id` varchar(32) NOT NULL DEFAULT '' COMMENT '乘客聊天会话',
  `last_operator` varchar(50) NOT NULL default '',
  PRIMARY KEY (`ride_order_id`),
  KEY `yc_order_id` (`yc_order_id`),
  KEY `idx_user_id_create_time_status` (`user_id`,`create_time`,`status`),
  KEY `idx_user_id_status` (`user_id`,`status`),
  KEY `idx_uid_endtime` (`user_id`,`end_time`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_update_time` (`update_time`),
  KEY `idx_end_pay_status` (`end_time`,`status`,`pay_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网约车订单表';

create table yopsaas_ride_order_ext(
  `ride_order_id` bigint(20) NOT NULL,
  `operator_id` int(11) NOT NULL DEFAULT '0' COMMENT '操作人的ID',
  `user_type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '用户类型',
  `sms` int(10) NOT NULL default 0 COMMENT '是否给乘客发短信',

  `create_order_longitude` double NOT NULL DEFAULT '0' COMMENT '下单时乘客端的经度',
  `create_order_latitude` double NOT NULL DEFAULT '0' COMMENT '下单时乘客端的纬度',

  `confirm_latitude` double NOT NULL DEFAULT 0 COMMENT '车辆接单的纬度',
  `confirm_longitude` double NOT NULL DEFAULT 0 COMMENT '车辆接单的经度',
  `arrive_latitude` double NOT NULL DEFAULT '0' COMMENT '司机就位的纬度',
  `arrive_longitude` double NOT NULL DEFAULT '0' COMMENT '司机就位的经度',

  `src_city_name` varchar(100) NOT NULL DEFAULT '' ,
  `dst_city_name` varchar(100) NOT NULL DEFAULT '' ,
  `dest_city` varchar(16) NOT NULL DEFAULT '' COMMENT '下车地点所在城市',

  `dispatch_driver_ids` varchar(1024) NOT NULL DEFAULT '' COMMENT '预约收藏司机订单，派单司机ID',
  `change_driver_reason_id` int(11) NOT NULL DEFAULT '0' COMMENT '改派司机的原因，只记录第一次, 1.司机原因， 2. 客户原因',
  `before_cancel_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '取消前状态',

  `app_version` varchar(32) NOT NULL default '' comment '下单客户端版本信息',
  `driver_version` varchar(64) NOT NULL DEFAULT '' COMMENT '司机端版本号',

  `balance_time` int(11) NOT NULL DEFAULT '0',
  `balance_result` tinyint(4) NOT NULL DEFAULT '0' COMMENT '支付失败状态: 0:支付成功,1:未绑卡支付失败,2:支付失败',
  `preauth_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '预授权状态: 1:预授权 2:预授权完成中 3:预授权完成成功 -1:预授权完成失败',

  `extra_amount` decimal(8,2) NOT NULL DEFAULT '0.00',
  `predict_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '预计费用',
  `night_amount` decimal(10,2) NOT NULL default 0 COMMENT '夜间行车费',
  `driver_amount` decimal(8,2) NOT NULL DEFAULT '0.00' ,
  `predict_origin_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '原始预计金额，不含优惠',
  `predict_pay_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '预计应收金额，不含优惠',
  `additional_time_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '附加的',
  `highway_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT  '高速费',
  `parking_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '停车费',
  `addons_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '其他费用',
  `addons_amount_src` varchar(100) NOT NULL DEFAULT '' COMMENT '产生其他费用的原因',
  `other_amount` decimal(8,2) NOT NULL DEFAULT '0.00',

  `runtime` int(11) NOT NULL DEFAULT 0 COMMENT '司机手动输入运行时间',
  `total_distance` int(11) NOT NULL DEFAULT '0' COMMENT '总距离，单位为米',

  `deadhead_distance` int(11) NOT NULL DEFAULT '0' COMMENT '空驶里程(米)',
  `is_night` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否含有夜间服务 0:否 1:是',

  `regulatepan_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '用户调整后的费用',
  `regulatedri_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '司机调整后的费用',
  `regulatepan_reason` varchar(20) NOT NULL DEFAULT '' COMMENT '用户费用调整原因',
  `regulatedri_reason` varchar(20) NOT NULL DEFAULT '' COMMENT '司机费用调整原因',
  `regulate_amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '调整金额.若是负数，则表示返回用户的钱。',

  `estimate_snap` varchar(4096) NOT NULL DEFAULT '' COMMENT '订单预估快照',   -- to cache
  `app_msg` text COMMENT '客户端消息',                                    -- to driver order 冗余表
  `comment` text NOT NULL COMMENT '订单的留言, CRM 客服，添加的留言',   -- erp专用
  `ip` varchar(40) NOT NULL DEFAULT '' COMMENT '用户下单时的IP',
  `order_port` int(11) NOT NULL DEFAULT '0' COMMENT '订单下单端口',
  `update_time` int(11) NOT NULL DEFAULT 0 COMMENT '更新订单的时间',
  PRIMARY KEY (`ride_order_id`),
  KEY `idx_update_time` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网约车订单扩展表';

create table yopsaas_ride_order_transaction_history(
  `ride_order_transaction_history_id` bigint(20) NOT NULL  default 0  COMMENT '主键ID',
  `account_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '账户ID',
  `ride_order_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '订单ID',
  `recharge_transaction_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '支付事务ID 余额和信用相关交易时置NULL',
  `consumer_id` bigint(20) NOT NULL DEFAULT '0',
  `operation` tinyint(4) NOT NULL DEFAULT '0' COMMENT '操作 1:预收 2:预收退款 3:预授权 4:预授权完成 5:人工退款 6:未消费退款 7:取消订单退款 8:结账收款',
  `pay_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '支付方式 退款和预授权完成时置NULL 值定义参考',
  `pay_source` smallint(6) NOT NULL DEFAULT '0' COMMENT '支付来源。值定义参考',
  `transaction_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '交易类型 1:订单交易',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '消费状态 -10:失败 0:初始化 10:验证通过 20:部分付款 30:成功',
  `amount` int(11) NOT NULL DEFAULT '0' COMMENT '交易金额(单位：分) 正负区分增加和扣减',
  `paid_amount` int(11) NOT NULL DEFAULT '0' COMMENT '已付金额',
  `operator` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人 记录操作人用户名，程序自动执行时记system',
  `comment` varchar(100) NOT NULL DEFAULT '' COMMENT '备注(如：退款原因描述)',
  `create_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` int(11) NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`ride_order_transaction_history_id`),
  KEY `ride_order_id` (`ride_order_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_update_time` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网约车订单交易表';