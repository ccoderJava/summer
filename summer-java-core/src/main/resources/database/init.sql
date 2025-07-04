drop table t_alarm_event if exists;
create table t_alarm_event
(
    event_id     bigint      not null primary key comment '主键',
    main_phase   varchar(32) not null comment '主事件阶段',
    sub_phase    varchar(32) not null comment '子事件阶段',
    event_key    varchar(32) not null comment '事件key,与业务关联单号',
    event_status varchar(8)  not null comment '事件状态',
    event_seq    varchar(50) null comment '事件流水号',
    extension    longtext null comment '扩展参数',
    is_deleted   int         not null default 0 comment '是否删除 0-否 1-是',
    create_time  datetime    not null default current_timestamp comment'创建事件',
    update_time  date_time   not null on update current_timestamp comment '更新时间',
    unique key uk_groups (main_phase,sub_phase,event_key),
    index        idx_status (event_status),
    index        idx_create (create_time)
) engine=InnoDB default charset=uutf8mb4;