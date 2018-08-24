drop database if exists esmar;
create database esmar;
use esmar;


create table tipoProductoCuero(
idTipoProducto int not null auto_increment primary key,
tipoProducto varchar(30)
); 


create table tipoCalibre(
idTipoCalibre int not null auto_increment primary key,
descripcion varchar(30)
);


create table seleccionCuero(
idSeleccion int not null auto_increment primary key,
seleccion varchar (30)
);


create table cueroTrabajar(
idCueroTrabajar int not null auto_increment primary key,
idTipoProducto int not null,
noPartida int,
noPiezas int,
noPiezasActuales int,
descripcion varchar(30),
fecha date,
key (idTipoProducto),
foreign key (idTipoProducto) references tipoProductoCuero (idTipoProducto)
on delete cascade on update cascade
);


create table calibrar(
idCalibrar int not null auto_increment primary key,
idCueroTrabajar int not null,
idTipoCalibre int not null,
noPiezas int,
noPiezasActuales int,
fecha date,
key (idCueroTrabajar),
foreign key (idCueroTrabajar) references cueroTrabajar (idCueroTrabajar)
on delete cascade on update cascade,
key (idTipoCalibre),
foreign key (idTipoCalibre) references tipoCalibre (idTipoCalibre)
on delete cascade on update cascade
);


create table cueroSeleccionado(
idcueroSeleccionado int not null auto_increment primary key,
idCalibrar int not null,
idSeleccion int not null,
noPiezas int,
noPiezasActuales int,
fecha date,
key (idCalibrar),
foreign key (idCalibrar) references calibrar (idCalibrar)
on delete cascade on update cascade,
key (idSeleccion),
foreign key (idSeleccion) references seleccionCuero (idSeleccion)
on delete cascade on update cascade
);


create table cueroPesado(
idcueroPesado int not null auto_increment primary key,
idcueroSeleccionado int not null,
peso float,
pesoActual float,
noPiezas int,
noPiezasActuales int,
fecha date,
key (idcueroSeleccionado),
foreign key (idcueroSeleccionado) references cueroSeleccionado (idcueroSeleccionado)
on delete cascade on update cascade
);


create table invDispTrabajar(
IdinvDispTrabajar int not null auto_increment primary key,
idcueroPesado int not null,
peso float,
pesoActual float,
noPiezas int,
noPiezasActuales int,
fecha date,
key (idcueroPesado),
foreign key (idcueroPesado) references cueroPesado (idcueroPesado)
on delete cascade on update cascade
);


create table productoSaldo(
idProductoSaldo int not null auto_increment primary key,
idTipoProducto int not null,
idTipoCalibre int not null,
idSeleccion int not null,
peso float,
pesoActual float,
noPiezas int,
noPiezasActuales int,
descripcion varchar(30),
fecha date,
key (idTipoProducto),
foreign key (idTipoProducto) references tipoProductoCuero (idTipoProducto)
on delete cascade on update cascade,
key (idTipoCalibre),
foreign key (idTipoCalibre) references tipoCalibre (idTipoCalibre)
on delete cascade on update cascade,
key (idSeleccion),
foreign key (idSeleccion) references seleccionCuero (idSeleccion)
on delete cascade on update cascade
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


create table pedaceraTrabajar(
idPedacera int not null auto_increment primary key,
idPedacerafk int not null,
peso float,
pesoActual float,
fecha date,
foreign key (idPedacerafk) references pedacera (idPedacera)
on delete cascade on update cascade
);


create table terminadoVenta(
idTerminadoVenta int not null auto_increment primary key,
idInvDispTrabajar int not null,
peso float,
pesoActual float,
noPiezas int,
noPiezasActuales int,
fecha date,
key (idInvDispTrabajar),
foreign key (idInvDispTrabajar) references invDispTrabajar (idInvDispTrabajar)
on delete cascade on update cascade
);


create table saldoVenta(
idSaldoVenta int not null auto_increment primary key,
idSaldoTerminadoTrabajar int not null,
peso float,
pesoActual float,
noPiezas int,
noPiezasActuales int,
fecha date,
key (idSaldoTerminadoTrabajar),
foreign key (idSaldoTerminadoTrabajar) references saldoTerminadoTrabajar (idSaldoTerminadoTrabajar)
on delete cascade on update cascade
);


create table pedaceraVenta(
idPedaceraVenta int not null primary key,
idPedacera int not null,
peso float,
pesoActual float,
fecha date,
key (idPedacera),
foreign key (idPedacera) references pedaceraTrabajar (idPedacera)
on delete cascade on update cascade
);


create table cliente(
idCliente int not null primary key,
nombre varchar (40),
calle varchar (40),
colonia varchar (40),
noInterior varchar (10),
noExterior varchar (10),
contacto varchar(40),
rfc varchar()
);


create table pedido(
);


create table pedidoDetalle(
);

create table moneda(
);


create table venta(
);


create table ventaDetalle(
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

