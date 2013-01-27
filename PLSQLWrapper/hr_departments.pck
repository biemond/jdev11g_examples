create or replace package hr_departments
is
   type department_rec_type is record (  department_id   number(4,0)
                                    ,  department_name varchar2(30)
                                    ,  manager_id      number(6,0)
                                    ,  location_id     number(4,0)
                                    );

   type department_tab_type is table of department_rec_type index by binary_integer;

   procedure insertdepartments(p_records in department_tab_type);

   procedure insertdepartment(p_record in department_rec_type);

   function selectdepartments return department_tab_type;

   procedure test;
   
   procedure test2;
   
   procedure test3;  

end hr_departments;
/
create or replace package body hr_departments
is

procedure insertdepartments(p_records in department_tab_type)
is
begin
   forall i in p_records.first..p_records.last
     insert into departments ( department_id
                             , department_name
                             , manager_id
                             , location_id
                             )
                 values      ( p_records(i).department_id
                             , p_records(i).department_name
                             , p_records(i).manager_id
                             , p_records(i).location_id
                             );
end;

procedure insertdepartment(p_record in department_rec_type)
is
begin
     insert into departments ( department_id
                             , department_name
                             , manager_id
                             , location_id
                             )
                 values      ( p_record.department_id
                             , p_record.department_name
                             , p_record.manager_id
                             , p_record.location_id
                             );
end;

function selectdepartments
return department_tab_type 
as
  v_records department_tab_type;
begin 
  select * bulk collect into v_records from departments;

  return v_records;
end;


procedure test
is
  v_records department_tab_type;
  v_record1 department_rec_type;
begin
  v_record1.department_id := 501;
  v_record1.department_name := 'arnhem';
  v_record1.location_id := 1700;
  v_record1.manager_id := 100;

  v_records(1) := v_record1;

  v_record1.department_id := 502;
  v_record1.department_name := 'amersfoort';
  v_record1.location_id := 1700;
  v_record1.manager_id := 100;

  v_records(2) := v_record1;

  insertdepartments(p_records => v_records);
end;

procedure test2
is
   result hr_departments.department_tab_type;
begin
   result := selectdepartments;
   for i in result.first..result.last
   loop  
     dbms_output.put_line( result(i).department_id);
   end loop;  
end;

procedure test3
is
  v_record1 department_rec_type;
begin
  v_record1.department_id := 501;
  v_record1.department_name := 'arnhem';
  v_record1.manager_id := 100;
  v_record1.location_id := 1700;

  insertdepartment(p_record => v_record1);
end;


end hr_departments;
/
