/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2013/6/9 16:18:11                            */
/*==============================================================*/

drop database if exists crevs;
create database crevs;
use crevs;

drop table if exists forwardprice;

drop table if exists pv;

drop table if exists settlementprice;

drop table if exists swap;

drop table if exists trader;

/*==============================================================*/
/* Table: forwardprice                                          */
/*==============================================================*/
create table forwardprice
(
   date                 date not null,
   month_year           varchar(30) not null,
   forwardprice         numeric(7,2),
   primary key (date, month_year)
);

/*==============================================================*/
/* Table: pv                                                    */
/*==============================================================*/
create table pv
(
   date                 date not null,
   tradeid              int,
   pv                   numeric(7,2),
   primary key (date)
);

/*==============================================================*/
/* Table: settlementprice                                       */
/*==============================================================*/
create table settlementprice
(
   date                 date not null,
   settlementprice      numeric(7,2),
   primary key (date)
);

/*==============================================================*/
/* Table: swap                                                  */
/*==============================================================*/
create table swap
(
   tradeid              int not null auto_increment,
   traderid             varchar(30),
   bookingdate          date,
   counterparty         varchar(30),
   buyorsell            varchar(30),
   fixedprice           numeric(7,2),
   floatingquotecode    varchar(30),
   quantity             int,
   pricingperiodstart   date,
   pricingperiodend     date,
   settlementspecification varchar(30),
   primary key (tradeid)
);

/*==============================================================*/
/* Table: trader                                                */
/*==============================================================*/
create table trader
(
   traderid             varchar(30) not null,
   name                 varchar(30),
   password             varchar(30),
   primary key (traderid)
);

alter table pv add constraint FK_Reference_2 foreign key (tradeid)
      references swap (tradeid) on delete restrict on update restrict;

alter table swap add constraint FK_Reference_1 foreign key (traderid)
      references trader (traderid) on delete restrict on update restrict;

