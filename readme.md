# Κατανεμνημένα συστήματα 2021-2022
-  Ομάδα 13 
- Θέμα 3ο-Συστατικές Επιστολές

## Προαπαιτούμενα

Η εφαρμογή απαιτεί εναν MySQL (η οποιαδήποτε αλλη βαση με jbdc) server για την αποθήκευση δεδομένων καθως και έναν SMTP mail server για την αποστολή emails.

Απαιτείτε μία βάση δεδομένων με τα tables που οι εντολές δημιουργίας τους παρατίθενται απο κάτω.

```sql
create table users
(
    email    varchar(45)  not null
        primary key,
    password varchar(256) not null,
    FName    varchar(45)  not null,
    LName    varchar(45)  not null,
    acc_type tinyint(1)   not null,
    phone_no varchar(45)  not null
);
```

```sql
create table applications
(
    app_id                varchar(255) not null,
    prof_email            varchar(255) null,
    body                  varchar(255) null,
    studfname             varchar(255) null,
    studlname             varchar(255) null,
    Accepted              tinyint(1)   null,
    from_mail             varchar(255) null,
    time_stamp            varchar(255) null,
    letter_receiver_email varchar(255) not null,
    letter_id             varchar(255) null,
    constraint applications_app_id_uindex
        unique (app_id)
);

create index applications_letters_id_fk
    on applications (letter_id);

alter table applications
    add primary key (app_id);
```

```sql
create table letters
(
    letter_id      varchar(255) not null,
    app_id         varchar(255) null,
    proffname      varchar(255) null,
    proflname      varchar(255) null,
    body           text         not null,
    receiver_email varchar(255) not null,
    constraint letters_letter_id_uindex
        unique (letter_id),
    constraint FK_LETTERS_ON_APP
        foreign key (app_id) references applications (app_id)
);

alter table letters
    add primary key (letter_id);
```

## Παραμετροποίηση

Η εφαρμογή λαμβάνει ολες τις παραμέτρους της απο το αρχειο application.properties που βρίσκεται στον φάκελο src/main/resources

Απο προεπιλογή η εφαρμογή επιχειρεί να συνδεθεί σε έναν MySQL διακομιστή στο localhost, που περιέχει το schema ds_2021 με όνομα χρήστη springuser και κωδικό thePassword.

Ακολουθεί επεξήγηση των παραμέτρων που λαμβάνει η εφαρμογή.

- spring.datasource.url : Το jbdc url της βάσης δεδομένων που θα χρησημοποιηθεί.
- spring.datasource.username : To όνομα χρήστη που θα χρησημοποιθεί για την σύνδεση στη βάση δεδομένων.
- spring.datasource.password : Ο κωδικός που θα χρησημοποιθεί για την σύνδεση στη βάση δεδομένων.
- email.email : Η διεύθυνση email που θα χρησημοποιηθεί για σύνδεση στον SMTP server αλλα και απο όπου θα προέρχονται τα email που αποστέλλει η εφαρμογή.
- email.password : Ο κωδικός που απαιτείται για την σύνδεση στον SMTP Server.
- email.smtp-server : H διεύθυνση δικτύου του SMTP server.
- email.smtp-port : H θύρα του SMTP server.
- email.smtp-auth : (true η false) Αν απαιτείται ταυτοποίηση απο τον SMTP server.
- email.smtp-s-s-l : (true η false) Αν απαιτείται κρυπτογράφηση SSL απο τον SMTP server.

Στο αρχείο application.properties που παρέχεται στο αποθετήριο,όλες οι τιμές πλην των 3ων πρώτων είναι κενές.

## Εκτέλεση

Για να πραγματοποιθεί compilation της εφαρμογής, απο τον φάκελο ds_group_13 τρέχετε:

```bash
./mvnw package
```
Σε linux/unix/powershell

Ή

```cmd
mvnw.cmd package
```
Σε windows command prompt

Αφού ολοκληρωθεί η απο πάνω εντολή, εκτελέστε το αρχείο ds_group_13-0.0.1-SNAPSHOT.jar που βρίσκεται στον φάκελο target.

Έπειτα η εφαρμογή θα εκτελέιται και θα "ακόυει" στην θύρα 8080.
