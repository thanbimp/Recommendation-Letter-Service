# Σύστημα συστατικών επιστολών

Η εφαρμογή αυτή δημιουργήθκε στα πλαίσια του μαθήματος "Κατανεμνημένα Συστήματα" του 5ου εξαμήνου στο Χαροκόπειο
Πανεπιστήμιο. Η εφαρμογή επιτρέπει στους φοιτητές να αιτηθόυν μια συστατική επιστολή απο κάποιον καθηγητή τους. Η
συστατική επιστολή μετά την σύνταξη της απο τον παραλήπτη αποστέλλεται με e-mail στον φορέα απο όπου ζητήθηκε.

Η εφαρμογή έχει υλοποιηθεί με Spring Boot και με vanilla JS/HTML.

- Ομάδα 13
- Θέμα 3ο-Συστατικές Επιστολές

## Προαπαιτούμενα

Η εφαρμογή απαιτεί εναν MySQL (η οποιαδήποτε αλλη βαση με jbdc) server για την αποθήκευση δεδομένων καθως και έναν SMTP
mail server για την αποστολή emails.

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

Η εφαρμογή λαμβάνει ολες τις παραμέτρους της απο το αρχειο application.properties που βρίσκεται στον φάκελο
src/main/resources

Απο προεπιλογή η εφαρμογή επιχειρεί να συνδεθεί σε έναν MySQL διακομιστή στο localhost, που περιέχει το schema ds_2021
με όνομα χρήστη springuser και κωδικό thePassword.

Ακολουθεί επεξήγηση των παραμέτρων που λαμβάνει η εφαρμογή.

- spring.datasource.url : Το jbdc url της βάσης δεδομένων που θα χρησημοποιηθεί.
- spring.datasource.username : To όνομα χρήστη που θα χρησημοποιθεί για την σύνδεση στη βάση δεδομένων.
- spring.datasource.password : Ο κωδικός που θα χρησημοποιθεί για την σύνδεση στη βάση δεδομένων.
- email.email : Η διεύθυνση email που θα χρησημοποιηθεί για σύνδεση στον SMTP server αλλα και απο όπου θα προέρχονται τα
  email που αποστέλλει η εφαρμογή.
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

Έπειτα η εφαρμογή θα εκτελέιται και θα "ακόυει" στην θύρα 8080 (μπορεί να αλαχθει στο application.properties).

---

# System for reccomendation letters

This application was created within the course "Distributed Systems" of the 5th semester at Harokopio University. The
application allows students to request a letter of recommendation from a professor. The letter of recommendation after
its compilation by the professor is sent by e-mail to the institution from which it was requested.

The application was implemented with Spring Boot and vanilla JS / HTML.

- Group 13
- Topic 3 - Letters of Recommendation

## Prerequisites

The application requires a MySQL (or any other database with jbdc) server for data storage as well as an SMTP mail
server for sending emails.

A database with the tables whose creation commands are listed below is required.

```sql
create table users
(
    email varchar (45) not null
        primary key,
    password varchar (256) not null,
    FName varchar (45) not null,
    LName varchar (45) not null,
    acc_type tinyint (1) not null,
    phone_no varchar (45) not null
);
```

```sql
create table applications
(
    app_id varchar (255) not null,
    prof_email varchar (255) null,
    body varchar (255) null,
    studfname varchar (255) null,
    studlname varchar (255) null,
    Accepted tinyint (1) null,
    from_mail varchar (255) null,
    time_stamp varchar (255) null,
    letter_receiver_email varchar (255) not null,
    letter_id varchar (255) null,
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
    letter_id varchar (255) not null,
    app_id varchar (255) null,
    proffname varchar (255) null,
    proflname varchar (255) null,
    body text not null,
    receiver_email varchar (255) not null,
    constraint letters_letter_id_uindex
        unique (letter_id),
    constraint FK_LETTERS_ON_APP
        foreign key (app_id) references applications (app_id)
);

alter table letters
    add primary key (letter_id);
```

## Configuration

The application receives all its parameters from the application.properties file located in the src/main/resources
folder

By default, the application attempts to connect to a MySQL server on localhost, which contains the schema ds_2021 using
springuser as the username and thePassword as the password.

The following is an explanation of the parameters received by the application.

- spring.datasource.url: The jbdc url of the database to be used.
- spring.datasource.username: The username that will be used to connect to the database.
- spring.datasource.password: The password that will be used to connect to the database.
- email.email: The email address that will be used to connect to the SMTP server but also where the emails sent by the
  application will come from.
- email.password: The password required to connect to the SMTP Server.
- email.smtp-server: The network address of the SMTP server.
- email.smtp-port: The SMTP server port.
- email.smtp-auth: (true or false) If authentication by SMTP server is required.
- email.smtp-s-s-l: (true or false) If SSL encryption is required from the SMTP server.

In the application.properties file provided in the repository, all values except the first 3 are empty.

## Execution

To compile the application, run the ds_group_13 folder:

```bash
./mvnw package
```

In linux / unix / powershell

Or

```cmd
mvnw.cmd package
```

In windows command prompt

After completing the above command, run the ds_group_13-0.0.1-SNAPSHOT.jar file located in the target folder.

The application will then run and "listen" to port 8080 (can be changed in application.properties).
