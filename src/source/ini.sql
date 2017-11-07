create table if not exists client (
    id int(15) not null primary key AUTO_INCREMENT,
    first_name varchar(255) not null,
    secord_name varchar(255) not null
);

create table if not exists school (
    id int(15) not null primary key AUTO_INCREMENT,
    school_name varchar(255) not null,
    shcool_short_name varchar(255) not null,
    address varchar(800) not null,
    phone varchar(255) not null
);

create table if not exists client_record (
    id int(15) not null primary key AUTO_INCREMENT,
    invoice_num varchar(100) not null,
    rows_count int(5) not null,
    description varchar(255) not null,
    sigle_amount float(10),
    total_amount float(10)
);
create table if not exists school_record (
    id int(15) not null primary key AUTO_INCREMENT,
    school_id int(15) not null,
    invoice_num varchar(100) not null,
    rows_count int(5) not null,
    student_ids varchar(800) not null,
    student_names varchar(800) not null,
    programmes TEXT not null,
    start_dates varchar(800) not null,
    fees float(10) not null,
    rate float(10) not null,
    commission float(10) not null,
    gst_amount float(10) not null,
    total_amount float(10) not null
);
create table if not exists company (
    id int(15) not null primary key AUTO_INCREMENT,
    company_name varchar(255) not null,
    address varchar(255) not null,
    post_address varchar(255) not null,
    phone varchar(50) not null,
    email varchar(255) not null,
    gst varchar(255) not null,
    account_name varchar(255) not null,
    account_number varchar(255) not null,
    bank varchar(255) not null,
    swift_code varchar(255) not null
);