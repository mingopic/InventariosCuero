drop database if exists esmar_t;
create database esmar_t;
use esmar_t;

create table tipoProductoCuero(
idTipoProducto int not null auto_increment primary key,
tipoProducto varchar(30)
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

create table tipoCalibre(
idTipoCalibre int not null auto_increment primary key,
descripcion varchar(30)
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

create table seleccionCuero(
idSeleccion int not null auto_increment primary key,
seleccion varchar (30)
);

create table cueroSeleccionado(
idcueroSeleccionado int not null auto_increment primary key,
noPartida int,
tipoProducto varchar(20),
calibre varchar(20),
seleccion varchar(30),
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
seleccion varchar(30),
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
seleccion varchar(30),
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
tipoProducto varchar(20),
calibre varchar(20),
seleccion varchar(30),
peso float,
noPiezas int,
noPiezasActuales int,
descripcion varchar(30),
fecha date
);


create table usuario(
idUsuario int not null AUTO_INCREMENT  primary key,
usuario varchar(15),
contrasenia varchar(15),
nombre varchar(30),
tipo varchar(20)
);




insert into tipoProductoCuero values 
(null,'Crupon Sillero'), (null,'Centro Quebracho'), (null,'Centro Castaño'), (null,'Delantero Sillero'), (null,'Delantero Doble'), (null,'Delantero Suela');

insert into tipoCalibre values 
(null,'4-5'), (null,'5-6'), (null,'6-7'), (null,'7-8'), (null,'8-9'), (null,'9-10'), (null,'10-11'), (null,'40-45'), (null,'45-50'), (null,'50-55'), (null,'55-60'), (null,'60-65'), (null,'65-70');

insert into seleccionCuero values 
(null,'Estandar'), (null,'B'), (null,'C'), (null,'D'), (null,'E'), (null,'F');

insert into usuario values 
(null,'mingo','123','Domingo Luna','Sistemas'), (null,'manuel','463mr','Manuel Rodriguez','Semiterminado'), (null,'fernando','f.robless','Fernando Robles','Produccion');

