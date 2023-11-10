CREATE TABLE IF NOT EXISTS task (
                             id uuid primary key not null,
                             completed boolean not null,
                             createddate timestamp(6) without time zone,
                             description character varying(255),
                             title character varying(255)
);