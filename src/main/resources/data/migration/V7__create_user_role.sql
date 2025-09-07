CREATE TABLE users_roles (
                             user_id               bigint not null,
                             role_id               int not null,
                             primary key (user_id, role_id),
                             foreign key (user_id) references user_entities (user_id),
                             foreign key (role_id) references role_entities (role_id)
);