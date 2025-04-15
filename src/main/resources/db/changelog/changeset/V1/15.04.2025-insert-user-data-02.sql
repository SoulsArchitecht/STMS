-- liquibase formatted sql

-- changeset serge_sh:15042025-02

insert into users (email, password, first_name, last_name, role)
values ('admin@admin.com','$2y$10$cFV3cv24YXKakb8btvHgm.iyiJiz4WPeSNtFWNHhSJMSWIAkWfhqy',
                                                                   'admin', 'adminov',
        'ROLE_ADMIN');

insert into users (email, password, first_name, last_name, role)
values ('1user@user.com','$2y$10$IVkIO9YPv6cDA4RN5Soa4OaXNu8LLykr/HOBC2QKS3FBUEVqmhyPe',
        'First', 'Firstov', 'ROLE_USER');

insert into users (email, password, first_name, last_name, role)
values ('2user@user.com','$2y$10$IVkIO9YPv6cDA4RN5Soa4OaXNu8LLykr/HOBC2QKS3FBUEVqmhyPe',
        'Second', 'Secondov', 'ROLE_USER');
insert into users (email, password, first_name, last_name, role)
values ('3user@user.com','$2y$10$IVkIO9YPv6cDA4RN5Soa4OaXNu8LLykr/HOBC2QKS3FBUEVqmhyPe',
        'Third', 'Thirdov', 'ROLE_USER');
