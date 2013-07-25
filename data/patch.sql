alter table customer_report add column  tracks      INT;
alter table customer_report add column  detected    INT;
alter table customer_report add column  revenue     INT;
alter table customer_report add column  accepted    BOOL;


alter table customer_report_item add column  detected       BOOL;
alter table customer_report_item add column  number         INT;
alter table customer_report_item add column  deleted         BOOL;