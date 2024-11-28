insert ignore into users(username, password, active)
values("admin", "{noop}admin", 1)
;

insert ignore into roles(username, role)
values("admin", "ROLE_ADMIN")
;

