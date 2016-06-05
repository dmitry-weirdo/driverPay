create table hibernate_sequence (
    next_val integer
);

insert into hibernate_sequence (next_val) values (2);
insert into hibernate_sequence (next_val) values (1);
insert into hibernate_sequence (next_val) values (1);
insert into hibernate_sequence (next_val) values (1);
insert into hibernate_sequence (next_val) values (1);
insert into hibernate_sequence (next_val) values (1);
insert into hibernate_sequence (next_val) values (1);
insert into hibernate_sequence (next_val) values (1);

create table drivers (
    id integer primary key autoincrement,
    name text
);

create table balances (
    id integer primary key autoincrement,
    type text,
    driver_id integer,
    foreign key (driver_id) references drivers (id)
);

insert into balances (id,type,driver_id) values (null,'COMPANY',null);

create table payment_reasons (
    id integer primary key autoincrement,
    name text,
    driver_id integer,
    payment_type text,
    from_id integer,
    to_id integer,
    schedule_type text,
    opening_balance real,
    gross real,
    net real,
    vat real,
    total real,
    nominal_code text,
    tax_code text,
    frequency text,
    end_date integer,
    foreign key (driver_id) references drivers (id),
    foreign key (from_id) references balances (id),
    foreign key (to_id) references balances (id)
);

create table payments (
    id integer primary key autoincrement,
    name text,
    payment_reason_id integer,
    driver_id integer,
    payment_document_id integer,
    from_id integer,
    to_id integer,
    type text,
    status text,
    planned_date integer,
    net real,
    vat real,
    total real,
    nominal_code text,
    tax_code text,
    foreign key (payment_reason_id) references payment_reasons (id),
    foreign key (driver_id) references drivers (id),
    foreign key (payment_document_id) references payment_documents (id),
    foreign key (from_id) references balances (id),
    foreign key (to_id) references balances (id)
);

create table jobs (
    id integer primary key autoincrement,
    driver_id integer,
    payment_id integer,
    job_date integer,
    type text,
    pricing real,
    foreign key (driver_id) references drivers (id),
    foreign key (payment_id) references payments (id)
);

create table job_rates (
    id integer primary key autoincrement,
    job_id integer,
    net real,
    nominal_code text,
    tax_code text,
    foreign key (job_id) references jobs (id)
);

create table payment_documents (
    id integer primary key autoincrement,
    driver_id integer,
    payment_date integer,
    processed integer,
    foreign key (driver_id) references drivers (id)
);

create table transactions (
    id integer primary key autoincrement,
    payment_id integer,
    from_id integer,
    to_id integer,
    payment_date integer,
    net real,
    vat real,
    total real,
    foreign key (payment_id) references payments (id),
    foreign key (from_id) references balances (id),
    foreign key (to_id) references balances (id)
);

create table files (
    id integer primary key autoincrement,
    content blob
)

create table export_history (
    id integer primary key autoincrement,
    date integer,
    type text,
    file_id integer,
    foreign key (file_id) references files (id)
);

create table payment_document_exports (
    export_history_id integer,
    payment_document_id integer,
    foreign key (export_history_id) references export_history (id),
    foreign key (payment_document_id) references payment_documents (id)
);