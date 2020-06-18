drop database if exists esmar;
create database esmar;
use esmar;


create table tipoProductoCuero(
idTipoProducto int not null primary key,
tipoProducto varchar(30)
); 


create table tipoCalibre(
idTipoCalibre int not null primary key,
descripcion varchar(30)
);


create table seleccionCuero(
idSeleccion int not null primary key,
seleccion varchar (30)
);


create table cueroTrabajar(
idCueroTrabajar int not null auto_increment primary key,
noPartida int,
tipoProducto varchar(20),
noPiezas int,
noPiezasActuales int,
descripcion varchar(30),
fecha date
);


create table calibrar(
idCalibrar int not null auto_increment primary key,
noPartida int,
tipoProducto varchar(20),
calibre varchar(20),
noPiezas int,
noPiezasActuales int,
descripcion varchar(30),
fecha date
);


create table cueroSeleccionado(
idcueroSeleccionado int not null auto_increment primary key,
noPartida int,
tipoProducto varchar(20),
calibre varchar(20),
seleccion varchar(20),
noPiezas int,
noPiezasActuales int,
descripcion varchar(30),
fecha date
);


create table cueroPesado(
idcueroPesado int not null auto_increment primary key,
noPartida int,
tipoProducto varchar(20),
calibre varchar(20),
seleccion varchar(20),
peso float,
noPiezas int,
noPiezasActuales int,
descripcion varchar(30),
fecha date
);

create table invDispTrabajar(
IdinvDispTrabajar int not null auto_increment primary key,
noPartida int,
tipoProducto varchar(20),
calibre varchar(20),
seleccion varchar(30),
peso float,
noPiezas int,
noPiezasActuales int,
descripcion varchar(30),
fecha date
);


create table productoSaldo(
idProductoSaldo int not null auto_increment primary key,
tipoProducto varchar(20),
calibre varchar(20),
seleccion varchar(20),
peso float,
noPiezas int,
noPiezasActuales int,
descripcion varchar(30),
fecha date
);


create table pedacera(
idPedacera int not null auto_increment primary key,
peso float,
pesoActual float,
descripcion varchar(30),
fecha date
);

create table saldoTerminadoTrabajar(
idSaldoTerminadoTrabajar int not null auto_increment primary key,
idProductoSaldo int not null,
peso float,
pesoActual float,
noPiezas int,
noPiezasActuales int,
fecha date,
foreign key (idProductoSaldo) references productoSaldo (idProductoSaldo)
on delete cascade on update cascade
);

create table usuario(
idUsuario int not null AUTO_INCREMENT  primary key,
usuario varchar(15),
contrasenia varchar(15),
nombre varchar(30),
tipo varchar(20)
);

insert into tipoProductoCuero values 
(2,'Delantero Sillero'), (3,'Crupon Sillero'), (5,'Centro Castaño'), (6,'Centro Quebracho'), (7,'Delantero Suela'), (9,'Delantero Doble');

insert into tipoCalibre values 
(1,'4-5'),
(2,'5-6'),
(3,'6-7'),
(4,'7-8'),
(5,'8-9'),
(6,'9-10'),
(7,'10-11'),
(8,'40-45'),
(9,'45-50'),
(10,'50-55'),
(11,'55-60'),
(12,'60-65'),
(13,'65-70'),
(14,'3-3.5'),
(15,'3.5-4'),
(16,'2.5-3'),
(17,'3.2-3.6'),
(18,'2-2.8'),
(19,'2.8-3.2'),
(20,'POR CALIBRAR'),
(21,'3.5-4.1');

insert into seleccionCuero values
(1,	'Estandar'),
(2,	'A'),
(3,	'B'),
(4,	'C'),
(5,	'D'),
(6,	'E'),
(7,	'F'),
(8,	'TP'),
(9, '1'),
(10,'2'),
(11,'3'),
(12,'4'),
(13,'5'),
(14,'PARA CINTO'),
(15,'ESTANDAR PULIDO'),
(16,'SALDO AW'),
(17,'C PULIDO'),
(18,'PARA CERCO'),
(19,'PULIDOS'),
(20,'STD'),
(21,'POR SELECCIONAR'),
(22,'P/EXPORTACION');

insert into usuario values 
(null,'sis','pa55word','Domingo Luna','Sistemas'), (null,'siss','pa55word','Domingo Luna','Semiterminado'), (null,'adolfo','adayala16','Adolfo Ayala','Semiterminado'), (null,'fernando','f.robless','Fernando Robles','Produccion');

