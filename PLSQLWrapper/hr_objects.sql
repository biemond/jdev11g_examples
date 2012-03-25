create or replace type hr_department_row_type as object
(   department_id    number(4,0)
,   department_name  varchar2(30)
,   manager_id       number(6,0)
,   location_id      number(4,0)
);
/

create or replace type hr_department_type as table of hr_department_row_type;
/