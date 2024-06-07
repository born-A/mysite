desc user;

-- join
insert into user values(null, '관리자', 'admin@mysicte.com', password('1234'), 'male', current_date());

-- login
select no, name from user where email = 'kickscar@gmail.com' and password=password('1234');

-- test
select * from user; 
