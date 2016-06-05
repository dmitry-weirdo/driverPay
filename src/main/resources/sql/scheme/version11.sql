alter table payment_documents add column type text;
alter table payment_documents add column method text;
alter table transactions add column nominal_code text;
alter table transactions add column tax_code text;

create table files (
    id integer primary key autoincrement,
    content blob
);

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