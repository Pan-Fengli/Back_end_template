truncate table audit;
truncate table notification;
truncate table user_comment_like;
truncate table user_discuss_like;
truncate table user_interest;
truncate table user_user_star;
truncate table user_discuss_star;
truncate table discuss_interest;
truncate table interest;
truncate table reply;
truncate table comment;
truncate table discuss;
truncate table user;

insert into `user`
values (null, 2, 'user1', '$2a$10$D3B3Z6UyCfHf1rA/vKQoGeBEorJ2qM4otJOVAxsDqcQiQDPX8K8Ny', '1@qq.com', null, null, null,
        null, false, default, '2020-07-14 15:07:23'),
       (null, 1, 'user2', '$2a$10$D3B3Z6UyCfHf1rA/vKQoGeBEorJ2qM4otJOVAxsDqcQiQDPX8K8Ny', '2@qq.com', null, null, null,
        null, false, default, '2020-07-15 15:07:23'),
       (null, 0, 'user3', '$2a$10$D3B3Z6UyCfHf1rA/vKQoGeBEorJ2qM4otJOVAxsDqcQiQDPX8K8Ny', '3@qq.com', null, null, null,
        null, false, default, '2020-07-16 15:07:23'),
       (null, 0, 'user4', '$2a$10$D3B3Z6UyCfHf1rA/vKQoGeBEorJ2qM4otJOVAxsDqcQiQDPX8K8Ny', '4@qq.com', null, null, null,
        null, true, default, '2020-07-17 15:07:23'),
       (null, 0, 'user5', '$2a$10$D3B3Z6UyCfHf1rA/vKQoGeBEorJ2qM4otJOVAxsDqcQiQDPX8K8Ny', '5@qq.com', null, null, null,
        null, true, default, '2020-07-18 15:07:23');
insert into `audit`
values (null, 2, 4, 1, 'bad user', default, default),
       (null, 2, 5, 2, 'very bad user', default, default);
insert into `interest`
values (null, 'interest1', 1),
       (null, 'interest2', 1),
       (null, 'interest3', 1),
       (null, 'interest4', 1),
       (null, 'interest5', 1);
insert into `discuss`
values (null, 1, 'discuss1', default, default, default, 2, '2020-07-14 16:07:23'),
       (null, 2, 'discuss2', default, default, default, 2, '2020-07-15 16:07:23'),
       (null, 3, 'discuss3', default, default, default, 2, '2020-07-16 16:07:23'),
       (null, 4, 'discuss4', default, default, default, 2, '2020-07-17 16:07:23'),
       (null, 5, 'discuss5', default, default, default, 2, '2020-07-18 16:07:23');
insert into `comment`
values (null, 1, 2, default, default, 0, '2020-07-14 17:07:23'),
       (null, 1, 3, default, default, 0, '2020-07-14 17:17:23'),
       (null, 2, 3, default, default, 0, '2020-07-15 17:07:23'),
       (null, 2, 4, default, default, 0, '2020-07-15 17:17:23'),
       (null, 3, 4, default, default, 0, '2020-07-16 17:07:23'),
       (null, 3, 5, default, default, 0, '2020-07-16 17:17:23'),
       (null, 4, 5, default, default, 0, '2020-07-17 17:07:23'),
       (null, 4, 1, default, default, 0, '2020-07-17 17:17:23'),
       (null, 5, 1, default, default, 0, '2020-07-18 17:07:23'),
       (null, 5, 2, default, default, 0, '2020-07-18 17:17:23');
insert into `reply`
values (null, 1, 3, 0, '2020-07-14 18:08:23', default),
       (null, 1, 4, 0, '2020-07-14 18:09:23', default),
       (null, 2, 4, 0, '2020-07-14 18:18:23', default),
       (null, 2, 5, 0, '2020-07-14 18:19:23', default),
       (null, 3, 4, 0, '2020-07-15 18:08:23', default),
       (null, 3, 5, 0, '2020-07-15 18:09:23', default),
       (null, 4, 5, 0, '2020-07-15 18:18:23', default),
       (null, 4, 1, 0, '2020-07-15 18:19:23', default),
       (null, 5, 5, 0, '2020-07-16 18:08:23', default),
       (null, 5, 1, 0, '2020-07-16 18:09:23', default),
       (null, 6, 1, 0, '2020-07-16 18:18:23', default),
       (null, 6, 2, 0, '2020-07-16 18:19:23', default),
       (null, 7, 1, 0, '2020-07-17 18:08:23', default),
       (null, 7, 2, 0, '2020-07-17 18:09:23', default),
       (null, 8, 2, 0, '2020-07-17 18:18:23', default),
       (null, 8, 3, 0, '2020-07-17 18:19:23', default),
       (null, 9, 2, 0, '2020-07-18 18:08:23', default),
       (null, 9, 3, 0, '2020-07-18 18:09:23', default),
       (null, 10, 3, 0, '2020-07-18 18:18:23', default),
       (null, 10, 4, 0, '2020-07-18 18:19:23', default);
insert into `discuss_interest`
values (null, 1, 1),
       (null, 2, 1),
       (null, 3, 2),
       (null, 4, 2),
       (null, 1, 3),
       (null, 2, 3),
       (null, 3, 4),
       (null, 4, 4),
       (null, 1, 5),
       (null, 2, 5);
insert into `user_interest`
values (null, 1, 1),
       (null, 2, 1),
       (null, 2, 2),
       (null, 3, 2),
       (null, 3, 3),
       (null, 4, 3),
       (null, 4, 4),
       (null, 5, 4),
       (null, 5, 5),
       (null, 1, 5);
insert into `user_user_star`
values (null, 1, 3, '2020-07-19 00:00:00', default),
       (null, 1, 5, '2020-07-19 00:00:00', default),
       (null, 2, 4, '2020-07-19 00:00:00', default),
       (null, 2, 1, '2020-07-19 00:00:00', default),
       (null, 3, 5, '2020-07-19 00:00:00', default),
       (null, 3, 2, '2020-07-19 00:00:00', default),
       (null, 4, 1, '2020-07-19 00:00:00', default),
       (null, 4, 3, '2020-07-19 00:00:00', default),
       (null, 5, 2, '2020-07-19 00:00:00', default),
       (null, 5, 4, '2020-07-19 00:00:00', default);
